import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../services/order_service.dart';

class OrderScreen extends StatefulWidget {
  @override
  _OrderScreenState createState() => _OrderScreenState();
}

class _OrderScreenState extends State<OrderScreen> {
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      Provider.of<OrderService>(context, listen: false).fetchOrders();
    });
  }

  @override
  Widget build(BuildContext context) {
    final orderService = Provider.of<OrderService>(context);
    
    return Scaffold(
      appBar: AppBar(title: Text('My Orders')),
      body: orderService.orders.isEmpty
          ? Center(child: Text('No orders found'))
          : ListView.builder(
              itemCount: orderService.orders.length,
              itemBuilder: (ctx, i) {
                final order = orderService.orders[i];
                return ExpansionTile(
                  title: Text('Order #${order.id} - ${order.status}'),
                  subtitle: Text('Total: \$${order.totalAmount} - ${order.orderDate}'),
                  children: order.orderItems.map((item) {
                    return ListTile(
                      title: Text(item.product.name),
                      trailing: Text('${item.quantity} x \$${item.price}'),
                    );
                  }).toList(),
                );
              },
            ),
    );
  }
}
