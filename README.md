# social_login

[![pub package](https://img.shields.io/pub/v/social_login.svg)](https://pub.dartlang.org/packages/social_login)

A Flutter plugin to authenticate to social networks

# ARCHIVED
This repo is no longer below active development as the iOS implementation can not be handled at this time.

## Usage
To use this plugin, add `social_login` as a [dependency in your pubspec.yaml file](https://flutter.io/platform-plugins/).

### Example

``` dart
// Import package
import 'package:social_login/social_login.dart';

// Instantiate it
 final socialLogin = SocialLogin();

//Before calling any methods, set the configuration
socialLogin.setConfig(SocialConfig(
      facebookAppId: FACEBOOK_APP_ID,
      googleWebClientId: GOOGLE_WEB_CLIENT_ID, /*In case a Google tokenId is needed*/
      twitterConsumer: TWITTER_CONSUMER_KEY,
      twitterSecret: TWITTER_CONSUMER_SECRET,
    ));

// Get current logged user
 final FacebookUser facebookUser = await socialLogin.getCurrentFacebookUser();
 final GoogleUser googleUser = await socialLogin.getCurrentGoogleUser();
 final TwitterUser twitterUser = await socialLogin.getCurrentTwitterUser();

//Log in social networks
 final FacebookUser facebookUser = await socialLogin.logInFacebookWithPermissions(FacebookPermissions.DEFAULT);
 final GoogleUser googleUser = await socialLogin.logInGoogle();
 final TwitterUser twitterUser = await socialLogin.logInTwitter();

//Log out from social networks
 await socialLogin.logOutFacebook();
 await socialLogin.logOutGoogle();
 await socialLogin.logOutTwitter();

```
