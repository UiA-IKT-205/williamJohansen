package no.uia.ikt205.pomodoro

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import no.uia.ikt205.pomodoro.util.millisecondsToDescriptiveTime

class MainActivity : AppCompatActivity() {

    lateinit var timer:CountDownTimer

    lateinit var seekBar:SeekBar
    lateinit var pauseSeekBar:SeekBar

    lateinit var repInput:EditText

    lateinit var startButton:Button
    lateinit var stopButton:Button
    lateinit var coutdownDisplay:TextView
    lateinit var pauseTimeDisplay:TextView

    var timeToCountDownInMs = 60L * 60L * 1000L
    var repTime = timeToCountDownInMs
    var pauseTimeInMs = 15L * 60L * 1000L
    val timeTicks = 1000L
    var reps:Int = 0

    var isRunning = false
    var isPause = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seekBar = findViewById<SeekBar>(R.id.seekBar)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(x:SeekBar, y:Int, z:Boolean) {
                if(!isRunning){
                    repTime = x.progress * 15L * 60L * 1000L // 1 Min for debugging
                    timeToCountDownInMs = repTime
                    updateCountDownDisplay(timeToCountDownInMs)
                }else{
                    x.progress = (repTime / 60 / 1000).toInt()
                }

            }

            override fun onStartTrackingTouch(x: SeekBar) {}
            override fun onStopTrackingTouch(x: SeekBar) {}
        })

        pauseSeekBar = findViewById<SeekBar>(R.id.pauseSeekBar)
        pauseSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(x:SeekBar, y:Int, z:Boolean) {
                pauseTimeInMs = x.progress * 5L * 60L * 1000L
                pauseTimeDisplay.text = millisecondsToDescriptiveTime(pauseTimeInMs)
            }

            override fun onStartTrackingTouch(x: SeekBar) {}
            override fun onStopTrackingTouch(x: SeekBar) {}
        })

        repInput = findViewById<EditText>(R.id.editTextNumber)

        startButton = findViewById<Button>(R.id.startCountdownButton)
        startButton.setOnClickListener(){
            if (isRunning){
                println("Already running")
            }else {
                if(isPause){
                    startPause(it)
                    isRunning = true
                }else{
                    startCountDown(it)
                    isRunning = true
                }
            }
        }

        stopButton = findViewById<Button>(R.id.stopCountdownButton)
        stopButton.setOnClickListener(){
            if (isRunning){
                timer.cancel()
                isRunning = false
            }else {
                println("Already stopped")
            }
        }
        coutdownDisplay = findViewById<TextView>(R.id.countDownView)
        pauseTimeDisplay = findViewById<TextView>(R.id.pauseTimeView)

        pauseTimeDisplay.text = millisecondsToDescriptiveTime(pauseTimeInMs)
    }

    fun startCountDown(v: View){

        timer = object : CountDownTimer(timeToCountDownInMs,timeTicks) {

            override fun onFinish() {
                updateCountDownDisplay(timeToCountDownInMs)
                if (reps >= 1){
                    Toast.makeText(this@MainActivity,"Arbeidsøkt er ferdig", Toast.LENGTH_SHORT).show()
                    timeToCountDownInMs = pauseTimeInMs // Pause time
                    isPause = true
                    startPause(v)
                } else {
                    Toast.makeText(this@MainActivity,"Alle økter ferdig", Toast.LENGTH_SHORT).show()
                    updateCountDownDisplay(repTime)
                    timeToCountDownInMs = repTime
                    isRunning = false
                }
            }

            override fun onTick(millisUntilFinished: Long) {
                updateCountDownDisplay(millisUntilFinished)
                timeToCountDownInMs = millisUntilFinished
            }
        }

        if(!repInput.text.isEmpty()){
            reps = repInput.text.toString().toInt() // lol
        }else{
            reps = 0
        }
        timer.start()
    }

    fun startPause(v: View){
        timer = object : CountDownTimer(timeToCountDownInMs, timeTicks){
            override fun onFinish() {
                coutdownDisplay.setTextColor(Color.BLACK)
                updateCountDownDisplay(timeToCountDownInMs)
                if(reps >= 1){
                    reps -= 1
                    repInput.setText(reps.toString())
                    Toast.makeText(this@MainActivity,"Pause ferdig", Toast.LENGTH_SHORT).show()
                    timeToCountDownInMs = repTime
                    isPause = false
                    startCountDown(v)
                }else{
                    isRunning = false
                }
            }

            override fun onTick(millisUntilFinished: Long) {
                updateCountDownDisplay(millisUntilFinished)
                timeToCountDownInMs = millisUntilFinished
            }
        }

        coutdownDisplay.setTextColor(Color.GREEN)
        timer.start()
    }

    fun updateCountDownDisplay(timeInMs:Long){
        coutdownDisplay.text = millisecondsToDescriptiveTime(timeInMs)
    }
}