package edu.umsl.simonsaysapp

import android.widget.Button
import androidx.lifecycle.ViewModel

class FlashSequenceModel: ViewModel() {

    var sequence: ArrayList<Button> = ArrayList()
        private set

    var playerSequence: ArrayList<Button> = ArrayList()
        private set

    var sequenceTracker: ArrayList<Button> = ArrayList()
        private set

    fun addSeq(btn: Button){
        sequence.add(btn)
        resetTracker()
    }

    fun addPlayerSeq(btn: Button) {
        playerSequence.add(btn)
    }

    fun getSeq(): ArrayList<Button> {
        return sequence
    }

    fun getPlayerSeq(): ArrayList<Button> {
        return playerSequence
    }

    fun popButton() {
        if (sequenceTracker.isNotEmpty()){
            sequenceTracker.removeAt(0)
        }
    }

    fun clearSeq() {
        sequence.clear()
    }

    fun clearPlayerSeq(){
        playerSequence.clear()
    }

    fun resetTracker(){
        sequenceTracker.clear()
        sequenceTracker.addAll(sequence.filterNotNull())
    }
}