import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../services/product_service.dart';
import '../services/auth_service.dart';
import 'login_screen.dart';
import 'product_detail_screen.dart'; // To be created
import 'cart_screen.dart'; // To be created
import 'order_screen.dart';

class HomeScreen extends StatefulWidget {
  @override
  _HomeScreenState createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  @override
  void initState() {
    super.initState();
    // Fetch products on init
    WidgetsBinding.instance.addPostFrameCallback((_) {
      Provider.of<ProductService>(context, listen: false).fetchProducts();
    });
  }

  @override
  Widget build(BuildContext context) {
    final authService = Provider.of<AuthService>(context);
    final productService = Provider.of<ProductService>(context);

    return Scaffold(
      appBar: AppBar(
        title: Text('Cosmetic Shop'),
        actions: [
          IconButton(
            icon: Icon(Icons.shopping_cart),
            onPressed: () {
               Navigator.of(context).push(MaterialPageRoute(builder: (_) => CartScreen()));
            },
          ),
          IconButton(
            icon: Icon(Icons.history),
            onPressed: () {
               Navigator.of(context).push(MaterialPageRoute(builder: (_) => OrderScreen()));
            },
          ),
          IconButton(
            icon: Icon(Icons.exit_to_app),
            onPressed: () {
              authService.logout();
              Navigator.of(context).pushReplacement(MaterialPageRoute(builder: (_) => LoginScreen()));
            },
          ),
        ],
      ),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: TextField(
              decoration: InputDecoration(
                labelText: 'Search Products',
                suffixIcon: Icon(Icons.search),
                border: OutlineInputBorder(),
              ),
              onSubmitted: (value) {
                if (value.isEmpty) {
                  productService.fetchProducts();
                } else {
                  productService.searchProducts(value);
                }
              },
            ),
          ),
          Expanded(
            child: productService.products.isEmpty
                ? Center(child: CircularProgressIndicator()) // Or empty text
                : GridView.builder(
                    padding: const EdgeInsets.all(10.0),
                    itemCount: productService.products.length,
                    gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
                      crossAxisCount: 2,
                      childAspectRatio: 3 / 4,
                      crossAxisSpacing: 10,
                      mainAxisSpacing: 10,
                    ),
                    itemBuilder: (ctx, i) {
                      final product = productService.products[i];
                      return GestureDetector(
                        onTap: () {
                           Navigator.of(context).push(MaterialPageRoute(builder: (_) => ProductDetailScreen(product: product)));
                        },
                        child: GridTile(
                          footer: GridTileBar(
                            backgroundColor: Colors.black54,
                            title: Text(product.name, textAlign: TextAlign.center),
                            subtitle: Text('\$${product.price}'),
                          ),
                          child: Container(
                            color: Colors.grey[200],
                            child: Icon(Icons.shopping_bag, size: 50, color: Colors.purple),
                             // Image.network(product.imageUrl) // If we had images
                          ),
                        ),
                      );
                    },
                  ),
          ),
        ],
      ),
    );
  }
}
