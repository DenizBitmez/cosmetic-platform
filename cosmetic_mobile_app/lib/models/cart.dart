
import 'product.dart';

class CartItem {
  final int id;
  final Product product;
  final int quantity;
  final double price;

  CartItem({
    required this.id,
    required this.product,
    required this.quantity,
    required this.price,
  });

  factory CartItem.fromJson(Map<String, dynamic> json) {
    return CartItem(
      id: json['id'],
      product: Product.fromJson(json['product']),
      quantity: json['quantity'],
      price: (json['price'] as num).toDouble(),
    );
  }
}

class Cart {
  final int id;
  final List<CartItem> cartItems;
  final double totalAmount;

  Cart({
    required this.id,
    required this.cartItems,
    required this.totalAmount,
  });

  factory Cart.fromJson(Map<String, dynamic> json) {
    return Cart(
      id: json['id'],
      cartItems: (json['cartItems'] as List)
          .map((item) => CartItem.fromJson(item))
          .toList(),
      totalAmount: (json['totalAmount'] as num).toDouble(),
    );
  }
}
