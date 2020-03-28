package edu.umsl.simonsaysapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.activity_dashboard.*

class GameStatisticsActivity : AppCompatActivity() {

    companion object {
        // newIntent for MainViewFragment use
        @JvmStatic
        fun newIntent(context: FragmentActivity?): Intent {
            return Intent(context, GameStatisticsActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        startNewGame.setOnClickListener {
            val intent = ChooseDifficultyActivity.newIntent(this)
            startActivity(intent)
        }
        val gameStatsFragment = GameStatisticsFragment()

        if (savedInstanceState == null){
            // Set up new fragment into transaction and start it
            val transaction = this.supportFragmentManager.beginTransaction()
            transaction.add(R.id.dashFragmentContainer, gameStatsFragment)
            transaction.commit()
        }
    }
}
