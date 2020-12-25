package com.example.afinal

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import java.util.Collections.min
import kotlin.math.min

class GameBoardAdapter(private val context: Context, private val NumPairs: Int) :
    RecyclerView.Adapter<GameBoardAdapter.ViewHolder>() {

    companion object{
        private const val MARGIN_SIZE = 10
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cardWidth = parent.width/2 - (2 * MARGIN_SIZE)
        val cardHeight =parent.height/4 - (2 * MARGIN_SIZE)
        val cardSideLenth = min(cardWidth,cardHeight)
        val view = LayoutInflater.from(context).inflate(R.layout.game_card,parent,false)
        val layoutParams = view.findViewById<CardView>(R.id.cardView).layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.width = cardSideLenth
        layoutParams.height = cardSideLenth
        layoutParams.setMargins(MARGIN_SIZE, MARGIN_SIZE, MARGIN_SIZE, MARGIN_SIZE)
        return ViewHolder(view)
    }

    override fun getItemCount() = NumPairs

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
              // No-op
        }
    }
}
