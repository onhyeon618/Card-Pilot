# ProGuard / R8 rules for CardPilot

# Preserve source file and line numbers for crash reporting
-keepattributes SourceFile,LineNumberTable
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod

# Firebase & Crashlytics Components
-keep class com.google.firebase.crashlytics.** { *; }
-dontwarn com.google.firebase.crashlytics.**
-keep class com.google.firebase.components.** { *; }
-keep class * implements com.google.firebase.components.ComponentRegistrar { *; }

# Google API Client & Google Drive
-keep class com.google.api.services.drive.** { *; }
-keep interface com.google.api.services.drive.** { *; }
-keep class com.google.api.client.** { *; }
-keep interface com.google.api.client.** { *; }
-keepclassmembers class * {
    @com.google.api.client.util.Key <fields>;
}
-keep class * extends com.google.api.client.json.GenericJson { *; }
-keep class * extends com.google.api.client.util.GenericData { *; }
-dontwarn com.google.api.client.**
-dontwarn com.google.api.services.drive.**
-dontwarn com.google.common.**
-dontwarn org.apache.http.**

# Navigation Type-Safe routes & Kotlinx Serialization
-keep class com.toyprojects.card_pilot.ui.Screen** { *; }
-keepclassmembers class * {
    *** Companion;
}
-keepclasseswithmembers class * {
    kotlinx.serialization.KSerializer serializer(...);
}

# Room Database & DAOs
-keep class * extends androidx.room.RoomDatabase {
    <init>();
}
-keep class * extends androidx.room.RoomDatabase
-keep class com.toyprojects.card_pilot.data.local.dao.** { *; }
-keep class * implements com.toyprojects.card_pilot.data.local.dao.** { *; }
-dontwarn androidx.room.paging.**

# Data & Entity models (prevent field stripping for Room, DataStore & serialization)
-keep class com.toyprojects.card_pilot.data.local.entity.** { *; }
-keep class com.toyprojects.card_pilot.model.** { *; }
-keepclassmembers enum com.toyprojects.card_pilot.model.** { *; }
-keep class com.toyprojects.card_pilot.domain.model.** { *; }

# AboutLibraries
-keep class com.mikepenz.aboutlibraries.** { *; }