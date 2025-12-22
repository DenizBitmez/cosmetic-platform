import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../models/product.dart';
import '../services/cart_service.dart';
import '../services/rating_service.dart';

class ProductDetailScreen extends StatefulWidget {
  final Product product;

  ProductDetailScreen({required this.product});

  @override
  _ProductDetailScreenState createState() => _ProductDetailScreenState();
}

class _ProductDetailScreenState extends State<ProductDetailScreen> {
  int _quantity = 1;

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
       Provider.of<RatingService>(context, listen: false).fetchRatings(widget.product.id);
    });
  }

  void _showRatingDialog() {
    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        title: Text('Rate this Product'),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Wrap(
              children: List.generate(5, (index) {
                return IconButton(
                  icon: Icon(Icons.star_border), // Interactive stars could be implemented here
                  onPressed: () {
                     // Submit rating directly for MVP
                     Provider.of<RatingService>(context, listen: false)
                         .addRating(widget.product.id, index + 1)
                         .then((success) {
                            Navigator.of(ctx).pop();
                            ScaffoldMessenger.of(context).showSnackBar(
                                SnackBar(content: Text(success ? 'Rated successfully!' : 'Failed to rate'))
                            );
                         });
                  },
                );
              }),
            ),
            Text('Tap a star to rate directly'),
          ],
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final ratingService = Provider.of<RatingService>(context);
    final ratings = ratingService.ratings;
    double averageRating = 0;
    if (ratings.isNotEmpty) {
      averageRating = ratings.map((r) => r.score).reduce((a, b) => a + b) / ratings.length;
    }

    return Scaffold(
      appBar: AppBar(title: Text(widget.product.name)),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Container(
              height: 200,
              width: double.infinity,
              color: Colors.grey[300],
              child: Icon(Icons.shopping_bag, size: 100, color: Colors.purple),
            ),
            SizedBox(height: 20),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text(
                  widget.product.name,
                  style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
                ),
                Column(
                  children: [
                    Row(
                       children: [
                         Icon(Icons.star, color: Colors.amber),
                         Text(
                             ratings.isEmpty ? 'New' : averageRating.toStringAsFixed(1),
                             style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                         ),
                       ],
                    ),
                    TextButton(onPressed: _showRatingDialog, child: Text('Rate It'))
                  ],
                )
              ],
            ),
            Text(
              '\$${widget.product.price}',
              style: TextStyle(fontSize: 20, color: Colors.grey),
            ),
             SizedBox(height: 10),
             Text('Category: ${widget.product.category}'),
             Text('Stock: ${widget.product.stock}'),
             SizedBox(height: 20),
             Text('Comments (${ratings.length}):', style: TextStyle(fontWeight: FontWeight.bold)),
             Expanded(
               child: ratings.isEmpty 
               ? Text('No ratings yet.') 
               : ListView.builder(
                   itemCount: ratings.length,
                   itemBuilder: (ctx, i) => ListTile(
                     leading: Icon(Icons.person),
                     title: Text(ratings[i].username), // Requires User field
                     trailing: Row(
                       mainAxisSize: MainAxisSize.min,
                       children: [
                         Text(ratings[i].score.toString()),
                         Icon(Icons.star, size: 16, color: Colors.amber),
                       ],
                     ),
                   ),
                 ),
             ),
             Row(
               children: [
                 IconButton(
                   icon: Icon(Icons.remove),
                   onPressed: () {
                     setState(() {
                       if (_quantity > 1) _quantity--;
                     });
                   },
                 ),
                 Text('$_quantity', style: TextStyle(fontSize: 18)),
                 IconButton(
                   icon: Icon(Icons.add),
                   onPressed: () {
                      setState(() {
                         // Simple check against stock if available in model
                         if (_quantity < widget.product.stock) _quantity++;
                      });
                   },
                 ),
               ],
             ),
             SizedBox(height: 10),
             SizedBox(
               width: double.infinity,
               child: ElevatedButton(
                 style: ElevatedButton.styleFrom(
                   padding: EdgeInsets.symmetric(vertical: 16),
                 ),
                 onPressed: () async {
                    try {
                      await Provider.of<CartService>(context, listen: false)
                          .addToCart(widget.product.id, _quantity);
                      ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(content: Text('Added to cart!')),
                      );
                      Navigator.of(context).pop();
                    } catch (e) {
                      ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(content: Text('Failed to add to cart')),
                      );
                    }
                 },
                 child: Text('Add to Cart'),
               ),
             ),
          ],
        ),
      ),
    );
  }
}
