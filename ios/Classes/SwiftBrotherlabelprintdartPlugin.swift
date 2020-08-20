import Flutter
import UIKit

public class SwiftBrotherlabelprintdartPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "brotherql111onwb", binaryMessenger: registrar.messenger())
    let instance = SwiftBrotherlabelprintdartPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    result("iOS " + UIDevice.current.systemVersion)
  }
}
