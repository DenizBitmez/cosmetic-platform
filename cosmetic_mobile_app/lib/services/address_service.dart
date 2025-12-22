import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';
import '../models/address.dart';
import 'auth_service.dart';

class AddressService with ChangeNotifier {
  static const String baseUrl = AuthService.baseUrl;

  List<Address> _addresses = [];
  List<Address> get addresses => _addresses;

  Future<void> fetchAddresses() async {
    final prefs = await SharedPreferences.getInstance();
    final userId = prefs.getInt('userId');
    if (userId == null) return;

    final url = Uri.parse('$baseUrl/address/user/$userId');
    try {
      final response = await http.get(url);
      if (response.statusCode == 200) {
        final List<dynamic> data = jsonDecode(response.body);
        _addresses = data.map((json) => Address.fromJson(json)).toList();
        notifyListeners();
      }
    } catch (e) {
      print('Fetch addresses error: $e');
    }
  }

  Future<bool> addAddress(String title, String city, String district, String fullAddress) async {
    final prefs = await SharedPreferences.getInstance();
    final userId = prefs.getInt('userId');
    if (userId == null) return false;

    final url = Uri.parse('$baseUrl/address/add');
    try {
      final response = await http.post(
        url,
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode({
          'userId': userId,
          'title': title,
          'city': city,
          'district': district,
          'fullAddress': fullAddress,
        }),
      );

      if (response.statusCode == 201 || response.statusCode == 200) {
        await fetchAddresses(); // Refresh list
        return true;
      }
      return false;
    } catch (e) {
      print('Add address error: $e');
      return false;
    }
  }
}
