import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';
import '../models/order.dart';
import 'auth_service.dart';

class OrderService with ChangeNotifier {
  static const String baseUrl = AuthService.baseUrl;

  List<Order> _orders = [];
  List<Order> get orders => _orders;

  Future<void> fetchOrders() async {
    final prefs = await SharedPreferences.getInstance();
    final userId = prefs.getInt('userId');
    if (userId == null) return;

    final url = Uri.parse('$baseUrl/order/user/$userId');
    try {
      final response = await http.get(url);
      if (response.statusCode == 200) {
        final List<dynamic> data = jsonDecode(response.body);
        _orders = data.map((json) => Order.fromJson(json)).toList();
        notifyListeners();
      }
    } catch (e) {
      print('Fetch orders error: $e');
    }
  }

  Future<bool> createOrder(int addressId) async {
    final prefs = await SharedPreferences.getInstance();
    final userId = prefs.getInt('userId');
    if (userId == null) return false;

    // API: POST /api/order/create/{userId}?addressId=... (Check backend signature)
    // Backend OrderController: createOrder(@PathVariable Integer userId, @RequestParam Long addressId)
    final url = Uri.parse('$baseUrl/order/create/$userId?addressId=$addressId');
    
    try {
      final response = await http.post(url);
      if (response.statusCode == 200 || response.statusCode == 201) {
        notifyListeners();
        return true;
      }
      return false;
    } catch (e) {
      print('Create order error: $e');
      return false;
    }
  }
}
