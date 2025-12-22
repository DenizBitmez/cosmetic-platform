import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';
import '../models/cart.dart';
import 'auth_service.dart';

class CartService with ChangeNotifier {
  static const String baseUrl = AuthService.baseUrl;
  
  Cart? _cart;
  
  Cart? get cart => _cart;

  Future<void> fetchCart() async {
    final prefs = await SharedPreferences.getInstance();
    final userId = prefs.getInt('userId');
    if (userId == null) return;

    final url = Uri.parse('$baseUrl/cart/$userId');
    try {
      final response = await http.get(url);
      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        _cart = Cart.fromJson(data);
        notifyListeners();
      }
    } catch (e) {
      print('Fetch cart error: $e');
    }
  }

  Future<void> addToCart(int productId, int quantity) async {
    final prefs = await SharedPreferences.getInstance();
    final userId = prefs.getInt('userId');
    if (userId == null) return;
    
    final url = Uri.parse('$baseUrl/cart/$userId/add?productId=$productId&quantity=$quantity');
    try {
      final response = await http.post(url);
      if (response.statusCode == 200) {
         final data = jsonDecode(response.body);
         _cart = Cart.fromJson(data);
         notifyListeners();
      }
    } catch (e) {
      print('Add to cart error: $e');
      throw e;
    }
  }

  Future<void> removeFromCart(int cartItemId) async {
    final prefs = await SharedPreferences.getInstance();
    final userId = prefs.getInt('userId');
    if (userId == null) return;

    final url = Uri.parse('$baseUrl/cart/$userId/remove/$cartItemId');
    try {
      final response = await http.delete(url);
      if (response.statusCode == 200) {
         final data = jsonDecode(response.body);
         _cart = Cart.fromJson(data);
         notifyListeners();
      }
    } catch (e) {
       print('Remove cart item error: $e');
    }
  }

  Future<void> clearCart() async {
     final prefs = await SharedPreferences.getInstance();
    final userId = prefs.getInt('userId');
    if (userId == null) return;

    final url = Uri.parse('$baseUrl/cart/$userId/clear');
    try {
      final response = await http.delete(url);
      if (response.statusCode == 204) {
        _cart = null; // Or empty cart structure
        fetchCart(); // Refresh
      }
    } catch (e) {
      print('Clear cart error: $e');
    }
  }
}
