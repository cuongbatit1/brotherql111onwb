import 'dart:async';

import 'package:brotherql111onwb/printerModel.dart';
import 'package:brotherql111onwb/templateLabel.dart';
import 'package:flutter/services.dart';

class BrotherQL111ONWBPlugin {
  static const MethodChannel _channel = const MethodChannel('brotherql111onwb');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<String> printLabelFromTemplate(
      String ip, PrinterModel model, List<TemplateLabel> labels) async {
    List<String> data = List<String>();

    for (TemplateLabel label in labels) {
      data += label.toNative();
    }

    return await _channel.invokeMethod('printLabelFromTemplate',
        {"ip": ip, "model": model.index, "data": data});
  }

  static Future<String> printFilePdf(String ip, String file, String labelPrint, bool isOnePage) async {
    return await _channel.invokeMethod('printFilePdf', {"ip": ip, "file": file, "label" : labelPrint, "isOnePage" : isOnePage});
  }
}
