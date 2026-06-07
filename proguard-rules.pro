# Keep Xposed classes
-keep public class de.robv.android.xposed.** { *; }
-keep public class * implements de.robv.android.xposed.IXposedHookLoadPackage

# Keep Gson models
-keep class com.example.xspoof.SpoofConfig { *; }
-keep class com.example.xspoof.** { *; }

# Keep BuildConfig
-keep class **.BuildConfig { *; }

# Keep Compose
-keep public class androidx.compose.** { *; }

# Keep reflection
-keepclasseswithmembernames class * {
    native <methods>;
}
