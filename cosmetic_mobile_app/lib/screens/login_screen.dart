import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../services/auth_service.dart';
import 'register_screen.dart';
// import 'home_screen.dart'; // To be created

class LoginScreen extends StatefulWidget {
  @override
  _LoginScreenState createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  final _formKey = GlobalKey<FormState>();
  final _emailController = TextEditingController();
  final _passwordController = TextEditingController();
  bool _isLoading = false;

  Future<void> _submit() async {
    if (!_formKey.currentState!.validate()) return;
    
    setState(() => _isLoading = true);
    final authProvider = Provider.of<AuthService>(context, listen: false);
    
    // For testing simplicity
    // final email = _emailController.text;
    // final password = _passwordController.text;
    
    final success = await authProvider.login(
      _emailController.text,
      _passwordController.text,
    );

    setState(() => _isLoading = false);

    if (success) {
      // Navigate to Home
      // Navigator.of(context).pushReplacement(MaterialPageRoute(builder: (_) => HomeScreen()));
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text('Login Successful!')));
    } else {
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text('Login Failed!')));
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Login')),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Form(
          key: _formKey,
          child: Column(
            children: [
              TextFormField(
                controller: _emailController,
                decoration: InputDecoration(labelText: 'Email'),
                validator: (val) => val!.isEmpty ? 'Please enter email' : null,
              ),
              TextFormField(
                 controller: _passwordController,
                 decoration: InputDecoration(labelText: 'Password'),
                 obscureText: true,
                 validator: (val) => val!.isEmpty ? 'Please enter password' : null,
              ),
              SizedBox(height: 20),
              if (_isLoading)
                CircularProgressIndicator()
              else
                ElevatedButton(
                  onPressed: _submit,
                  child: Text('Login'),
                ),
              TextButton(
                onPressed: () {
                   Navigator.of(context).push(MaterialPageRoute(builder: (_) => RegisterScreen()));
                },
                child: Text('Create Account'),
              )
            ],
          ),
        ),
      ),
    );
  }
}
