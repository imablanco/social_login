import 'package:flutter/material.dart';
import 'package:social_login/social_login.dart';

void main() => runApp(new MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => new _MyAppState();
}

class _MyAppState extends State<MyApp> {
  final SocialLogin socialLogin = SocialLogin();

  static const GOOGLE_WEB_CLIENT_ID =
      "371639311724-bsao7n8qbod70ubdidg93gbshhp251j8.apps.googleusercontent.com";

  SocialUser _socialUser;
  String _errorMessage = "Waiting";

  @override
  void initState() {
    super.initState();
    socialLogin.setConfig(SocialConfig(
      googleWebClientId: GOOGLE_WEB_CLIENT_ID,
    ));

    socialLogin.logInGoogle().then((socialUser) {
      setState(() {
        _socialUser = socialUser;
      });
    }, onError: (e) {
      setState(() {
        _errorMessage = "Error";
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      home: new Scaffold(
        appBar: new AppBar(
          title: const Text('Plugin example app'),
        ),
        body: new Center(
          child:
              new Text(_socialUser != null ? _socialUser.email : _errorMessage),
        ),
      ),
    );
  }
}
