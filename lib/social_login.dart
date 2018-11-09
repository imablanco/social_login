import 'dart:async';

import 'package:flutter/services.dart';

class SocialLogin {
  static const MethodChannel _channel =
      const MethodChannel('com.wembleystudios.social_login');

  Future<void> setConfig(SocialConfig socialConfig) {
    return _channel.invokeMethod(
      ChannelMethods.SET_CONFIG,
      socialConfig.toMap(),
    );
  }

  Future<FacebookUser> logInFacebookWithPermissions(
      List<String> permissions) async {
    final response = await _channel.invokeMethod(
      ChannelMethods.LOGIN_FACEBOOK,
      permissions,
    );
    return FacebookUser.fromMap(response);
  }

  Future<GoogleUser> logInGoogle() async {
    final response = await _channel.invokeMethod(ChannelMethods.LOGIN_GOOGLE);
    return GoogleUser.fromMap(response);
  }
}

class FacebookPermissions {
  static const EMAIL = "email";
  static const PUBLIC_PROFILE = "public_profile";

  FacebookPermissions._();
}

class ChannelMethods {
  static const SET_CONFIG = "set_config";
  static const LOGIN_FACEBOOK = "login_facebook";
  static const LOGIN_GOOGLE = "login_google";

  ChannelMethods._();
}

class SocialConfig {
  static const KEY_GOOGLE_WEB_CLIENT_ID = "google_web_client_id";
  final String googleWebClientId;

  SocialConfig({this.googleWebClientId});

  Map toMap() => {KEY_GOOGLE_WEB_CLIENT_ID: googleWebClientId};
}

class UserFields {
  static const ID = "id";
  static const EMAIL = "email";
  static const NAME = "name";
  static const PICTURE_URL = "picture_url";
  static const EXTRA_DATA = "extra_data";
  static const FACEBOOK_TOKEN = "facebook_token";
  static const GOOGLE_TOKEN = "google_token";
  static const GOOGLE_ID_TOKEN = "google_id_token";

  UserFields._();
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
    final extraMap = map[UserFields.EXTRA_DATA] as Map;
    return FacebookUser(
      map[UserFields.ID],
      map[UserFields.EMAIL],
      map[UserFields.NAME],
      map[UserFields.PICTURE_URL],
      extraMap[UserFields.FACEBOOK_TOKEN],
    );
  }
}

class GoogleUser extends SocialUser {
  final String token;
  final String idToken;

  GoogleUser(
    String id,
    String email,
    String name,
    String pictureUrl,
    this.token,
    this.idToken,
  ) : super(id, email, name, pictureUrl);

  factory GoogleUser.fromMap(Map map) {
    final extraMap = map[UserFields.EXTRA_DATA] as Map;
    return GoogleUser(
      map[UserFields.ID],
      map[UserFields.EMAIL],
      map[UserFields.NAME],
      map[UserFields.PICTURE_URL],
      extraMap[UserFields.GOOGLE_TOKEN],
      extraMap[UserFields.GOOGLE_ID_TOKEN],
    );
  }
}
