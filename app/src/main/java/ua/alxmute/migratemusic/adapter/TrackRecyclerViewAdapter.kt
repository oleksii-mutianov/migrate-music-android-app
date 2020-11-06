package ua.alxmute.migratemusic.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ua.alxmute.migratemusic.R
import ua.alxmute.migratemusic.data.LocalTrackDto

class TrackRecyclerViewAdapter(
    private val tracks: List<LocalTrackDto>,
    private val context: Context
) : RecyclerView.Adapter<TrackRecyclerViewAdapter.TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val inflater = LayoutInflater.from(context)
        return TrackViewHolder(inflater.inflate(R.layout.tracks_item_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

    class TrackViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvTrackName = view.findViewById<TextView>(R.id.tvTrackName)

        fun bind(track: LocalTrackDto) {
            tvTrackName.text = "${track.author} - ${track.title}"
        }
    }
}