package edu.umsl.simonsaysapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_choose_difficulty.*

class ChooseDifficultyFragment() : Fragment() {

    private lateinit var receivedName: String
    private val EASY_LVL = 1
    private val MEDIUM_LVL = 2
    private val HARD_LVL = 3
    private lateinit var gameModel: GameModel

    companion object {
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        gameModel = ModelHolder.instance.get(GameModel::class) ?: GameModel()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_difficulty, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name = view.findViewById<EditText>(R.id._nameHolder)


        submitBtn.setOnClickListener {
            val playerName = name.text.toString()
            if (playerName.length === 0){
                Toast.makeText(this@ChooseDifficultyFragment.context, "Please enter your name", Toast.LENGTH_LONG).show()
            } else {
                this@ChooseDifficultyFragment.receivedName = playerName
                easyBtn.visibility = View.VISIBLE
                mediumBtn.visibility = View.VISIBLE
                hardBtn.visibility = View.VISIBLE
                submitBtn.visibility = View.INVISIBLE
            }
        }

        easyBtn?.setOnClickListener {
            gameModel.addNewPlayer(receivedName, EASY_LVL)
            ModelHolder.instance.set(gameModel)
            val intent = PlayGameActivity.newIntent(activity, receivedName, EASY_LVL)
            startActivity(intent)
        }

        mediumBtn?.setOnClickListener {
            gameModel.addNewPlayer(receivedName, MEDIUM_LVL)
            ModelHolder.instance.set(gameModel)
            val intent = PlayGameActivity.newIntent(activity, receivedName, MEDIUM_LVL)
            startActivity(intent)
        }

        hardBtn?.setOnClickListener {
            gameModel.addNewPlayer(receivedName, HARD_LVL)
            ModelHolder.instance.set(gameModel)
            val intent = PlayGameActivity.newIntent(activity, receivedName, HARD_LVL)
            startActivity(intent)
        }
    }
}
