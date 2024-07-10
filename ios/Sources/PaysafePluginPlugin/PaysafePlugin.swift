import Foundation
import PaysafePaymentsSDK
import UIKit

@objc public class PaysafePlugin: NSObject {
    let paysafeSDKService = PaysafeSDKService()
    
    @objc public func setupSDK() {
        print("setupSDK")
        paysafeSDKService.setupSDK()
    }
    
    @objc public func setAppScheme(_ value: String) {
        print("setAppScheme", value)
        paysafeSDKService.set(scheme: value)
    }
    
    
    @objc public func setURLContext(_ urlContexts: Set<UIOpenURLContext>) {
        print("setURLContext", urlContexts)
        paysafeSDKService.set(urlContexts: urlContexts)
    }
    
    @objc public func initializeVenmo(completion: @escaping () -> Void) {
        print("initializeVenmo")
        paysafeSDKService.initializeVenmo(completion: completion)
    }
    
    @objc public func tokenize(consumerId: String, completion: @escaping (String) -> Void) {
        print("tokenize")
        paysafeSDKService.tokenize(consumerId: consumerId, completion: completion)
    }
}

class PaysafeSDKService {
    let apiKey = "c3V0LTM0ODg2MDpCLXFhMi0wLTVkM2VjYjMwLTEtMzAyYzAyMTQyYTM3NjgxMmE2YzJhYzRlNmQxMjI4NTYwNGMwNDAwNGU2NWI1YzI4MDIxNDU1N2EyNGFiNTcxZTJhOWU2MDVlNWQzMjk3MjZjMmIzZWNjNjJkNWY=="
    let venmoAccountId = "1002603510"
    let currencyCode = "USD"
    var venmo: PSVenmoContext? = nil
    
    func set(scheme: String) {
        PSVenmoContext.setURLScheme(scheme: scheme)
    }
    
    func set(urlContexts: Set<UIOpenURLContext>) {
        PSVenmoContext.setURLContexts(contexts: urlContexts)
    }
    
    func setupSDK() {
        PaysafeSDK
            .shared
            .setup(
                apiKey: apiKey,
                environment: .test) { result in
                    DispatchQueue.main.async {
                        switch result {
                        case .success:
                            print("sdk initialized")
                        case .failure(let error):
                            print(error.detailedMessage)
                        }
                    }
                }
    }
    
    func initializeVenmo(completion: @escaping () -> Void) {
        PSVenmoContext.initialize(
            currencyCode: "USD",
            accountId: self.venmoAccountId) { [weak self] result in
                DispatchQueue.main.async {
                    switch result {
                    case .success(let venmoContext):
                        print("venmo context initialized")
                        self?.venmo = venmoContext
                        completion()
                        
                    case .failure(let error):
                        print(error.detailedMessage)
                    }
                }
            }
    }
    
    func tokenize(consumerId: String, completion: @escaping (String) -> Void) {
        let totalPrice = 1 // $1
        let amount = Int(totalPrice * 100)
        let venmoTokenizeOptions = PSVenmoTokenizeOptions(
            amount: amount,
            currencyCode: currencyCode,
            transactionType: .payment,
            merchantRefNum: PaysafeSDK.shared.getMerchantReferenceNumber(),
            accountId: venmoAccountId,
            dupCheck: false,
            venmo: VenmoAdditionalData(
                consumerId: consumerId
            )
        )
        
        venmo?.tokenize(
            using: venmoTokenizeOptions) { result in
                DispatchQueue.main.async {
                    switch result {
                    case .success(let paymentToken):
                        print("tokenization finished successfully")
                        print("paymentToken: \(paymentToken)")
                        completion(paymentToken)
                    case .failure(let error):
                        print(error.detailedMessage)
                        completion(error.detailedMessage)
                    }
                }
            }
    }
}
