//
//  FacebookHandler.swift
//  Runner
//
//  Created by Luis Paez Gonzalez on 13/11/2018.
//  Copyright © 2018 The Chromium Authors. All rights reserved.
//

import UIKit

import Flutter
import FBSDKCoreKit
import FBSDKLoginKit

class FacebookHandler {
    
    // MARK: Constants
    private static let ME_REQUEST_PARAM_FIELDS_KEY = "fields"
    private static let ME_REQUEST_PARAM_FIELDS_VALUE = "email, name, picture"
    private static let ME_REQUEST_RESPONSE_ID = "id"
    private static let ME_REQUEST_RESPONSE_EMAIL = "email"
    private static let ME_REQUEST_RESPONSE_NAME = "name"
    private static let ME_REQUEST_RESPONSE_PICTURE = "picture"
    private static let ME_REQUEST_RESPONSE_DATA = "data"
    private static let ME_REQUEST_RESPONSE_URL = "url"
    
    
    // MARK: Methods
    class func configuration(facebookAppID: String) {
        FBSDKSettings.setAppID(facebookAppID)
    }
        
    class func logInFacebookWithPermissions(permissions: [String], result: @escaping FlutterResult) {
        
        let sequence = Set(permissions)
        
        if FBSDKAccessToken.current() != nil {
            
        } else {
            let login = FBSDKLoginManager.init()
            login.logIn(withReadPermissions: permissions, from: UIApplication.shared.keyWindow?.rootViewController) { (fbresult, error) in
                
                if error != nil {
                    result (false)
                    return
                }
                
                if let bool = fbresult?.isCancelled, bool == true {
                    result (false)
                    return
                }
                
                if (fbresult?.token) != nil {
                    result (true)
                    return
                }
                
                result(false)
            }
        }
    }
    
    class func logOutFacebook() {
        FBSDKLoginManager().logOut()
        FBSDKAccessToken.setCurrent(nil)
        FBSDKProfile.setCurrent(nil)
    }
}
