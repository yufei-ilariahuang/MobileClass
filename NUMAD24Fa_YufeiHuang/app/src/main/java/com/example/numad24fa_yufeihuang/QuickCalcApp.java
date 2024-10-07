package com.example.numad24fa_yufeihuang;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class QuickCalcApp extends AppCompatActivity implements View.OnClickListener {

    private TextView calcDisplay;
    private StringBuilder currentInput;
    private double firstOperand;
    private String operator;
    private boolean isNewInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quick_calc_app);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        calcDisplay = findViewById(R.id.calc_display);
        currentInput = new StringBuilder();
        isNewInput = true;

        // Initialize buttons
        int[] buttonIds = {R.id.button_0, R.id.button_1, R.id.button_2, R.id.button_3, R.id.button_4,
                R.id.button_5, R.id.button_6, R.id.button_7, R.id.button_8, R.id.button_9,
                R.id.button_plus, R.id.button_minus, R.id.button_multiply, R.id.button_equals};
        // sets up click listeners for all the buttons
        for (int id : buttonIds) {
            findViewById(id).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        Button button = (Button) view;
        String buttonText = button.getText().toString();

        switch (buttonText) {
            case "+":
            case "-":
            case "*":
                handleOperator(buttonText);
                break;
            case "=":
                calculateResult();
                break;
            default:
                handleNumber(buttonText);
                break;
        }
    }

    private void handleNumber(String number) {
        if (isNewInput) {
            currentInput.setLength(0);
            isNewInput = false;
        }
        currentInput.append(number);
        updateDisplay();
    }

    private void handleOperator(String newOperator) {
        if (!isNewInput) {
            calculateResult();
        }
        firstOperand = Double.parseDouble(calcDisplay.getText().toString());
        operator = newOperator;
        isNewInput = true;
    }

    private void calculateResult() {
        if (!isNewInput && operator != null) {
            double secondOperand = Double.parseDouble(currentInput.toString());
            double result = 0;
            switch (operator) {
                case "+":
                    result = firstOperand + secondOperand;
                    break;
                case "-":
                    result = firstOperand - secondOperand;
                    break;
                case "*":
                    result = firstOperand * secondOperand;
                    break;
            }
            currentInput.setLength(0);
            currentInput.append(result);
            updateDisplay();
            isNewInput = true;
            operator = null;
        }
    }

    private void updateDisplay() {
        calcDisplay.setText(currentInput.toString());
    }
}