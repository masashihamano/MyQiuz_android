package edu.misao.myqiuz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MYSCORE = "edu.misao.myqiuz.MYSCORE";
    private ArrayList<String[]> quizSet = new ArrayList<String[]>();

    TextView scoreText,qText;
    Button a0btn,a1btn,a2btn,a3btn,nextBtn;

    private int currentQuiz = 0;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        loadQuizSet();

        getViews();

        setQuiz();
    }

    private void showScore() {
        scoreText.setText("Score: " + score + " / " + quizSet.size());
    }

    private void setQuiz() {
        qText.setText(quizSet.get(currentQuiz)[0]);

        ArrayList<String> answers = new ArrayList<String>();
        for (int i = 1; i <= 4; i++) {
            answers.add(quizSet.get(currentQuiz)[i]);
        }
        Collections.shuffle(answers);

        a0btn.setText(answers.get(0));
        a1btn.setText(answers.get(1));
        a2btn.setText(answers.get(2));
        a3btn.setText(answers.get(3));

        a0btn.setEnabled(true);
        a1btn.setEnabled(true);
        a2btn.setEnabled(true);
        a3btn.setEnabled(true);
        nextBtn.setEnabled(false);

        showScore();
    }

    public void checkAnswer(View view) {
        // answer?
        Button clickedButton = (Button) view;
        String clickedAnswer = clickedButton.getText().toString();

        // judge
        if (clickedAnswer.equals(quizSet.get(currentQuiz)[1])) {
            clickedButton.setText("◯ " + clickedAnswer);
            score++;
        } else {
            clickedButton.setText("× " + clickedAnswer);
        }
        showScore();

        // button
        a0btn.setEnabled(false);
        a1btn.setEnabled(false);
        a2btn.setEnabled(false);
        a3btn.setEnabled(false);
        nextBtn.setEnabled(true);

        // next quiz
        currentQuiz++;

        if (currentQuiz == quizSet.size()) {
            nextBtn.setText("Check result");
        }
    }

    public void goNext(View view) {
        if (currentQuiz == quizSet.size()) {
            // show result
            Intent intent = new Intent(this, MyResultActivity.class);
            intent.putExtra(EXTRA_MYSCORE, score + " / " + quizSet.size());
            startActivity(intent);
        } else {
            setQuiz();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        nextBtn.setText("Next");
        currentQuiz = 0;
        score = 0;
        setQuiz();
    }

    private void getViews() {
        scoreText = (TextView) findViewById(R.id.scoreText);
        qText = (TextView) findViewById(R.id.qText);
        a0btn = (Button) findViewById(R.id.a0btn);
        a1btn = (Button) findViewById(R.id.a1btn);
        a2btn = (Button) findViewById(R.id.a2btn);
        a3btn = (Button) findViewById(R.id.a3btn);
        nextBtn = (Button) findViewById(R.id.nextBtn);
    }

    private void loadQuizSet() {
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        try {
            inputStream = getAssets().open("quiz");
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                quizSet.add(s.split("\t"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) inputStream.close();
                if (bufferedReader != null) bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
