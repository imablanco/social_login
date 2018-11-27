//
//  FacebookHandler.swift
//  Runner
//
//  Created by Luis Paez Gonzalez on 13/11/2018.
//  Copyright © 2018 The Chromium Authors. All rights reserved.
//

import UIKit

import FBSDKCoreKit
import FBSDKLoginKit

class FacebookHandler: NSObject {
    
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
    class func logoutFromFacebook() {
        FBSDKLoginManager().logOut()
        FBSDKAccessToken.setCurrent(nil)
        FBSDKProfile.setCurrent(nil)
    }
}
