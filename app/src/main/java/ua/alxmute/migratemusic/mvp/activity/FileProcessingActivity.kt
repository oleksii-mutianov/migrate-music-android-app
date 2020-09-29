package ua.alxmute.migratemusic.mvp.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_file_processing.*
import ua.alxmute.migratemusic.R
import ua.alxmute.migratemusic.adapter.TrackRecyclerViewAdapter
import ua.alxmute.migratemusic.data.LocalTrackDto
import ua.alxmute.migratemusic.mvp.presenter.FileProcessingPresenter
import ua.alxmute.migratemusic.mvp.view.FileProcessingView
import kotlin.concurrent.thread

class FileProcessingActivity : AppCompatActivity(), FileProcessingView {

    override val processedTracks = mutableListOf<LocalTrackDto>()

    private val trackRecyclerViewAdapter: TrackRecyclerViewAdapter = TrackRecyclerViewAdapter(processedTracks, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_processing)

        rcProcessedTracks.adapter = trackRecyclerViewAdapter
        rcProcessedTracks.layoutManager = LinearLayoutManager(this)

        FileProcessingPresenter.view = this

        thread {
            FileProcessingPresenter.onLoad()
        }
    }

    override fun refreshList() {
        runOnUiThread {
            trackRecyclerViewAdapter.notifyDataSetChanged()
        }
    }

    override fun setTrackCounter(text: String) {
        runOnUiThread {
            trackCounter.text = text
        }
    }

    override fun getActivity() = this

}