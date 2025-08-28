package com.ai.gamelauncher.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.ai.gamelauncher.R
import com.ai.gamelauncher.ui.MainActivity
import com.ai.gamelauncher.ai.AiEngine
import com.ai.gamelauncher.ai.HeuristicAiEngine

class BoosterService : Service() {
	companion object {
		const val CHANNEL_ID = "booster_service"
		const val NOTIF_ID = 101
		const val ACTION_ONE_TAP_BOOST = "one_tap_boost"
		const val ACTION_PREPARE_AND_LAUNCH = "prepare_and_launch"
		const val EXTRA_PACKAGE_NAME = "pkg"
	}

	private val aiEngine: AiEngine by lazy { HeuristicAiEngine(this) }

	override fun onCreate() {
		super.onCreate()
		createChannel()
	}

	override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
		startForeground(NOTIF_ID, buildNotification("Idle"))
		when (intent?.action) {
			ACTION_ONE_TAP_BOOST -> {
				performBoost()
			}
			ACTION_PREPARE_AND_LAUNCH -> {
				val pkg = intent.getStringExtra(EXTRA_PACKAGE_NAME)
				if (pkg != null) {
					performBoost()
					launchPackage(pkg)
				}
			}
		}
		return START_STICKY
	}

	private fun performBoost() {
		aiEngine.onPreBoost()
		aiEngine.trimMemory()
		aiEngine.adjustThermalAndCpuPolicy()
	}

	private fun launchPackage(pkg: String) {
		val pm = packageManager
		val i = pm.getLaunchIntentForPackage(pkg)
		if (i != null) {
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
			startActivity(i)
			updateNotification("Launching ${'$'}pkg")
		}
	}

	private fun createChannel() {
		if (Build.VERSION.SDK_INT >= 26) {
			val chan = NotificationChannel(CHANNEL_ID, "AI Booster", NotificationManager.IMPORTANCE_LOW)
			chan.lightColor = Color.CYAN
			chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
			val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
			nm.createNotificationChannel(chan)
		}
	}

	private fun buildNotification(status: String): Notification {
		val pendingIntent = PendingIntent.getActivity(
			this, 0, Intent(this, MainActivity::class.java), PendingIntent.FLAG_IMMUTABLE
		)
		return NotificationCompat.Builder(this, CHANNEL_ID)
			.setSmallIcon(android.R.drawable.stat_sys_download_done)
			.setContentTitle("AI Game Launcher")
			.setContentText(status)
			.setOngoing(true)
			.setContentIntent(pendingIntent)
			.build()
	}

	private fun updateNotification(status: String) {
		val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
		nm.notify(NOTIF_ID, buildNotification(status))
	}

	override fun onBind(intent: Intent?): IBinder? = null
}