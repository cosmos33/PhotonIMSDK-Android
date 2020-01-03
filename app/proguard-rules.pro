# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
#Photon Push SDK 公共
-keep class com.cosmos.photon.push.**{*;}
-keep class com.cosmos.photon.baseim.**{*;}
-keep interface com.cosmos.photon.push.**{*;}
-keep class com.immomo.momo.util.jni.Coded {*;}

-keepclasseswithmembernames class * {
  native <methods>;
}
-keep class com.mm.mmfile.core.**{*;}
-keep class com.cosmos.mdlog.**{*;}

#OPPO
-keep public class * extends android.app.Service
#VIVO
-dontwarn com.vivo.push.**
-keep class com.vivo.push.**{*; }
-keep class com.vivo.vms.**{*; }
-keep class xxx.xxx.xxx.PushMessageReceiverImpl{*;}
#小米
-keep class com.cosmos.photon.thirdparty.mi.MiMessageReceiver {*;}
-dontwarn com.xiaomi.push.**
#华为
-keepattributes *Annotation*
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
-keep class com.hianalytics.android.**{*;}
-keep class com.huawei.updatesdk.**{*;}
-keep class com.huawei.hms.**{*;}
-keep class com.huawei.android.hms.agent.**{*;}
-keep class com.huawei.gamebox.plugin.gameservice.**{*;}
#魅族
-keep class com.meizu.cloud.pushsdk.** { *; }
-dontwarn  com.meizu.cloud.pushsdk.**
-keep class com.meizu.nebula.** { *; }
-dontwarn com.meizu.nebula.**
-keep class com.meizu.push.** { *; }
-dontwarn com.meizu.push.**