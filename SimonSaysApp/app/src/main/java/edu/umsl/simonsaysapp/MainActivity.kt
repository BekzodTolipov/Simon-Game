package edu.umsl.simonsaysapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var gameModel: GameModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gameModel = ModelHolder.instance.get(GameModel::class) ?: GameModel()
        ModelHolder.instance.set(gameModel)

        val menuFragment = MenuFragment()

        if (savedInstanceState == null){
            // Set up new fragment into transaction and start it
            val transaction = this.supportFragmentManager.beginTransaction()
            transaction.add(R.id.fragmentContainer, menuFragment)
            transaction.commit()
        }
    }
}
