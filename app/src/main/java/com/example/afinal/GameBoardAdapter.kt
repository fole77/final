package com.example.afinal

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.afinal.models.BoardSize
import com.example.afinal.models.GameCard
import kotlin.math.min

class GameBoardAdapter(
    private val context: Context,
    private val boardSize: BoardSize,
    private val cards: List<GameCard>,
    private val cardClickListener: CardClickListener
) :
     RecyclerView.Adapter<GameBoardAdapter.ViewHolder>() {

    companion object{
        private const val MARGIN_SIZE = 10
        private const val TAG = "GameBoardAdapter"
    }

    interface CardClickListener {
        fun onCardClicked(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cardWidth = parent.width/boardSize.getWidth() - (2 * MARGIN_SIZE)
        val cardHeight =parent.height/boardSize.getHeight() - (2 * MARGIN_SIZE)
        val cardSideLenth = min(cardWidth,cardHeight)
        val view = LayoutInflater.from(context).inflate(R.layout.game_card,parent,false)
        val layoutParams = view.findViewById<CardView>(R.id.cardView).layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.width = cardSideLenth
        layoutParams.height = cardSideLenth
        layoutParams.setMargins(MARGIN_SIZE, MARGIN_SIZE, MARGIN_SIZE, MARGIN_SIZE)
        return ViewHolder(view)
    }

    override fun getItemCount() = boardSize.numCards

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imageButton = itemView.findViewById<ImageButton>(R.id.imageButton)

        fun bind(position: Int) {
            val gameCard = cards[position]
            imageButton.setImageResource(  if (gameCard.isFaceUp)
                gameCard.identifier else  (R.drawable.zise)
            )

            imageButton.alpha = if (gameCard.isMatched) .4f else 1f
            val colorStateList = if (gameCard.isMatched) ContextCompat.getColorStateList(context, R.color.color_gray) else null
            ViewCompat.setBackgroundTintList(imageButton, colorStateList)
            imageButton.setOnClickListener {
                Log.i(TAG, "Clicked on position $position")
                cardClickListener.onCardClicked(position)
            }
        }

    }
}
