package ua.alxmute.migratemusic.adapter

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ua.alxmute.migratemusic.R
import ua.alxmute.migratemusic.data.ProcessedTrackDto

class TrackRecyclerViewAdapter : RecyclerView.Adapter<TrackRecyclerViewAdapter.TrackViewHolder>() {

    lateinit var tracks: List<ProcessedTrackDto>
    lateinit var context: Context


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TrackViewHolder {
        val inflater = LayoutInflater.from(context)
        return TrackViewHolder(inflater.inflate(R.layout.tracks_item_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
    }


    class TrackViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvTrackName = view.findViewById<TextView>(R.id.tvTrackName)

        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        fun bind(track: ProcessedTrackDto) {
            tvTrackName.text = "${track.author} - ${track.title}"
            if (track.isFailed) {
                tvTrackName.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.failure,
                    0
                )
                tvTrackName.tag = "Failure"
                println("F")
            }
        }
    }
}