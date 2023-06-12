package com.example.calcgame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private TextView textViewMainScore;
    private Button buttonStart;
    private int maxScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewMainScore = findViewById(R.id.textViewMainScore);
        buttonStart = findViewById(R.id.buttonStart);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });
    }

    private void startGame() {
        Random random = new Random();
        int num1 = random.nextInt(201) - 100;  // -100에서 100까지의 랜덤한 값
        int num2 = random.nextInt(201) - 100;  // -100에서 100까지의 랜덤한 값

        String[] operators = {"+", "-", "*", "/"};
        String operator = operators[random.nextInt(4)];  // 덧셈, 뺄셈, 곱셈, 나눗셈 중 하나 선택

        int answer = calculateAnswer(num1, num2, operator);

        Bundle bundle = new Bundle();
        bundle.putInt("num1", num1);
        bundle.putInt("num2", num2);
        bundle.putString("operator", operator);
        bundle.putInt("answer", answer);

        CalcActivity.start(this, bundle);
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

    public void showResultDialog(int score) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("게임 종료")
                .setMessage("최종 스코어: " + score)
                .setPositiveButton("돌아가기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (score > maxScore) {
                            maxScore = score;
                            textViewMainScore.setText(String.valueOf(maxScore));
                        }
                    }
                })
                .setCancelable(false)
                .show();
    }

}