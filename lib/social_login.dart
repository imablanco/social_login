import 'dart:async';

import 'package:flutter/services.dart';

class SocialLogin {
  static const MethodChannel _channel =
      const MethodChannel('com.wembleystudios.social_login');

  Future<SocialUser> logInFacebookWithPermissions(
      List<String> permissions) async {
    final response = await _channel.invokeMethod(
      ChannelMethods.LOGIN_FACEBOOK,
      permissions,
    );
    return SocialUser.fromMap(response);
  }
}

class FacebookPermissions {
  static const EMAIL = "email";
  static const PUBLIC_PROFILE = "public_profile";
  static const FRIENDS = "user_friends";

  FacebookPermissions._();
}

class ChannelMethods {
  static const LOGIN_FACEBOOK = "login_facebook";

  ChannelMethods._();
}

class SocialUser {
  final String id;
  final String email;
  final String name;
  final String pictureUrl;
  final String token;
  final String idToken;

  SocialUser(
    this.id,
    this.email,
    this.name,
    this.pictureUrl,
    this.token,
    this.idToken,
  );

  factory SocialUser.fromMap(Map<dynamic, dynamic> map) {
    return SocialUser(
      map['id'],
      map['email'],
      map['name'],
      map['picture_url'],
      map['token'],
      map['id_token'],
    );
  }
}
