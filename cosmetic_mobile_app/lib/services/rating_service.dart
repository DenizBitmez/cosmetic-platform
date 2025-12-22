import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';
import '../models/rating.dart';
import 'auth_service.dart';

class RatingService with ChangeNotifier {
  static const String baseUrl = AuthService.baseUrl;

  List<Rating> _ratings = [];
  List<Rating> get ratings => _ratings;

  Future<void> fetchRatings(int productId) async {
    final url = Uri.parse('$baseUrl/rating/product/$productId');
    try {
      final response = await http.get(url);
      if (response.statusCode == 200) {
        final List<dynamic> data = jsonDecode(response.body);
        _ratings = data.map((json) => Rating.fromJson(json)).toList();
        notifyListeners();
      }
    } catch (e) {
      print('Fetch ratings error: $e');
    }
  }

  Future<bool> addRating(int productId, int score) async {
    final prefs = await SharedPreferences.getInstance();
    final userId = prefs.getInt('userId');
    if (userId == null) return false;

    final url = Uri.parse('$baseUrl/rating/add');
    try {
      final response = await http.post(
        url,
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode({
          'userId': userId,
          'productId': productId, // Backend expects productId in DTO
          'score': score,
        }),
      );

      if (response.statusCode == 201 || response.statusCode == 200) {
        await fetchRatings(productId); // Refresh
        return true;
      }
      return false;
    } catch (e) {
      print('Add rating error: $e');
      return false;
    }
  }
}
