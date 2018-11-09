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

  SocialUser _facebookUser;
  SocialUser _googleUser;

  @override
  void initState() {
    super.initState();
    socialLogin.setConfig(SocialConfig(
      googleWebClientId: GOOGLE_WEB_CLIENT_ID,
    ));
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('SocialLogin example app'),
        ),
        body: Column(
          children: <Widget>[
            Row(
              children: <Widget>[
                RaisedButton(
                  onPressed: logInFacebook,
                  child: Text("LogInFacebok"),
                ),
                SocialUserDetail(_facebookUser)
              ],
            ),
            Row(
              children: <Widget>[
                RaisedButton(
                  onPressed: logInGoogle,
                  child: Text("LogInGoogle"),
                ),
                SocialUserDetail(_googleUser)
              ],
            )
          ],
        ),
      ),
    );
  }

  Future<void> logInFacebook() async {
    try {
      _facebookUser = await socialLogin
          .logInFacebookWithPermissions(FacebookPermissions.DEFAULT);
      setState(() {});
    } catch (e) {
      print(e);
    }
  }

  Future<void> logInGoogle() async {
    try {
      _googleUser = await socialLogin.logInGoogle();
      setState(() {});
    } catch (e) {
      print(e);
    }
  }
}

class SocialUserDetail extends StatelessWidget {
  final SocialUser _socialUser;

  static const String DEFAULT_IMAGE_URL =
      "https://cdn.icon-icons.com/icons2/1378/PNG/512/avatardefault_92824.png";

  SocialUserDetail(this._socialUser);

  @override
  Widget build(BuildContext context) {
    return Row(
      crossAxisAlignment: CrossAxisAlignment.center,
      children: <Widget>[
        Image.network(
          _socialUser?.pictureUrl ?? DEFAULT_IMAGE_URL,
          height: 60.0,
          width: 60.0,
        ),
        Text(_socialUser?.email ?? "-")
      ],
    );
  }
}
