Paysafe Mobile SDK - Capacitor plugin documentation 



## **Paysafe Capacitor Plugin (for Venmo) Documentation**

### **Overview**
The Paysafe Capacitor Plugin for Payments API provides a bridge between the native Paysafe Payments SDKs (iOS and Android) and a Capacitor JS app. It allows the app to interact with **Paysafe SDK's Venmo implementation**, exposed through the SDK methods: `setupSDK()`, `initialize()`, and `tokenize()` from a Capacitor JS environment (cross-platform). This documentation will go through the folder structure, the plugin implementation, and the example Capacitor app.


### **Project Requirements**
- **iOS**: Minimum iOS version: 14.0
- **Android**: Minimum Android SDK version: 23, Compile SDK version: 34
- **Capacitor**: Capacitor 6+

### **Prerequisites**
Configure your development environment to support building and running capacitor apps and plugins. 
Use the following setup guide: https://capacitorjs.com/docs/getting-started/environment-setup

### **Steps to Run the Project**
1. **Clone the Project**
   Clone the repository that contains 2 directories:
   - `demo-plugin-kotlin`
   - `test-plugin-ios-and-java`

    Both directories contain: the Paysafe Capacitor plugin and the demo app. The only difference between them is the android implementation - one is using Java and the other one is Kotlin.

    The rest of the documentation addresses the `test-plugin-ios-and-java` project, but all steps and guides apply for both.

2. **Install Dependencies**
   Use the following command to install project dependencies:
   ```bash
   npm install
   ```

3. **Add Platforms (iOS and Android)**
   Add the necessary platforms to the example Capacitor app:
   ```bash
   cd example
   ```
   
    and then run.
   
   ```bash
   npx cap add ios
   npx cap add android
   ```

   Note: This step is not neccessary because the platforms are already added and configured for this demo, but you can regenerate the native projects if needed.

4. **Build the Projects and the demo app**
   Compile the web assets:
   ```bash
   npm run build-and-sync
   ```

   Note: This is a custom command that builds both the plugin and the demo app. Running `npx cap sync` will copy over your already built web bundle to both your Android and iOS projects as well as update the native dependencies that Capacitor uses.

    It is an alias for 
   ```bash
   npm run build && cd example && npx cap sync && cd ..
   ```

5. **Run the demo apps from terminal**
    For iOS
    ```bash
    cd example
    npx cap run ios
    ```

    For Android
    ```bash
    cd example
    npx cap run android
    ```

    These commands will open the apps on device/simulator/emulator, but won't open any debugger.

    *The recommended way to run and debug the projects/apps is through Xcode/Android Studio. Check the next step.*

6. **Open native IDEs**
   
    The app can also run on iOS via Xcode or on Android via Android Studio as well. Both options are valid for development.

    Opening the native project can give you full control over the native runtime of the application and plugins. You can create plugins or add custom native code.

   Xcode for **iOS**:
   ```bash
   cd example && npx cap open ios
   ```
   Android Studio for **Android**:
   ```bash
   cd example && npx cap open android
   ```


### **Demo App Usage**

The demo app demonstrates how to use the Paysafe Capacitor plugin. The app includes a button labeled **Venmo**, which invokes the `initialize()` and `tokenize()` methods when clicked.

#### **Usage in Demo App**
- **Setup Venmo**: On app launch, the `setupSDK()` method is automatically called when the plugin is loaded.
- **Tokenization**: Clicking the **Venmo** button invokes the `startVenmo()` method, initializing the payment with Venmo and tokenizing the payment details for the consumer.
- **Tokenization options**: 
  - Android: 
    - go to file at path `<project-root>/android/src/main/java/com/mycompany/plugins/example/PaysafeSDKService.java`
    - change and update the `apiKey`, `venmoAccountId` and the `currencyCode`
    - change the `PSTokenizeOptions` object if you want to test different scenarios
    
  - iOS: 
    - go to file at path `<project-root>/ios/Sources/PaysafePluginPlugin/PaysafePlugin.swift`
    - change and update the `apiKey`, `venmoAccountId` and the `currencyCode`
    - change the `PSTokenizeOptions` object if you want to test different scenarios
  


### **Project Folder Structure**
The project is organized into several key directories and files:

- **android/**: This folder contains the Android-specific Capacitor implementation of the Paysafe Plugin. It includes the necessary files to interface with the Paysafe Android SDK.
  
- **example/**: The example Capacitor app, demonstrating how to use the Paysafe SDK Plugin. It is a simple app that allows you to test the functionality of the plugin.

- **ios/**: This folder contains the iOS-specific Capacitor implementation of the Paysafe Plugin. It uses Cocoapods to integrate the Paysafe iOS SDK.

- **src/**: The core source folder for the plugin implementation. It includes the TypeScript definitions and registration logic for the plugin, as well as a web fallback implementation.

### **Explaining the `src/` Folder**
The `src/` folder contains three key files that define the Capacitor plugin and its interface. Let’s break them down:

1. **`definitions.ts`**
   This file defines the interface for the plugin, specifying the methods that the plugin exposes to the Capacitor app. It ensures type safety and provides the expected structure for plugin methods.

   Here is the place to define extra functions that the plugin will make available to typescript/javascript. Add as many as you need.

   ```typescript
   // src/definitions.ts
   export interface PaysafePluginPlugin {
     echo(): Promise<void>;        // A basic echo method for testing
     startVenmo(): Promise<string>; // Method to initiate a Venmo payment process
   }
   ```

2. **`index.ts`**
   This file registers the plugin with Capacitor. It defines the plugin for use in the app and ensures that the proper implementation is loaded, whether for iOS, Android, or web.

   ```typescript
   // src/index.ts
   import { registerPlugin } from '@capacitor/core';
   import type { PaysafePluginPlugin } from './definitions';

   const PaysafePlugin = registerPlugin<PaysafePluginPlugin>('PaysafePlugin', {
     web: () => import('./web').then(m => new m.PaysafePluginWeb()),  // Fallback web implementation
   });

   export * from './definitions';  // Export the interface
   export { PaysafePlugin };       // Export the plugin for use in the app
   ```

3. **`web.ts`**
   This file provides a fallback implementation of the plugin for web environments. Since the native functionality is unavailable on the web, this implementation logs to the console and simulates method calls.

   ```typescript
   // src/web.ts
   import { WebPlugin } from '@capacitor/core';
   import type { PaysafePluginPlugin } from './definitions';

   export class PaysafePluginWeb extends WebPlugin implements PaysafePluginPlugin {
     async echo(): Promise<void> {
       console.log('ECHO');  // Logs a message to simulate functionality
     }

     async startVenmo(): Promise<string> {
       console.log('startVenmo called');  // Logs a message to simulate Venmo start process
       return Promise.resolve("echo");    // Returns a mock result
     }
   }
   ```

### **The Example Capacitor App**

The example app is designed to demonstrate how the Paysafe Plugin can be used in a Capacitor project. It includes the following structure:

- **android/**: Android-specific files for the example app.
- **ios/**: iOS-specific files for the example app.
- **src/**: Contains the main HTML and JavaScript files for the example app.

#### **`index.html`**
The HTML file provides a basic UI for interacting with the Paysafe Plugin. It has an input field to capture the Venmo consumer ID and a button to trigger the venmo.

```html
<!doctype html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <title>Example Capacitor App</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no" />
  </head>
  <body>
    <main>
      <h1>Capacitor Test Plugin Project</h1>
      <p>This project tests the functionality of your plugin.</p>

      <label for="venmoConsumerIdInput">Venmo consumer id:</label>
      <input type="text" id="venmoConsumerIdInput" value="consumer+2028" />
      <button onclick="venmoFlow()">Venmo!</button>
    </main>

    <script src="./js/example.js" type="module"></script>
  </body>
</html>
```

This file includes a simple input form to collect a Venmo consumer ID and a button to trigger the `venmoFlow()` function.

#### **`example.js`**
The JavaScript file connects the HTML page with the Paysafe Plugin and defines the logic for calling the plugin’s methods.

```javascript
import { PaysafePlugin } from 'test-plugin';

// Function to test the echo method
window.testEcho = () => {
    PaysafePlugin.echo();
}

// Function to start the payment venmo using the consumer ID
window.venmoFlow = () => {
    const consumerId = document.getElementById("venmoConsumerIdInput").value;

    if (!consumerId) {
        console.log("Empty/null consumer id.");
        return;
    }

    PaysafePlugin.startVenmo({ consumerId: consumerId })
        .then((response) => {
            console.log('venmoFlow resolved with value: ' + response["paymentToken"]);
        })
        .catch((error) => {
            console.error('venmoFlow rejected with error: ' + error);
        });
}
```

This file:
- Imports the `PaysafePlugin` from the plugin.
- Defines two functions:
  1. **`testEcho()`**: This function calls the plugin’s `echo()` method for testing purposes.
  2. **`venmoFlow()`**: This function collects the consumer ID from the input field and calls the `startVenmo()` method from the plugin to initiate the payment flow.

7. **Configure the iOS and Android Projects**
   - **iOS**: Ensure that the `<project-root>/TestPlugin.podspec` file contains the dependency for Paysafe Payments SDK like in this example:
     ```ruby
        require 'json'

        package = JSON.parse(File.read(File.join(__dir__, 'package.json')))

        Pod::Spec.new do |s|
        s.name = 'TestPlugin'
        # ...

        s.ios.deployment_target  = '14.0'
        s.dependency 'Capacitor'
        s.dependency 'PaysafePaymentsSDK/PaysafeVenmo', '~> x.y.z'
        
        # ...
        end
     ```
     Run `pod install` in the `<project-root>/example/ios/App/` directory to install the plugin pods (that contain the SDK).

   - **Android**: Make sure the `<project-root>/android/build.gradle` file includes:
     ```gradle
     implementation 'com.github.company.paysafe_sdk_android_api:x.y.z'
     implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3"
     ```

### **Capacitor Plugin Structure**

The Paysafe Capacitor plugin exposes the following key methods:
- `startVenmo`: Initializes the Paysafe venmo and performs tokenization using the provided `consumerId`.
- `echo`: A placeholder method for simple testing.


### **Capacitor Plugin Implementation**

#### **iOS Plugin Code**

**Plugin Definition:**
```swift
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
        implementation.setupSDK() // Automatically sets up the SDK when the app loads
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
```

**Paysafe Plugin Implementation:**
This class handles the native operations using the PaysafeSDK.
```swift
import Foundation
import PaysafePaymentsSDK
import UIKit

@objc public class PaysafePlugin: NSObject {
    let paysafeSDKService = PaysafeSDKService()

    @objc public func setupSDK() {
        paysafeSDKService.setupSDK()
    }

    @objc public func initializeVenmo(completion: @escaping () -> Void) {
        paysafeSDKService.initializeVenmo(completion: completion)
    }

    @objc public func tokenize(consumerId: String, completion: @escaping (String) -> Void) {
        paysafeSDKService.tokenize(consumerId: consumerId, completion: completion)
    }
}

class PaysafeSDKService {
    let apiKey = ""
    let venmoAccountId = ""
    let currencyCode = "USD"
    var venmo: PSVenmoContext? = nil
    
    func set(scheme: String) {
        PSVenmoContext.setURLScheme(scheme: scheme)
    }
    
    func set(urlContexts: Set<UIOpenURLContext>) {
        PSVenmoContext.setURLContexts(contexts: urlContexts)
    }
    
    func setupSDK() {
        PaysafeSDK.shared.setup(apiKey: apiKey, environment: .test) { result in
            // ...
        }
    }
    
    func initializeVenmo(completion: @escaping () -> Void) {
        PSVenmoContext.initialize(
            currencyCode: currencyCode,
            accountId: self.venmoAccountId) { [weak self] result in
                // ...
            }
    }
    
    func tokenize(consumerId: String, completion: @escaping (String) -> Void) {
        let venmoTokenizeOptions = PSVenmoTokenizeOptions(...)
        
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
```

#### **Android Plugin Code**

The Android implementation uses Kotlin coroutines to handle asynchronous operations efficiently.

**Plugin Definition:**
```java
@CapacitorPlugin(name = "PaysafePlugin")
public class PaysafePluginPlugin extends Plugin {

    private final PaysafePlugin implementation = new PaysafePlugin();

    @Override
    public void load() {
        super.load();
        implementation.setupSDK();
    }

    @Override
    protected void handleOnStart() {
        super.handleOnStart();
        implementation.initializeVenmo(getActivity(), new VenmoInitializationCallback() {
            @Override
            public void onInitialized() {
                System.out.println("onInitialized call");
            }
        });
    }

    @PluginMethod
    public void echo(PluginCall call) {
        call.resolve();
    }

    @PluginMethod
    public void startVenmo(PluginCall call) {
        String consumerId = call.getString("consumerId");

        implementation.tokenize(getContext(), consumerId, new VenmoTokenizationCallback() {
            @Override
            public void onTokenized(String result) {
                JSObject ret = new JSObject();
                ret.put("paymentToken", result);
                call.resolve(ret);
            }
        });
    }
}


public class PaysafeSDKService {
    private String apiKey = "";
    private String venmoAccountId = "";
    private String currencyCode = "USD";

    private PSVenmoContext venmoContext = null;

    // Method to setup SDK
    public void setupSDK() {
        try {
            PaysafeSDK.INSTANCE.setup(apiKey, PSEnvironment.TEST);
            System.out.println("setup sdk successfully");
        } catch (PaysafeException | PaysafeRuntimeError e) {
            System.out.println("setup sdk failure " + e);
            throw new RuntimeException(e);
        }
    }

    // Method to initialize Venmo with a completion callback
    public void initializeVenmo(ComponentActivity activity, VenmoInitializationCallback completion) {
        PSVenmoConfig venmoConfig = new PSVenmoConfig(currencyCode, venmoAccountId);
        PSVenmoContext.Companion.initialize(activity, venmoConfig, new PSCallback<PSVenmoContext>() {
            // ...
        });
    }

    // Method to tokenize with a consumerId and a completion callback
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void tokenize(Context context, String consumerId, VenmoTokenizationCallback completion) {
        int totalPrice = 1;
        PSVenmoTokenizeOptions tokenizeOptions = new PSVenmoTokenizeOptions(...);


        Continuation<Unit> continuation = getContinuation();

        venmoContext.tokenize(context, tokenizeOptions, new PSVenmoTokenizeCallback() {
            @Override
            public void onSuccess(@NonNull String s) {
                System.out.println("Tokenization finished successfully");
                System.out.println("paymentToken: " + s);
                completion.onTokenized(s);
            }

            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Tokenization onFailure: " + e);
            }

            @Override
            public void onCancelled(@NonNull PaysafeException e) {
                System.out.println("Tokenization onCancelled: " + e);
            }
        }, continuation);
    }


}
```



##### **Tapping into the `handleOnStart()` Method**

The `handleOnStart()` method is used for handling the Android activity's lifecycle state change to "start". 
It is called/triggered when the activity moves to the "start" state and is necessary because the Paysafe Android SDK internally uses an **activity result launcher**. This launcher requires the activity to be in the started state to handle the Venmo flow, such as initializing the SDK and starting the tokenization flow. This method ensures that the activity is in the correct state when these actions are performed.

By overriding `handleOnStart()`, the plugin can notify the SDK that the activity is ready to handle any interactions or initialization steps that require an active and started activity.

```java
@Override
protected void handleOnStart() {
    super.handleOnStart();
    implementation.initializeVenmo(getActivity(), new VenmoInitializationCallback() {
        @Override
        public void onInitialized() {
            System.out.println("onInitialized call");
        }
    });
}
```

##### **Continuation and Coroutines Support**
The Android Paysafe SDK implementation uses Kotlin coroutines to manage asynchronous tasks - but Java does not have support for this Kotlin feature. The use of `Continuation<Unit>` allows the plugin to handle coroutines and asynchronous callbacks in Java, used for the venmo tokenization.

```java
@RequiresApi(api = Build.VERSION_CODES.N)
public void tokenize(Context context, String consumerId, VenmoTokenizationCallback completion) {
    PSVenmoTokenizeOptions tokenizeOptions = new PSVenmoTokenizeOptions(...);

    Continuation<Unit> continuation = getContinuation();

    venmoContext.tokenize(context, tokenizeOptions, new PSVenmoTokenizeCallback() {
        @Override
        public void onSuccess(@NonNull String s) {
            System.out.println("Tokenization finished successfully");
            completion.onTokenized(s);
        }

        @Override
        public void onFailure(@NonNull Exception e) {
            System.out.println("Tokenization onFailure: " + e);
        }
    }, continuation);
}


@RequiresApi(api = Build.VERSION_CODES.N)
@NonNull
private static Continuation<Unit> getContinuation() {
    CompletableFuture<String> future = new CompletableFuture<>();
    return new Continuation<Unit>() {
        @NonNull
        @Override
        public CoroutineContext getContext() {
            return EmptyCoroutineContext.INSTANCE;
        }

        @Override
        public void resumeWith(@NonNull Object o) {
            if (o instanceof Result.Failure) {
                future.completeExceptionally((((Result.Failure) o).exception));
            } else {
                future.complete((String) o);
            }
        }
    };
}
```

The Kotlin coroutines dependency must be added to the Android plugin project:
```gradle
implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3
```






















