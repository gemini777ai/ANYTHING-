package com.ai.gamelauncher.ui

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ai.gamelauncher.R
import com.ai.gamelauncher.service.BoosterService
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : ComponentActivity() {
	private val viewModel: MainViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		findViewById<MaterialToolbar>(R.id.toolbar)

		val recycler = findViewById<RecyclerView>(R.id.recyclerGames)
		recycler.layoutManager = LinearLayoutManager(this)
		val adapter = GamesAdapter { appInfo ->
			startPreOptimizedGame(appInfo)
		}
		recycler.adapter = adapter

		adapter.submitList(GameDetector.detectInstalledGames(this))

		findViewById<FloatingActionButton>(R.id.fabBoost).setOnClickListener {
			startForegroundBooster()
		}

		ensureUsageStatsPermission()
	}

	private fun startForegroundBooster() {
		val intent = Intent(this, BoosterService::class.java)
		intent.action = BoosterService.ACTION_ONE_TAP_BOOST
		if (Build.VERSION.SDK_INT >= 26) {
			startForegroundService(intent)
		} else {
			startService(intent)
		}
	}

	private fun startPreOptimizedGame(appInfo: ApplicationInfo) {
		val intent = Intent(this, BoosterService::class.java)
		intent.action = BoosterService.ACTION_PREPARE_AND_LAUNCH
		intent.putExtra(BoosterService.EXTRA_PACKAGE_NAME, appInfo.packageName)
		if (Build.VERSION.SDK_INT >= 26) {
			startForegroundService(intent)
		} else {
			startService(intent)
		}
	}

	private fun ensureUsageStatsPermission() {
		if (!hasUsageStatsPermission(this)) {
			val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
			intent.data = Uri.parse("package:$packageName")
			startActivity(intent)
		}
	}

	private fun hasUsageStatsPermission(context: Context): Boolean {
		val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
		val mode = appOps.unsafeCheckOpNoThrow(
			"android:get_usage_stats", android.os.Process.myUid(), context.packageName
		)
		return mode == AppOpsManager.MODE_ALLOWED
	}
}