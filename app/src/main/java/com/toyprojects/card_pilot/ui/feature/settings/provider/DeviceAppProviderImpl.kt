package com.toyprojects.card_pilot.ui.feature.settings.provider

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import com.toyprojects.card_pilot.ui.feature.settings.model.CardCompanyApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeviceAppProviderImpl(private val context: Context) : DeviceAppProvider {
    private val packageManager: PackageManager = context.packageManager

    override suspend fun getInstalledCardApps(customApps: Set<String>): List<CardCompanyApp> =
        withContext(Dispatchers.IO) {
            CardCompanyApp.KNOWN_PACKAGES.mapNotNull { knownApp ->
                if (isAppInstalled(knownApp.packageName)) {
                    val icon = loadAppIcon(knownApp.packageName)
                    knownApp.copy(icon = icon)
                } else {
                    null
                }
            } + customApps.mapNotNull { packageName ->
                if (isAppInstalled(packageName)) {
                    val appName = loadAppName(packageName) ?: packageName
                    val icon = loadAppIcon(packageName)
                    CardCompanyApp(appName, packageName, icon)
                } else {
                    null
                }
            }.distinctBy { it.packageName }
        }

    override suspend fun getAllInstalledApps(excludePackages: Set<String>): List<CardCompanyApp> =
        withContext(Dispatchers.IO) {
            val intent = Intent(Intent.ACTION_MAIN, null).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
            }
            val resolveInfoList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.queryIntentActivities(intent, PackageManager.ResolveInfoFlags.of(0L))
            } else {
                @Suppress("DEPRECATION")
                packageManager.queryIntentActivities(intent, 0)
            }

            val ownPackageName = context.packageName
            val fullExclude = excludePackages + ownPackageName

            resolveInfoList.mapNotNull { resolveInfo ->
                val packageName = resolveInfo.activityInfo.packageName
                if (fullExclude.contains(packageName)) {
                    return@mapNotNull null
                }
                val appName = loadAppName(packageName) ?: packageName
                val icon = loadAppIcon(packageName)
                CardCompanyApp(appName, packageName, icon)
            }.distinctBy { it.packageName }.sortedBy { it.displayName }
        }

    override suspend fun getAppName(packageName: String): String =
        withContext(Dispatchers.IO) {
            loadAppName(packageName) ?: packageName
        }

    private fun loadAppName(packageName: String): String? {
        return try {
            val appInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.getApplicationInfo(packageName, PackageManager.ApplicationInfoFlags.of(0L))
            } else {
                @Suppress("DEPRECATION")
                packageManager.getApplicationInfo(packageName, 0)
            }
            packageManager.getApplicationLabel(appInfo).toString()
        } catch (_: PackageManager.NameNotFoundException) {
            null
        }
    }

    private fun isAppInstalled(packageName: String): Boolean {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0L))
            } else {
                @Suppress("DEPRECATION")
                packageManager.getPackageInfo(packageName, 0)
            }
            true
        } catch (_: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun loadAppIcon(packageName: String): ImageBitmap? {
        return try {
            val drawable = packageManager.getApplicationIcon(packageName)
            drawable.toBitmap().asImageBitmap()
        } catch (_: Exception) {
            null
        }
    }
}
