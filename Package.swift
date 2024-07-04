// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "TestPlugin",
    platforms: [.iOS(.v13)],
    products: [
        .library(
            name: "TestPlugin",
            targets: ["PaysafePluginPlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", branch: "main")
    ],
    targets: [
        .target(
            name: "PaysafePluginPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/PaysafePluginPlugin"),
        .testTarget(
            name: "PaysafePluginPluginTests",
            dependencies: ["PaysafePluginPlugin"],
            path: "ios/Tests/PaysafePluginPluginTests")
    ]
)