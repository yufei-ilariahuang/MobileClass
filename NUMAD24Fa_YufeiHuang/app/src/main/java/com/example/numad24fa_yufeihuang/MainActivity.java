package com.example.numad24fa_yufeihuang;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the TextView and Button by their IDs
        TextView textView = findViewById(R.id.textView);
        Button displayButton = findViewById(R.id.display_button);
        //calcuator button
        Button calculatorButton = findViewById(R.id.calculator_button);


        // Set OnClickListener on the button to display a Toast with name and email
        displayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hardcoded name and email for demonstration
                String name = "Yufei Huang";
                String email = "huang.yufe@northeastern.edu";

                // Create the toast message
                String message = "Name: " + name + "\nEmail: " + email;

                // Display the toast
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
        // Set OnClickListener on the calculator button to launch the QuickCalcApp
        calculatorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QuickCalcApp.class);
                startActivity(intent);
            }
        });
    }
}