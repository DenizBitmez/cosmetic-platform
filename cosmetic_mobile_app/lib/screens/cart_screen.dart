import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../services/cart_service.dart';
import '../services/order_service.dart';
import '../services/address_service.dart';
import 'add_address_screen.dart';

class CartScreen extends StatefulWidget {
  @override
  _CartScreenState createState() => _CartScreenState();
}

class _CartScreenState extends State<CartScreen> {
  @override
  void initState() {
    super.initState();
    // Fetch cart logic usually here or on init provider
     WidgetsBinding.instance.addPostFrameCallback((_) {
      Provider.of<CartService>(context, listen: false).fetchCart();
    });
  }

  @override
  Widget build(BuildContext context) {
    final cartService = Provider.of<CartService>(context);
    final cart = cartService.cart;

    return Scaffold(
      appBar: AppBar(title: Text('Your Cart')),
      body: cart == null || cart.cartItems.isEmpty
          ? Center(child: Text('Your cart is empty'))
          : Column(
              children: [
                Expanded(
                  child: ListView.builder(
                    itemCount: cart.cartItems.length,
                    itemBuilder: (ctx, i) {
                      final item = cart.cartItems[i];
                      return ListTile(
                        leading: CircleAvatar(child: Text('${item.quantity}x')),
                        title: Text(item.product.name),
                        subtitle: Text('Total: \$${(item.price * item.quantity).toStringAsFixed(2)}'),
                        trailing: IconButton(
                          icon: Icon(Icons.delete),
                          onPressed: () {
                             // Correctly using cart id logic as per backend removal? 
                             // Backend uses cartItemId (long).
                             // We don't have cartItemId field in CartItem model explicitly matched to "id" field?
                             // Wait, CartItem model has "int id". Backend DELETE uses "Long cartItemId".
                             // Should be fine.
                             cartService.removeFromCart(item.id);
                          },
                        ),
                      );
                    },
                  ),
                ),
                Card(
                  margin: EdgeInsets.all(15),
                  child: Padding(
                    padding: EdgeInsets.all(8),
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Text('Total', style: TextStyle(fontSize: 20)),
                        Spacer(),
                        Chip(
                          label: Text('\$${cart.totalAmount.toStringAsFixed(2)}', style: TextStyle(color: Colors.white)),
                          backgroundColor: Theme.of(context).primaryColor,
                        ),
                        TextButton(
                          child: Text('ORDER NOW'),
                          onPressed: () async {
                             // Implement Order Logic
                             try {
                               final addressProvider = Provider.of<AddressService>(context, listen: false);
                               final orderProvider = Provider.of<OrderService>(context, listen: false);
                               
                               await addressProvider.fetchAddresses();
                               
                               if (addressProvider.addresses.isEmpty) {
                                  ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text('No address found! Please add an address.')));
                                  
                                  Navigator.of(context).push(MaterialPageRoute(builder: (_) => AddAddressScreen()));
                                  return;
                               }
                               
                               // Pick first address for MVP
                               final addressId = addressProvider.addresses.first.id;
                               
                               final success = await orderProvider.createOrder(addressId);
                               
                               if (success) {
                                  ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text('Order Created Successfully!')));
                                  // Refresh cart (should be empty)
                                  Provider.of<CartService>(context, listen: false).clearCart(); // Backend clears it, but local state sync needed. Or fetchCart.
                                  Provider.of<CartService>(context, listen: false).fetchCart();
                                  Navigator.of(context).pop();
                               } else {
                                  ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text('Order Failed!')));
                               }
                             } catch (e) {
                                ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text('Error creating order: $e')));
                             }
                          },
                        )
                      ],
                    ),
                  ),
                ),
              ],
            ),
    );
  }
}
