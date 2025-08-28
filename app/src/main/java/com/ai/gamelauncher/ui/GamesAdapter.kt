package com.ai.gamelauncher.ui

import android.content.pm.ApplicationInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ai.gamelauncher.R

class GamesAdapter(private val onClick: (ApplicationInfo) -> Unit) :
	ListAdapter<ApplicationInfo, GamesAdapter.GameViewHolder>(Diff) {

	object Diff : DiffUtil.ItemCallback<ApplicationInfo>() {
		override fun areItemsTheSame(oldItem: ApplicationInfo, newItem: ApplicationInfo) =
			oldItem.packageName == newItem.packageName

		override fun areContentsTheSame(oldItem: ApplicationInfo, newItem: ApplicationInfo) =
			oldItem == newItem
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
		val v = LayoutInflater.from(parent.context).inflate(R.layout.item_game, parent, false)
		return GameViewHolder(v, onClick)
	}

	override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
		holder.bind(getItem(position))
	}

	class GameViewHolder(itemView: View, private val onClick: (ApplicationInfo) -> Unit) :
		RecyclerView.ViewHolder(itemView) {
		private val icon: ImageView = itemView.findViewById(R.id.icon)
		private val name: TextView = itemView.findViewById(R.id.name)

		fun bind(app: ApplicationInfo) {
			val pm = itemView.context.packageManager
			icon.setImageDrawable(pm.getApplicationIcon(app))
			name.text = pm.getApplicationLabel(app)
			itemView.setOnClickListener { onClick(app) }
		}
	}
}