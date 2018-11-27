import Flutter
import UIKit


enum Method: String {
    case LOGIN_FACEBOOK = "login_facebook"
    case LOGOUT_FACEBOOK = "logout_facebook"
    case LOGIN_GOOGLE = "login_google"
    case LOGOUT_GOOGLE = "logout_google"
    case LOGIN_TWITTER = "login_twitter"
    case LOGOUT_TWITTER = "logout_twitter"
}


public class SwiftSocialLoginPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "social_login", binaryMessenger: registrar.messenger())
    let instance = SwiftSocialLoginPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    //result("iOS " + UIDevice.current.systemVersion)
    
    switch call.method {
        
    case Method.LOGIN_FACEBOOK.rawValue:        
        FacebookHandler.logInFacebookWithPermissions(permissions: [], result:result)
        break
        
    case Method.LOGOUT_FACEBOOK.rawValue:
        FacebookHandler.logOutFacebook()
        break
        
    case Method.LOGIN_GOOGLE.rawValue:
    
        break
        
    case Method.LOGOUT_GOOGLE.rawValue:
        
        break
        
    case Method.LOGIN_TWITTER.rawValue:
        
        break
        
    case Method.LOGOUT_TWITTER.rawValue:
        
        break
        
    default: break
        
    }
  }
}
