# Material 3 & Compose rules
-keepattributes *Annotation*
-dontwarn androidx.compose.**

# Kotlinx Serialization
-keepattributes *Annotation*,InnerClasses
-dontnote kotlinx.serialization.SerializationKt
-keepclassmembers class * {
    *** Companion;
}
-keepclasseswithmembers class * {
    kotlinx.serialization.KSerializer serializer(...);
}

# Hilt & Dagger
-dontwarn dagger.hilt.**
-keep class * extends dagger.hilt.android.HiltAndroidApp
