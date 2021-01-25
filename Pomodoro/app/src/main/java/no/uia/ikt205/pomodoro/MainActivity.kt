package no.uia.ikt205.pomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import no.uia.ikt205.pomodoro.util.millisecondsToDescriptiveTime

class MainActivity : AppCompatActivity() {

    lateinit var timer:CountDownTimer

    lateinit var thirtyButton:Button
    lateinit var sixtyButton:Button
    lateinit var ninetyButton:Button
    lateinit var onetwentyButton:Button

    lateinit var startButton:Button
    lateinit var stopButton:Button
    lateinit var coutdownDisplay:TextView

    var timeToCountDownInMs = 5000L
    val timeTicks = 1000L

    var isRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        thirtyButton = findViewById<Button>(R.id.thirtyMinButton)
        sixtyButton = findViewById<Button>(R.id.sixtyMinButton)
        ninetyButton = findViewById<Button>(R.id.ninetyMinButton)
        onetwentyButton = findViewById<Button>(R.id.onetwentyMinButton)

        thirtyButton.setOnClickListener(){
            timeToCountDownInMs = 1000 * 60 * 30;
            if (!isRunning){
                updateCountDownDisplay(timeToCountDownInMs)
            }
        }
        sixtyButton.setOnClickListener(){
            timeToCountDownInMs = 1000 * 60 * 60;
            if (!isRunning){
                updateCountDownDisplay(timeToCountDownInMs)
            }
        }
        ninetyButton.setOnClickListener(){
            timeToCountDownInMs = 1000 * 60 * 90;
            if (!isRunning){
                updateCountDownDisplay(timeToCountDownInMs)
            }
        }
        onetwentyButton.setOnClickListener(){
            timeToCountDownInMs = 1000 * 60 * 120;
            if (!isRunning){
                updateCountDownDisplay(timeToCountDownInMs)
            }
        }

       startButton = findViewById<Button>(R.id.startCountdownButton)
       startButton.setOnClickListener(){
           if (isRunning){
                println("Already running");
           }else {
               startCountDown(it)
               isRunning = true;
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

    }

    fun startCountDown(v: View){

        timer = object : CountDownTimer(timeToCountDownInMs,timeTicks) {
            override fun onFinish() {
                Toast.makeText(this@MainActivity,"Arbeidsøkt er ferdig", Toast.LENGTH_SHORT).show()
                updateCountDownDisplay(timeToCountDownInMs)
                isRunning = false
            }

            override fun onTick(millisUntilFinished: Long) {
               updateCountDownDisplay(millisUntilFinished)
                timeToCountDownInMs = millisUntilFinished
            }
        }

        timer.start()
    }

    fun updateCountDownDisplay(timeInMs:Long){
        coutdownDisplay.text = millisecondsToDescriptiveTime(timeInMs)
    }

}