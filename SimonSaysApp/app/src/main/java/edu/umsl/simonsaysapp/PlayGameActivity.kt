package edu.umsl.simonsaysapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentActivity

class PlayGameActivity : AppCompatActivity() {

    companion object {
        private const val PASSING_PLAYER_NAME = "edu.umsl.bekzod_tolipov.PLAYER_NAME"
        private const val PASSING_GAME_LEVEL = "edu.umsl.bekzod_tolipov.LEVEL"
        // newIntent for MainViewFragment use
        @JvmStatic
        fun newIntent(context: FragmentActivity?, name: String, lvl: Int): Intent {
            val intent = Intent(context, PlayGameActivity::class.java)
            intent.putExtra(PASSING_PLAYER_NAME, name)
            intent.putExtra(PASSING_GAME_LEVEL, lvl)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val playGameFragment = PlayGameFragment()

        if (savedInstanceState == null){
            // Set up new fragment into transaction and start it
            val transaction = this.supportFragmentManager.beginTransaction()
            transaction.add(R.id.fragmentContainer, playGameFragment)
            transaction.commit()
        }
    }
}
