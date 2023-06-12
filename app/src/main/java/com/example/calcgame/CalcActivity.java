package com.example.calcgame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Random;

public class CalcActivity extends AppCompatActivity {

    private static final long GAME_DURATION = 30000;  // 게임 시간(밀리초)

    private TextView textViewNum1;
    private TextView textViewNum2;
    private TextView textViewIcon;
    private TextView textViewGameScore;
    private TextView textViewGameTimer;
    private EditText editTextNumber;

    private int currentScore;
    private int answer;

    private int did = 0;
    private CountDownTimer countDownTimer;

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, CalcActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);

        textViewNum1 = findViewById(R.id.textViewNum1);
        textViewNum2 = findViewById(R.id.textViewNum2);
        textViewIcon = findViewById(R.id.textViewIcon);
        textViewGameScore = findViewById(R.id.textViewGameScore);
        textViewGameTimer = findViewById(R.id.textViewGameTimer);
        editTextNumber = findViewById(R.id.editTextNumber);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int num1 = bundle.getInt("num1");
            int num2 = bundle.getInt("num2");
            String operator = bundle.getString("operator");
            answer = bundle.getInt("answer");
            Toast.makeText(this, "debug:"+answer, Toast.LENGTH_SHORT).show();   //디버깅 토스트 : 정답 확인용
            textViewNum1.setText(String.valueOf(num1));
            textViewNum2.setText(String.valueOf(num2));
            textViewIcon.setText(operator);

            startTimer();
        }

        editTextNumber.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (keyCode){
                    case KeyEvent.KEYCODE_ENTER:
                        checkAnswer();
                        return true;
                }
                return false;
            }
        });


    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(GAME_DURATION, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                textViewGameTimer.setText(String.format(Locale.getDefault(), "남은 시간: %d초", seconds));
            }

            @Override
            public void onFinish() {
                textViewGameTimer.setText("남은 시간: 0초");
                showResultDialog(currentScore);
            }
        }.start();
    }

    private int calculateAnswer(int num1, int num2, String operator) {
        int answer = 0;

        switch (operator) {
            case "+":
                answer = num1 + num2;
                break;
            case "-":
                answer = num1 - num2;
                break;
            case "*":
                answer = num1 * num2;
                break;
            case "/":
                answer = num1 / num2;
                break;
        }

        return answer;
    }

    private void checkAnswer() {
        String inputText = editTextNumber.getText().toString().trim();

        if (!inputText.isEmpty()) {

            if (inputText.equals("-")) {
                showResultDialog(currentScore);
                return;
            }
            float input = Float.parseFloat(inputText);
            float userAnswer = input;

            if (userAnswer == answer) {
                currentScore += 10;
                textViewGameScore.setText("스코어: " + currentScore);
                did++;
                resetGame();
            } else {
                showResultDialog(currentScore);

            }
        }
    }

    private void resetGame() {
        countDownTimer.cancel();
        editTextNumber.setText("");

        Random random = new Random();
        int num11 = random.nextInt(201) - 100;  // -100에서 100까지의 랜덤한 값
        int num22 = random.nextInt(201) - 100;  // -100에서 100까지의 랜덤한 값

        String[] operators = {"+", "-", "*", "/"};
        String operator1 = operators[random.nextInt(4)];  // 덧셈, 뺄셈, 곱셈, 나눗셈 중 하나 선택
        textViewNum1.setText(String.valueOf(num11));
        textViewNum2.setText(String.valueOf(num22));
        textViewIcon.setText(operator1);

        answer = calculateAnswer(num11, num22, operator1);
        Toast.makeText(this, "debug:"+answer, Toast.LENGTH_SHORT).show();   //디버깅 토스트 : 정답 확인용
        startTimer();
    }

    @Override
    public void onBackPressed() {
        showResultDialog(currentScore);
    }

    private void showResultDialog(int score) {
        if (!isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("게임 결과")
                    .setMessage("최종 스코어: " + score)
                    .setPositiveButton("돌아가기", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
    }
}