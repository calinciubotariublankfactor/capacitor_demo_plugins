import Foundation
import Capacitor

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(PaysafePluginPlugin)
public class PaysafePluginPlugin: CAPPlugin, CAPBridgedPlugin {
    public let identifier = "PaysafePluginPlugin"
    public let jsName = "PaysafePlugin"
    public let pluginMethods: [CAPPluginMethod] = [
        CAPPluginMethod(name: "echo", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "startVenmo", returnType: CAPPluginReturnPromise)
    ]
    private let implementation = PaysafePlugin()
    
    public override func load() {
        implementation.setupSDK()
    }

    @objc func echo(_ call: CAPPluginCall) {
        call.resolve()
    }
    
    @objc func startVenmo(_ call: CAPPluginCall) {
        let consumerId = call.getString("consumerId") ?? ""
        implementation.initializeVenmo { [weak self] in
            self?.implementation.tokenize(consumerId: consumerId) { result in
                call.resolve([
                    "paymentToken": result
                ])
            }
        }
    }
}
