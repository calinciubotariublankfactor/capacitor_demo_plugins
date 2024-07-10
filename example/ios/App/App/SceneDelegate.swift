//
//  SceneDelegate.swift
//  App
//
//  Created by Calin Ciubotariu on 04.07.2024.
//

import UIKit
import TestPlugin

class SceneDelegate: UIResponder, UIWindowSceneDelegate {
    var window: UIWindow?
    
    let paysafePlugin: PaysafePlugin = PaysafePlugin()

    func scene(_ scene: UIScene, willConnectTo session: UISceneSession, options connectionOptions: UIScene.ConnectionOptions) {
        paysafePlugin.setAppScheme("com.example.plugin.payments")
    }
    
    func scene(_ scene: UIScene, openURLContexts URLContexts: Set<UIOpenURLContext>) {
        paysafePlugin.setURLContext(URLContexts)
    }
}
