package edu.umsl.simonsaysapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class MenuFragment : Fragment() {

    private lateinit var gameModel: GameModel

    companion object {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        gameModel = ModelHolder.instance.get(GameModel::class)!!

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val startButton = view.findViewById<Button>(R.id.startGameBtn)

        startButton?.setOnClickListener {

            val intent = ChooseDifficultyActivity.newIntent(activity)
            startActivity(intent)
        }

    }


}
