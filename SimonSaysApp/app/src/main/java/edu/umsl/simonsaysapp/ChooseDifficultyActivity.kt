package edu.umsl.simonsaysapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentActivity

class ChooseDifficultyActivity : AppCompatActivity() {


    companion object {
        // newIntent for MainViewFragment use
        @JvmStatic
        fun newIntent(context: FragmentActivity?): Intent {
            return Intent(context, ChooseDifficultyActivity::class.java)
        }
    }

    private lateinit var chooseDifficultyFragment: ChooseDifficultyFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chooseDifficultyFragment = ChooseDifficultyFragment()

        if (savedInstanceState == null){
            val transaction = this.supportFragmentManager.beginTransaction()
            transaction.add(R.id.fragmentContainer, chooseDifficultyFragment)
            transaction.commit()
        }
    }
}
