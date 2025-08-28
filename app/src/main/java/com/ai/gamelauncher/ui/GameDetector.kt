package com.ai.gamelauncher.ui

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager

object GameDetector {
	private val commonGameHints = listOf("com.tencent", "com.activision", "com.garena", "mihoyo", "hoYoverse")

	fun detectInstalledGames(context: Context): List<ApplicationInfo> {
		val pm = context.packageManager
		val apps = pm.getInstalledApplications(PackageManager.GET_META_DATA)
		val result = mutableListOf<ApplicationInfo>()
		for (app in apps) {
			if (!isUserApp(app)) continue
			val isCategoryGame = pm.getLaunchIntentForPackage(app.packageName)?.categories?.contains(Intent.CATEGORY_GAME) == true
			val hint = commonGameHints.any { app.packageName.contains(it, ignoreCase = true) }
			if (isCategoryGame || hint) {
				result.add(app)
			}
		}
		return result.sortedBy { it.loadLabel(pm).toString() }
	}

	private fun isUserApp(app: ApplicationInfo): Boolean {
		val mask = ApplicationInfo.FLAG_SYSTEM or ApplicationInfo.FLAG_UPDATED_SYSTEM_APP
		return (app.flags and mask) == 0
	}
}