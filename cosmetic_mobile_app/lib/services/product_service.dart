import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import '../models/product.dart';
import 'auth_service.dart';

class ProductService with ChangeNotifier {
  static const String baseUrl = AuthService.baseUrl;

  List<Product> _products = [];
  
  List<Product> get products => _products;

  Future<void> fetchProducts() async {
    final url = Uri.parse('$baseUrl/product/all');
    try {
      final response = await http.get(url);
      if (response.statusCode == 200) {
        final List<dynamic> data = jsonDecode(response.body);
        _products = data.map((json) => Product.fromJson(json)).toList();
        notifyListeners();
      } else {
        throw Exception('Failed to load products');
      }
    } catch (e) {
      print('Fetch products error: $e');
      throw e;
    }
  }

  Future<void> searchProducts(String query) async {
    final url = Uri.parse('$baseUrl/product/search?name=$query');
    try {
      final response = await http.get(url);
      if (response.statusCode == 200) {
        final List<dynamic> data = jsonDecode(response.body);
        _products = data.map((json) => Product.fromJson(json)).toList();
        notifyListeners();
      }
    } catch (e) {
      print('Search error: $e');
    }
  }

  Future<void> filterByCategory(String category) async {
    final url = Uri.parse('$baseUrl/product/category/$category');
    try {
      final response = await http.get(url);
      if (response.statusCode == 200) {
         final List<dynamic> data = jsonDecode(response.body);
        _products = data.map((json) => Product.fromJson(json)).toList();
        notifyListeners();
      }
    } catch (e) {
       print('Filter error: $e');
    }
  }
}
