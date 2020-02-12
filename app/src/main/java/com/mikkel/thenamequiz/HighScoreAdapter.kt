package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Entity.ScoreEntity
import kotlinx.android.synthetic.main.highscore_row.view.*

class HighScoreAdapter(context: Context) : RecyclerView.Adapter<HighScoreAdapter.ViewHolder>() {

    private var scores = emptyList<ScoreEntity>()
    private val inflater = LayoutInflater.from(context)

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val row = inflater.inflate(R.layout.highscore_row, parent, false)
        return ViewHolder(row)
    }

    override fun getItemCount(): Int {
        return scores.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val positionPlussOne = "${position + 1}"
        holder.view.scoreNum.text = positionPlussOne
        holder.view.scoreName.text = scores[position].name
        val pointText = "${scores[position].score} points"
        holder.view.scoreScore.text = pointText
    }

    internal fun setScores(scores: List<ScoreEntity>) {
        val sortedList = scores.sortedByDescending { it.score }
        if (sortedList.size > 10) {
            this.scores = sortedList.subList(0, 10)
        } else {
            this.scores = sortedList
        }
        notifyDataSetChanged()
    }
}