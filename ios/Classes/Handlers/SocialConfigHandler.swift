//
//  SocialConfigHandler.swift
//  Bolts
//
//  Created by Luis Paez Gonzalez on 10/01/2019.
//

import Foundation


class SocialConfigHandler {
    
    static let shareInstance = SocialConfigHandler()
    private static let KEY_FACEBOOK_APP_ID = "facebook_app_id"
    
    var facebookAppId : String? = nil
    
    class func configuration(properties: [String: Any]) {
        shareInstance.facebookAppId = properties[KEY_FACEBOOK_APP_ID] as? String
    }
}
