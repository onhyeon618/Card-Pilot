# ProGuard / R8 rules for CardPilot

# Preserve source file and line numbers for crash reporting
-keepattributes SourceFile,LineNumberTable
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod

# Google API Client & Google Drive
-keepclassmembers class * {
    @com.google.api.client.util.Key <fields>;
}
-dontwarn com.google.api.client.**
-dontwarn com.google.common.**
-dontwarn org.apache.http.**

# Kotlinx Serialization
-keepclassmembers class * {
    *** Companion;
}
-keepclasseswithmembers class * {
    kotlinx.serialization.KSerializer serializer(...);
}

# Room
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**

# Data & Entity models (prevent field stripping for Room & serialization)
-keep class com.toyprojects.card_pilot.data.local.entity.** { *; }
-keep class com.toyprojects.card_pilot.data.model.** { *; }
-keep class com.toyprojects.card_pilot.domain.model.** { *; }

# AboutLibraries
-keep class com.mikepenz.aboutlibraries.** { *; }