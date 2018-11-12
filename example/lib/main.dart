import 'package:flutter/material.dart';
import 'package:social_login/social_login.dart';

void main() => runApp(new MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => new _MyAppState();
}

class _MyAppState extends State<MyApp> {
  final SocialLogin socialLogin = SocialLogin();

  static const FACEBOOK_APP_ID = "2249712475303378";
  static const GOOGLE_WEB_CLIENT_ID =
      "371639311724-bsao7n8qbod70ubdidg93gbshhp251j8.apps.googleusercontent.com";
  static const TWITTER_CONSUMER_KEY = "LfNAorWdEOHjjz3ugRd3v3Fyu";
  static const TWITTER_CONSUMER_SECRET =
      "fomJMRxC1XzqLYTaFLqEcknLqfuJBG6naIS1BL3Umma4t4I6et";

  SocialUser _facebookUser;
  SocialUser _googleUser;
  SocialUser _twitterUser;

  @override
  void initState() {
    super.initState();
    socialLogin.setConfig(SocialConfig(
      facebookAppId: FACEBOOK_APP_ID,
      googleWebClientId: GOOGLE_WEB_CLIENT_ID,
      twitterConsumer: TWITTER_CONSUMER_KEY,
      twitterSecret: TWITTER_CONSUMER_SECRET,
    ));

    socialLogin.getCurrentFacebookUser().then((user) {
      setState(() {
        _facebookUser = user;
      });
    });

    socialLogin.getCurrentGoogleUser().then((user) {
      setState(() {
        _googleUser = user;
      });
    });

    socialLogin.getCurrentTwitterUser().then((user) {
      setState(() {
        _twitterUser = user;
      });
    });
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
            Padding(
              padding: const EdgeInsets.all(8.0),
              child: Row(
                children: <Widget>[
                  getFacebookButton(),
                  Expanded(
                    child: Padding(
                      padding: const EdgeInsets.only(left: 8.0),
                      child: SocialUserDetail(_facebookUser),
                    ),
                  )
                ],
              ),
            ),
            Padding(
              padding: const EdgeInsets.all(8.0),
              child: Row(
                children: <Widget>[
                  getGoogleButton(),
                  Expanded(
                    child: Padding(
                      padding: const EdgeInsets.only(left: 8.0),
                      child: SocialUserDetail(_googleUser),
                    ),
                  )
                ],
              ),
            ),
            Padding(
              padding: const EdgeInsets.all(8.0),
              child: Row(
                children: <Widget>[
                  getTwitterButton(),
                  Expanded(
                    child: Padding(
                      padding: const EdgeInsets.only(left: 8.0),
                      child: SocialUserDetail(_twitterUser),
                    ),
                  )
                ],
              ),
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

  Future<void> logOutFacebook() async {
    try {
      await socialLogin.logOutFacebook();
      _facebookUser = null;
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

  Future<void> logOutGoogle() async {
    try {
      await socialLogin.logOutGoogle();
      _googleUser = null;
      setState(() {});
    } catch (e) {
      print(e);
    }
  }

  Future<void> logInTwitter() async {
    try {
      _twitterUser = await socialLogin.logInTwitter();
      setState(() {});
    } catch (e) {
      print(e);
    }
  }

  Future<void> logOutTwitter() async {
    try {
      await socialLogin.logOutTwitter();
      _twitterUser = null;
      setState(() {});
    } catch (e) {
      print(e);
    }
  }

  Widget getFacebookButton() {
    final action = _facebookUser == null ? logInFacebook : logOutFacebook;
    final text = _facebookUser == null ? "Login FB" : "Logout FB";
    return RaisedButton(
      textColor: Colors.white,
      color: Colors.indigoAccent,
      onPressed: action,
      child: Text(text),
    );
  }

  Widget getGoogleButton() {
    final action = _googleUser == null ? logInGoogle : logOutGoogle;
    final text = _googleUser == null ? "Login G" : "Logout G";
    return RaisedButton(
      textColor: Colors.white,
      color: Colors.red,
      onPressed: action,
      child: Text(text),
    );
  }

  Widget getTwitterButton() {
    final action = _twitterUser == null ? logInTwitter : logOutTwitter;
    final text = _twitterUser == null ? "Login Tw" : "Logout TW";
    return RaisedButton(
      textColor: Colors.white,
      color: Colors.lightBlueAccent,
      onPressed: action,
      child: Text(text),
    );
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
        Expanded(
          child: Padding(
            padding: const EdgeInsets.only(left: 8.0),
            child: Text(_socialUser?.email ?? "-"),
          ),
        )
      ],
    );
  }
}
