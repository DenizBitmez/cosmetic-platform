import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../services/address_service.dart';

class AddAddressScreen extends StatefulWidget {
  @override
  _AddAddressScreenState createState() => _AddAddressScreenState();
}

class _AddAddressScreenState extends State<AddAddressScreen> {
  final _formKey = GlobalKey<FormState>();
  final _titleController = TextEditingController();
  final _cityController = TextEditingController();
  final _districtController = TextEditingController();
  final _fullAddressController = TextEditingController();
  bool _isLoading = false;

  Future<void> _submit() async {
    if (!_formKey.currentState!.validate()) return;

    setState(() => _isLoading = true);

    final success = await Provider.of<AddressService>(context, listen: false).addAddress(
      _titleController.text,
      _cityController.text,
      _districtController.text,
      _fullAddressController.text,
    );

    setState(() => _isLoading = false);

    if (success) {
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text('Address Added!')));
      Navigator.of(context).pop();
    } else {
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text('Failed to add address')));
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Add New Address')),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Form(
          key: _formKey,
          child: Column(
            children: [
              TextFormField(
                controller: _titleController,
                decoration: InputDecoration(labelText: 'Addrress Title (e.g. Home)'),
                validator: (val) => val!.isEmpty ? 'Please enter title' : null,
              ),
              TextFormField(
                controller: _cityController,
                decoration: InputDecoration(labelText: 'City'),
                validator: (val) => val!.isEmpty ? 'Please enter city' : null,
              ),
               TextFormField(
                controller: _districtController,
                decoration: InputDecoration(labelText: 'District'),
                validator: (val) => val!.isEmpty ? 'Please enter district' : null,
              ),
               TextFormField(
                controller: _fullAddressController,
                decoration: InputDecoration(labelText: 'Full Address'),
                maxLines: 3,
                validator: (val) => val!.isEmpty ? 'Please enter address' : null,
              ),
              SizedBox(height: 20),
              if (_isLoading)
                CircularProgressIndicator()
              else
                ElevatedButton(
                  onPressed: _submit,
                  child: Text('Save Address'),
                ),
            ],
          ),
        ),
      ),
    );
  }
}
