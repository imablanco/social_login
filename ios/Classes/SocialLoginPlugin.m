#import "SocialLoginPlugin.h"
#import <social_login/social_login-Swift.h>

@implementation SocialLoginPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftSocialLoginPlugin registerWithRegistrar:registrar];
}
@end
