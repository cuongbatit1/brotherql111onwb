package com.king.studio.brotherql111onwb

import android.os.Build
import android.util.Log
import androidx.annotation.NonNull
import com.brother.ptouch.sdk.LabelInfo
import com.brother.ptouch.sdk.Printer
import com.brother.ptouch.sdk.PrinterInfo
import com.brother.ptouch.sdk.PrinterInfo.ErrorCode
import com.brother.ptouch.sdk.PrinterStatus
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import kotlinx.coroutines.*

/** BrotherQL111ONWBPlugin */
public class BrotherQL111ONWBPlugin: FlutterPlugin, MethodCallHandler {
  // https://proandroiddev.com/android-coroutine-recipes-33467a4302e9
  private val uiScope = CoroutineScope(Dispatchers.Main)
  private val uiDispatcher: CoroutineDispatcher = Dispatchers.Main
  private val bgDispatcher: CoroutineDispatcher = Dispatchers.IO

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    val channel = MethodChannel(flutterPluginBinding.flutterEngine.dartExecutor, "brotherql111onwb")
    channel.setMethodCallHandler(BrotherQL111ONWBPlugin());
  }

  companion object {
    @JvmStatic
    fun registerWith(registrar: Registrar) {
      val channel = MethodChannel(registrar.messenger(), "brotherql111onwb")
      channel.setMethodCallHandler(BrotherQL111ONWBPlugin())
    }
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    when(call.method) {
      "getPlatformVersion" -> result.success("Android ${android.os.Build.VERSION.RELEASE}")
      "printLabelFromTemplate" -> {
        Log.d("printLabelFromTemplateT", "printLabelFromTemplate")
        uiScope.launch {
          val task = async(bgDispatcher) {
            // background thread
            val dataResult = printLabelFromTemplate(
                    call.argument<String>("ip").orEmpty(),
                    call.argument<Int>("model")!!,
                    call.argument<List<String>>("data").orEmpty()
            )

            return@async dataResult
          }
          val resultTask = task.await()
//          delay(2000)
          Log.d("printLabelFromTemplateT", "delay $resultTask")
          result.success(resultTask)
        }

      }
      "printFilePdf" -> {
        val nameFile: String = call.argument<String>("file")!!
        val ip: String = call.argument<String>("ip")!!
        val isOnePage: Boolean = call.argument<Boolean>("isOnePage") ?: false
        val label: String? = call.argument<String>("label")
        Log.d("printFilePdf", "printFilePdf : $nameFile - $ip - $isOnePage")
        printPdf(ip, nameFile, label, isOnePage, result)
      }
      else -> result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {

  }

  private fun printPdf(printerIp: String, file: String, labelPrint: String?, isOnePage : Boolean , @NonNull result: Result) {
    uiScope.launch {
      val task = async(bgDispatcher) {
        // background thread
        try {
          var mPrintResult: PrinterStatus? = null
          val printer = Printer()

          val info = PrinterInfo()

          info.ipAddress = printerIp
          info.printerModel = PrinterInfo.Model.QL_1110NWB
          info.port = PrinterInfo.Port.NET
          info.localName = PrinterInfo.Model.QL_1110NWB.name
          if (labelPrint == LabelQL1100.W102H152) {
            info.labelNameIndex = LabelInfo.QL1100.W102H152.ordinal
          } else if (labelPrint == LabelQL1100.W29H90) {
            info.labelNameIndex = LabelInfo.QL1100.W29H90.ordinal
          } else if (labelPrint == LabelQL1100.W38H90) {
            info.labelNameIndex = LabelInfo.QL1100.W38H90.ordinal
          } else if (labelPrint == LabelQL1100.W62H29) {
            info.labelNameIndex = LabelInfo.QL1100.W62H29.ordinal
          } else if (labelPrint == LabelQL1100.W62H100) {
            info.labelNameIndex = LabelInfo.QL1100.W62H100.ordinal
          } else if (labelPrint == LabelQL1100.W17H54) {
            info.labelNameIndex = LabelInfo.QL1100.W17H54.ordinal
          } else if (labelPrint == LabelQL1100.W17H87) {
            info.labelNameIndex = LabelInfo.QL1100.W17H87.ordinal
          } else if (labelPrint == LabelQL1100.W29H42) {
            info.labelNameIndex = LabelInfo.QL1100.W29H42.ordinal
          } else if (labelPrint == LabelQL1100.W52H29) {
            info.labelNameIndex = LabelInfo.QL1100.W52H29.ordinal
          } else if (labelPrint == LabelQL1100.W23H23) {
            info.labelNameIndex = LabelInfo.QL1100.W23H23.ordinal
          } else if (labelPrint == LabelQL1100.W39H48) {
            info.labelNameIndex = LabelInfo.QL1100.W39H48.ordinal
          } else if (labelPrint == LabelQL1100.W60H86) {
            info.labelNameIndex = LabelInfo.QL1100.W60H86.ordinal
          } else if (labelPrint == LabelQL1100.W103H164) {
            info.labelNameIndex = LabelInfo.QL1100.W103H164.ordinal
          } else if (labelPrint == LabelQL1100.W102H51) {
            info.labelNameIndex = LabelInfo.QL1100.W102H51.ordinal
          } else if (labelPrint == LabelQL1100.W12) {
            info.labelNameIndex = LabelInfo.QL1100.W12.ordinal
          } else if (labelPrint == LabelQL1100.W24) {
            info.labelNameIndex = LabelInfo.PT.W24.ordinal
          } else {
            info.labelNameIndex = LabelInfo.QL1100.W62.ordinal
          }
          info.skipStatusCheck = false
          info.isAutoCut = false
          info.isCutAtEnd = false
          info.checkPrintEnd = PrinterInfo.CheckPrintEnd.CPE_NO_CHECK
          info.rawMode = false
          info.enabledTethering = false
          info.softFocusing = false
          info.trimTapeAfterData = false
          info.printQuality = PrinterInfo.PrintQuality.NORMAL
          info.thresholdingValue = 127
          info.overwrite = false
//          info.savePrnPath = input
//          info.workPath = sharedPreferences.getString("workPath", "")
          printer.printerInfo = info
          var countPage = if (isOnePage) {
              1
          } else {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
              printer.getPDFPages(file)
            } else {
              printer.getPDFFilePages(file)
            }
          }

          Log.d("printFilePdf", "countPage : $countPage")
          for (index in 1 .. countPage) {
            Log.d("printFilePdf", "index : $index")
            mPrintResult = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
              printer.printPDF(file, index)
            } else {
              printer.printPdfFile(file, index)
            }
            if (mPrintResult.errorCode !== ErrorCode.ERROR_NONE) {
              break
            }
          }
          if (mPrintResult == null) {
            return@async "Cannot Print"
          } else {
            return@async mPrintResult
          }
        } catch (e: Exception) {
          return@async e.message
        }

      }
      val resultTask = task.await()

      if (resultTask is PrinterStatus) {
        Log.d("printLabelFromTemplateT", "delay ${resultTask.errorCode}")
        val textMessage = if (resultTask.errorCode == ErrorCode.ERROR_NONE) {
          resultTask.errorCode.toString()
        } else {
          resultTask.errorCode.toString()
        }
        result.success(textMessage)
      } else {
        result.success(resultTask)
      }
    }
  }


  private suspend fun printLabelFromTemplate(printerIp: String, printerModel: Int, data: List<String>) : String {
    val printer = Printer()

    val info = PrinterInfo()

    info.ipAddress = printerIp
    info.printerModel = PrinterInfo.Model.valueFromID(printerModel)
    info.port = PrinterInfo.Port.NET
    printer.printerInfo = info

//    val thread = Thread(Runnable {
//      try {
//        Log.d("BROTHER LABEL PRINT", "Thread started")
//
//        var result = PrinterStatus()
//
//        for(d in data) {
//          var tmp = d.split("||")
//          when(tmp[0]) {
//            "START" -> printer.startPTTPrint(tmp[1].toInt(), null)
//            "TEXT" -> printer.replaceText(tmp[1])
//            "END" -> result = printer.flushPTTPrint()
//          }
//        }
//
//        if(result.errorCode != PrinterInfo.ErrorCode.ERROR_NONE) {
//          Log.e("BROTHER PRINTER ERROR", result.errorCode.toString())
//        }
//      } catch (e: Exception) {
//        e.printStackTrace()
//      }
//      Log.d("BROTHER LABEL PRINT", "Print finished")
//    })
//
//    thread.start()

    try {
      Log.d("BROTHER LABEL PRINT", "Thread started")

      var result = PrinterStatus()

      for(d in data) {
        var tmp = d.split("||")
        when(tmp[0]) {
          "START" -> printer.startPTTPrint(tmp[1].toInt(), null)
          "TEXT" -> printer.replaceText(tmp[1])
          "END" -> result = printer.flushPTTPrint()
        }
      }

      if(result.errorCode != PrinterInfo.ErrorCode.ERROR_NONE) {
        Log.e("BROTHER PRINTER ERROR", result.errorCode.toString())
      }
    } catch (e: Exception) {
      e.printStackTrace()
    } finally {
      Log.d("BROTHER LABEL PRINT", "Print finally finally")

    }
    Log.d("BROTHER LABEL PRINT", "Print finished")

    Log.d("BROTHER LABEL PRINT", "Printing on : $printerIp")

    return "xxxx Finished"
  }
}
