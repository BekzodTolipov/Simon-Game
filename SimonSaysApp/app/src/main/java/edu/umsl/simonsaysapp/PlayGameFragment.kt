package edu.umsl.simonsaysapp

import android.animation.*
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import edu.umsl.simonsaysapp.persistence.SimonDB
import edu.umsl.simonsaysapp.persistence.entities.PlayerEntitiy
import kotlinx.android.synthetic.main.fragment_play_game.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class PlayGameFragment() : Fragment() {

    private lateinit var gamerModel: GameModel
    private lateinit var viewModel: FlashSequenceModel
    private val buttonColors = ArrayList<Button>()
    var totalDuration = 0

    private lateinit var playerName: String
    private var difficultyLevel: Int = 0


    private val timer = object: CountDownTimer(60000, 1000) {
        override fun onFinish() {
            gameOver()
        }

        override fun onTick(millisUntilFinished: Long) {
            timerText?.text = "Timer: " + millisUntilFinished / 1000
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        gamerModel = ModelHolder.instance.get(GameModel::class)!!
        playerName = gamerModel.getPlayerInfo()!!.name
        difficultyLevel = gamerModel.getPlayerInfo()!!.level

        viewModel = ViewModelProvider(this).get(FlashSequenceModel::class.java)

        val view = inflater.inflate(R.layout.fragment_play_game, container, false)

        // Inflate the layout for this fragment
        return view
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retainInstance

        buttonColors.add(yellowBtn)
        buttonColors.add(blueBtn)
        buttonColors.add(greenBtn)
        buttonColors.add(redBtn)

        if (savedInstanceState == null){
            addNewRandomSequence(buttonColors)
        }
        Log.e("TAG-viewModel", viewModel.sequence.toString())
        viewModel.resetTracker()

        // Disable buttons before flashing finishes
        controlButtonsVisibility(yellowBtn, blueBtn, greenBtn, redBtn, false)
        runUIUpdate()
        // Enable buttons back
        controlButtonsVisibility(yellowBtn, blueBtn, greenBtn, redBtn, true)

        yellowBtn.setOnClickListener(selectionListener)

        blueBtn.setOnClickListener(selectionListener)

        greenBtn.setOnClickListener(selectionListener)

        redBtn.setOnClickListener(selectionListener)

        restartBtn.setOnClickListener(gameOverSelection)

        scoreBoard.setOnClickListener(gameOverSelection)

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private var gameOverSelection = View.OnClickListener { v ->
        when(v.id){
            R.id.restartBtn -> {
                restartGame()
            }
            R.id.scoreBoard -> {
                viewModel.clearSeq()
                val intent = GameStatisticsActivity.newIntent(activity)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }
    }

    fun playSoundEffect(btn: Button) {
        val greenSoundURI = Uri.parse("https://s3.amazonaws.com/freecodecamp/simonSound1.mp3")
        val redSoundURI = Uri.parse("https://s3.amazonaws.com/freecodecamp/simonSound2.mp3")
        val blueSoundURI = Uri.parse("https://s3.amazonaws.com/freecodecamp/simonSound3.mp3")
        val yellowSoundURI = Uri.parse("https://s3.amazonaws.com/freecodecamp/simonSound4.mp3")

        val greenSound: MediaPlayer? = MediaPlayer().apply{
            activity?.let { setDataSource(it, greenSoundURI) }
            prepare()
        }
        val redSound: MediaPlayer? = MediaPlayer().apply{
            activity?.let { setDataSource(it, redSoundURI) }
            prepare()
        }
        val blueSound: MediaPlayer? = MediaPlayer().apply{
            activity?.let { setDataSource(it, blueSoundURI) }
            prepare()
        }
        val yellowSound: MediaPlayer? = MediaPlayer().apply{
            activity?.let { setDataSource(it, yellowSoundURI) }
            prepare()
        }

        when(btn){
            redBtn -> {
                redSound?.start()
            }
            greenBtn -> {
                greenSound?.start()
            }
            blueBtn -> {
                blueSound?.start()
            }
            yellowBtn -> {
                yellowSound?.start()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private var selectionListener = View.OnClickListener { v ->

        var validateSeq = true
        when(v.id){
            R.id.redBtn -> {
                addPlayerNewSequence(redBtn)
                playSoundEffect(redBtn)
                validateSeq = checkPlayerChoice()
            }
            R.id.greenBtn -> {
                addPlayerNewSequence(greenBtn)
                playSoundEffect(greenBtn)
                validateSeq = checkPlayerChoice()
            }
            R.id.blueBtn -> {
                addPlayerNewSequence(blueBtn)
                playSoundEffect(blueBtn)
                validateSeq = checkPlayerChoice()
            }
            R.id.yellowBtn -> {
                addPlayerNewSequence(yellowBtn)
                playSoundEffect(yellowBtn)
                validateSeq = checkPlayerChoice()
            }
        }
        if (validateSeq){
            if (viewModel.getPlayerSeq().size === viewModel.getSeq().size){
                gamerModel.setPlayerScore(viewModel.getPlayerSeq().size * 100)
                scoreText.text = "Score: " + gamerModel.getPlayerInfo()?.score


                timer.cancel()
                addNewRandomSequence(buttonColors)
                // Disable buttons before flashing finishes
                controlButtonsVisibility(yellowBtn, blueBtn, greenBtn, redBtn, false)
                runUIUpdate()
                // Enable buttons back
                controlButtonsVisibility(yellowBtn, blueBtn, greenBtn, redBtn, true)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun restartGame() {
        restartBtn?.visibility = View.INVISIBLE
        scoreBoard?.visibility = View.INVISIBLE
        gameOverText?.visibility = View.INVISIBLE
        viewModel.clearSeq()
        viewModel.clearPlayerSeq()
//        addNewRandomSequence(buttonColors)
        totalDuration = 0
        scoreText.text = "Score: 0"
//        titleText.text = "Simon Says"
        addNewRandomSequence(buttonColors)
        // Disable buttons before flashing finishes
        controlButtonsVisibility(yellowBtn, blueBtn, greenBtn, redBtn, false)
        runUIUpdate()
        // Enable buttons back
        controlButtonsVisibility(yellowBtn, blueBtn, greenBtn, redBtn, true)
    }

    fun checkPlayerChoice(): Boolean {
        for (position in 0 until viewModel.getPlayerSeq().size){
            if (viewModel.getPlayerSeq()[position] !== viewModel.getSeq()[position]) {
                gameOver()
                controlButtonsVisibility(yellowBtn, blueBtn, greenBtn, redBtn, false)
                timer.cancel()
                return false
            }
        }
        return true
    }

    fun gameOver() {
        scoreText?.text = "GAME OVER"
        restartBtn?.visibility = View.VISIBLE
        scoreBoard?.visibility = View.VISIBLE
        gameOverText?.visibility = View.VISIBLE

        runBlocking {
            val currentDate: String = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(
                Date()
            )
            val playerData = PlayerEntitiy(gamerModel.getPlayerInfo()?.name!!, gamerModel.getPlayerInfo()?.score!!)
            context?.let {
                SimonDB(it).getPlayerDataDao().addPlayer(playerData)
            }
        }
    }

    fun addPlayerNewSequence(pressedButton: Button) {
        viewModel.addPlayerSeq(pressedButton)
    }

    fun addNewRandomSequence(allBtnColors: ArrayList<Button>) {
        var random = (0..3).random()
        viewModel.addSeq(allBtnColors[random])
    }

    fun controlButtonsVisibility(btn1: Button, btn2: Button, btn3: Button, btn4: Button, value: Boolean) {
        if (!value) {
            btn1.isEnabled = value
            btn2.isEnabled = value
            btn3.isEnabled = value
            btn4.isEnabled = value
        }
        else {
            val handler = Handler()
            val runnable = Runnable {
                btn1.isEnabled = value
                btn2.isEnabled = value
                btn3.isEnabled = value
                btn4.isEnabled = value

                timer.start()
                //You can put anything you want in here that you want to run after delay is over
            }
            handler.postDelayed(runnable, totalDuration.toLong())
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    fun runUIUpdate() {
        var index = 0
        //filter not null activity
        activity?.let {activity ->
            viewModel.clearPlayerSeq()
            Log.e("TAG-PLAYER-SEQ", viewModel.sequenceTracker.toString())
            for (btn in viewModel.sequenceTracker) {
//                Log.e("TAG-INDEX", flashSequence.index.toString())
                val originalColor = btn.background as? ColorDrawable
                val redColor = ContextCompat.getColor(activity, R.color.colorAccent)
                val animator = ValueAnimator.ofObject(
                    ArgbEvaluator(),
                    originalColor?.color,
                    redColor,
                    originalColor?.color
                )

                animator.addUpdateListener { valueAnimator ->
                    (valueAnimator.animatedValue as? Int)?.let { animatedValue ->
                        btn.setBackgroundColor(animatedValue)
                    }
                }

                animator?.startDelay = ((index+1) * (2000/difficultyLevel)).toLong()
                animator?.duration = 2000.toLong()/difficultyLevel
                totalDuration = animator.totalDuration.toInt()
                animator?.start()

                animator.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator?) {
                        super.onAnimationStart(animation)
                        playSoundEffect(btn)

                    }
                    override fun onAnimationEnd(animation: Animator) {
                        // done
                        viewModel.popButton()
                    }
                })
                index++
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clearPlayerSeq()
    }

}
