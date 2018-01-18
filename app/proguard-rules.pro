# 包名不使用大小写混合 aA Aa
-dontusemixedcaseclassnames

# 不混淆第三方引用的库
-dontskipnonpubliclibraryclasses

# 不做预校验
-dontpreverify

# 忽略警告
-ignorewarning
##################输出混淆记录 start###############
# 混淆后生产映射文件 map 类名->转化后类名的映射
# 存放在app\build\outputs\mapping\release中
-verbose

-optimizationpasses 5

#-minifyEnabled true
#-zipAlignEnabled true
# 混淆前后的映射
-printmapping mapping.txt

# apk 包内所有 class 的内部结构
-dump class_files.txt

# 未混淆的类和成员
-printseeds seeds.txt

# 列出从 apk 中删除的代码
-printusage unused.txt

##################输出混淆记录 end###############
##################保留源代码行号 start###############


# 抛出异常时保留代码行号
# 这个最后release的时候关闭掉
-keepattributes SourceFile,LineNumberTable

##################保留源代码行号 end###############

##################基本组件白名单 start###############
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
##################基本组件白名单 end###############

##################Support包规则 start###############
# 如果有引用v4包可以添加下面这行
-keep public class * extends android.support.v4.app.Fragment

# 如果引用了v4或者v7包
-dontwarn android.support.**
##################Support包规则 end###############

##################不混淆本地方法 start###############
# 保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}
##################不混淆本地方法 end###############


##################WebView混淆规则 end###############
# WebView使用javascript功能则需要开启
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
    public *;
}
##################WebView混淆规则 start###############

##################注解、泛型和反射混淆 end###############
# 保护注解
-keepattributes *Annotation*
-keep class * extends java.lang.annotation.Annotation {*;}

# 泛型与反射
-keepattributes Signature
-keepattributes EnclosingMethod
##################注解、泛型和反射混淆 start###############

##################内部类混淆 start###############
# 不混淆内部类
-keepattributes InnerClasses

###################bugly ###################
-keep public class com.tencent.bugly.**{*;}
##################内部类混淆 end###############

-keep  class org.json.**{*;}

##################第三方混淆参考规则 start###############
# gson
-dontwarn com.google.**
-keep class com.google.gson.** {*;}

-keep class sun.misc.Unsafe { *; }
-keep public class * implements java.io.Serializable {*;}

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
#nineoldandroids-library-2.4.0.jar
-dontwarn com.nineoldandroids.*

#######################对反射 的java bean 不做混淆##################################

-keep class com.winguo.search.modle.WordsListBean{*;}### 某个类不混淆
-keep class com.winguo.utils.XmlUtil{*;}### 某个类不混淆
-keep class com.nineoldandroids.** {*;}### 在此包名小的类都不混淆
-keep class com.winguo.bean.** {*;}
-keep class com.winguo.product.modle.bean.** {*;}
-keep class com.winguo.productList.modle.** {*;}
-keep class com.winguo.theme.modle.** {*;}
-keep class com.winguo.lbs.bean.**{*;}

-keep class com.winguo.mine.collect.bean.**{*;}
-keep class com.winguo.cart.bean.**{*;}
-keep class com.winguo.personalcenter.wallet.bean.**{*;}
-keep class com.winguo.pay.modle.bean.**{*;}
-keep class com.winguo.confirmpay.modle.bean.**{*;}
-keep class com.winguo.mine.address.bean.**{*;}
-keep class com.winguo.home.bean.**{*;}
-keep class com.winguo.login.register.bean.**{*;}
-keep class com.winguo.mine.order.bean.**{*;}
-keep class com.winguo.productList.bean.**{*;}

##################Glide 图片加载工具#####################
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
##################Glide 图片加载工具  end #####################


# 友盟统计
-keepclassmembers class * {
    public <init> (org.json.JSONObject);
}

# 友盟统计5.0.0以上SDK需要
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 友盟统计R.java删除问题
-keep public class com.winguo.R$*{
    public static final int *;
}

# OkHttp
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** {*;}
-keep interface com.squareup.okhttp.** {*;}
-dontwarn okio.**

# 支付宝
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{
    public *;
}
-keep class com.alipay.sdk.app.AuthTask{
    public *;
}

# mob 分享
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class **.R$* {*;}
-keep class **.R{*;}
-keep class com.mob.**{*;}

# 百度定位 地图
-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-dontwarn com.baidu.**

-dontwarn com.iflytek.**
-keep class com.iflytek.** {*;}
##################第三方混淆参考规则 end###############
