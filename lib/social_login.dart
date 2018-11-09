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
    return FacebookUser.fromMap(response);
  }
}

class FacebookPermissions {
  static const EMAIL = "email";
  static const PUBLIC_PROFILE = "public_profile";

  FacebookPermissions._();
}

class ChannelMethods {
  static const LOGIN_FACEBOOK = "login_facebook";

  ChannelMethods._();
}

abstract class SocialUser {
  final String id;
  final String email;
  final String name;
  final String pictureUrl;

  SocialUser(this.id, this.email, this.name, this.pictureUrl);
}

class FacebookUser extends SocialUser {
  final String token;

  FacebookUser(
    String id,
    String email,
    String name,
    String pictureUrl,
    this.token,
  ) : super(id, email, name, pictureUrl);

  factory FacebookUser.fromMap(Map map) {
    final extraMap = map['extra_data'] as Map;
    return FacebookUser(
      map['id'],
      map['email'],
      map['name'],
      map['picture_url'],
      extraMap['facebook_token'],
    );
  }
}
