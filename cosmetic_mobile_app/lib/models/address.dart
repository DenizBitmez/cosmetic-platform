class Address {
  final int id;
  final String title;
  final String city;
  final String district;
  final String fullAddress;

  Address({
    required this.id,
    required this.title,
    required this.city,
    required this.district,
    required this.fullAddress,
  });

  factory Address.fromJson(Map<String, dynamic> json) {
    return Address(
      id: json['id'],
      title: json['title'],
      city: json['city'],
      district: json['district'],
      fullAddress: json['fullAddress'],
    );
  }
}
