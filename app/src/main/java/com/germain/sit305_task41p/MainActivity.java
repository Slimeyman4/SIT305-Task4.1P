package com.germain.sit305_task41p;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //Declare variables
    EditText myWorkoutDuration;
    EditText myRestDuration;
    Button myStartButton;
    Button myStopButton;
    TextView myCurrentPhase;
    TextView myTimeRemaining;
    ProgressBar myTimeBar;
    CountDownTimer myTimer;
    MediaPlayer myMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize variables
        myWorkoutDuration = findViewById(R.id.et_WorkoutDuration);
        myRestDuration = findViewById(R.id.et_RestDuration);
        myStartButton = findViewById(R.id.btn_Start);
        myStopButton = findViewById(R.id.btn_Stop);
        myCurrentPhase = findViewById(R.id.tv_CurrentPhase);
        myTimeRemaining = findViewById(R.id.tv_TimeRemaining);
        myTimeBar = findViewById(R.id.pb_TimeBar);
        myTimeBar.setProgress(100);
        myMediaPlayer = MediaPlayer.create(this, R.raw.timer_sound);
    }

    //Called when start button is clicked
    public void startTimer(View view){
        //If statement that check that the user has given an input.
        if (myWorkoutDuration.getText().toString().isEmpty() || myRestDuration.getText().toString().isEmpty()){
            //Notification for user to add their inputs
            Toast.makeText(this, "Please add your desired workout and rest duration (seconds)", Toast.LENGTH_SHORT).show();
            return;
        }

        //set variable based on user input
        int workoutSeconds = Integer.parseInt(myWorkoutDuration.getText().toString());
        //set current phase text
        myCurrentPhase.setText("CurrentPhase: Workout");
        //runs timer based on input parameters
        beginTimer(workoutSeconds,1);
    }

    //Called when stop button is clicked
    public void stopTimer(View view){
        //Checks to make sure there is a timer initialized first
        if (myTimer != null){
            //cancels and stops the current timer
            myTimer.cancel();
        }
    }

    //The beginTimer function is used to set up and run the timers for both the workout and rest periods
    private void beginTimer(int durationSeconds, int intervalSeconds){

        //converts the input parameters from int to long which is what the CountDownTimer requires.
        long millisInFuture = durationSeconds * 1000;
        long countDownInterval = intervalSeconds*1000;

        //Initializes the myTimer as a new CountDownTimer with the input settings.
        myTimer = new CountDownTimer(millisInFuture, countDownInterval){
            public void onTick(long millisUntilFinished) {

                //Sets the time remaining text to show the user how much time is left in the phase
                myTimeRemaining.setText("Time Remaining: " + millisUntilFinished / 1000);

                //Calculating and setting the progress bar
                int timeRemaining = (int) millisUntilFinished/1000;
                double tempDouble = (double)timeRemaining/(double)durationSeconds;
                double percentage = tempDouble*100;
                int calculation = (int) percentage;
                myTimeBar.setProgress(calculation);

                //Debugging code for the progress bar
                Log.v("TIME_REMAINING",String.valueOf(timeRemaining));
                Log.v("TEMP_DOUBLE",String.valueOf(tempDouble));
                Log.v("PERCENTAGE",String.valueOf(percentage));
                Log.v("CALCULATION",String.valueOf(calculation));
            }

            public void onFinish() {
                //reset the progressbar to 100%
                myTimeBar.setProgress(100);
                //Plays a sound to alert the user that the phase is over
                myMediaPlayer.start();

                //This if and else statement work to forever loop the timer between workout and rest phases
                if (myCurrentPhase.getText().equals("CurrentPhase: Workout")){
                    myCurrentPhase.setText("CurrentPhase: Rest");
                    int restSeconds = Integer.parseInt(myRestDuration.getText().toString());
                    beginTimer(restSeconds,1);
                }
                else {
                    myCurrentPhase.setText("CurrentPhase: Workout");
                    int workoutSeconds = Integer.parseInt(myWorkoutDuration.getText().toString());
                    beginTimer(workoutSeconds,1);
                }
            }
        }.start(); //Starts the timer
    }
}