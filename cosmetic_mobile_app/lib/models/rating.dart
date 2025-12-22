class Rating {
  final int id;
  final int score;
  final String username; // Simplified for display

  Rating({
    required this.id,
    required this.score,
    required this.username,
  });

  factory Rating.fromJson(Map<String, dynamic> json) {
    return Rating(
      id: json['id'],
      score: json['score'],
      username: json['user'] != null ? json['user']['username'] ?? 'Anonymous' : 'Anonymous',
    );
  }
}
