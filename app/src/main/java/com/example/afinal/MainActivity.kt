package com.example.afinal

import android.animation.ArgbEvaluator
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.afinal.models.BoardSize
import com.example.afinal.models.DrawGame
import com.github.jinatonic.confetti.CommonConfetti
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

   companion object{
       private const val TAG = "MainActivity"
   }

    private lateinit var drawGame: DrawGame
    private lateinit var clRoot:ConstraintLayout
    private lateinit var rvBoard: RecyclerView
    private lateinit var tvNumMoves: TextView
    private lateinit var tvNumPairs: TextView
    private lateinit var adapter:GameBoardAdapter
    private  var boardSize:BoardSize =BoardSize.EASY

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        clRoot = findViewById(R.id.clRoot)
        rvBoard = findViewById(R.id.rvBoard)
        tvNumMoves = findViewById(R.id.tvNumMoves)
        tvNumPairs = findViewById(R.id.tvNumPairs)

       setupBoard()
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_refresh -> {
                if (drawGame.getNumMoves() > 0 && !drawGame.haveWonGame()) {
                        showAlertDialog("Quit your current game?", null, View.OnClickListener {
                        setupBoard()
                    })
                } else {
                    setupBoard()
                }
                return true
            }
            R.id.mi_new_size -> {
                showNewSizeDialog()
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun showNewSizeDialog() {
        val boardSizeView =LayoutInflater.from(this).inflate(R.layout.dialog_board_size, null)
        val radioGroupSize = boardSizeView.findViewById<RadioGroup>(R.id.radioGroupSize)
        when (boardSize) {
            BoardSize.EASY -> radioGroupSize.check(R.id.rbEasy)
            BoardSize.MEDIUM -> radioGroupSize.check(R.id.rbMedium)
            BoardSize.HARD -> radioGroupSize.check(R.id.rbHard)
        }
        showAlertDialog("Choose new size", boardSizeView, View.OnClickListener {
            boardSize = when (radioGroupSize.checkedRadioButtonId) {
                R.id.rbEasy -> BoardSize.EASY
                R.id.rbMedium -> BoardSize.MEDIUM
                else -> BoardSize.HARD
            }

            setupBoard()
        })
    }

    private fun showAlertDialog(title: String, view: View?, positiveClickListener: View.OnClickListener) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(view)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("OK") { _, _ ->
                positiveClickListener.onClick(null)
            }.show()
    }


    private fun setupBoard() {
        when (boardSize) {
            BoardSize.EASY -> {
                tvNumMoves.text = "Easy: 4x2"
                tvNumPairs.text = "Pairs: 0/4"
            }
            BoardSize.MEDIUM -> {
                tvNumMoves.text = "Medium: 6x3"
                tvNumPairs.text = "Pairs: 0/9"
            }
            BoardSize.HARD -> {
                tvNumMoves.text = "Hard: 6x4"
                tvNumPairs.text = "Pairs: 0/12"
            }
        }
        tvNumPairs.setTextColor(ContextCompat.getColor(this, R.color.color_progress_none))
        drawGame = DrawGame(boardSize)
        adapter = GameBoardAdapter(this,boardSize,drawGame.cards,object : GameBoardAdapter.CardClickListener{
            override fun onCardClicked(position: Int) {
                updateGameWithFlip(position)
            }

        })
        rvBoard.adapter = adapter
        rvBoard.setHasFixedSize(true)
        rvBoard.layoutManager = GridLayoutManager(this,boardSize.getWidth())
    }



    private fun updateGameWithFlip(position: Int) {
        if (drawGame.haveWonGame()){
            Snackbar.make(clRoot,"You already won!",Snackbar.LENGTH_LONG).show()
            return
        }
        if(drawGame.isCardFaceUp(position)){
            Snackbar.make(clRoot,"Invalid move",Snackbar.LENGTH_SHORT).show()
            return
        }
          if( drawGame.flipCard(position)) {
              Log.i(TAG,"Found match! Num pairs found: ${drawGame.numPairsFound}")
              val color = ArgbEvaluator().evaluate(
                  drawGame.numPairsFound.toFloat()/boardSize.getNumPairs(),
                  ContextCompat.getColor(this,R.color.color_progress_none),
                  ContextCompat.getColor(this,R.color.color_progress_full)
              ) as Int
              tvNumPairs.setTextColor(color)
              tvNumPairs.text = "Pairs: ${drawGame.numPairsFound}/ ${boardSize.getNumPairs()}"
              if (drawGame.haveWonGame()){
                  Snackbar.make(clRoot,"You won! Congratulation.",Snackbar.LENGTH_LONG).show()
                  CommonConfetti.rainingConfetti(clRoot, intArrayOf(Color.RED,Color.BLUE,Color.GREEN)).oneShot()
              }
          }
        tvNumMoves.text = "Moves: ${drawGame.getNumMoves()}"
            adapter.notifyDataSetChanged()

    }
}
