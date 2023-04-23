package com.custom.webviewlauncher;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PasswordActivity extends AppCompatActivity {

    private final String password = "password123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        EditText passwordInput = findViewById(R.id.password_input);
        Button submitPasswordButton = findViewById(R.id.submit_password_button);
        Button homeButton = findViewById(R.id.home_button);

        submitPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredPassword = passwordInput.getText().toString();
                if (password.equals(enteredPassword)) {
                    Intent intent = new Intent(PasswordActivity.this, SettingsActivity.class);
                    startActivity(intent);
                    finish(); // Close the PasswordActivity
                } else {
                    // Inflate the custom toast layout
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.custom_toast, findViewById(R.id.custom_toast_root));

                    // Set the toast text
                    TextView text = layout.findViewById(R.id.custom_toast_text);
                    text.setText("Senha Incorreta");

                    // Create and show the custom toast
                    Toast incorrectPasswordToast = new Toast(getApplicationContext());
                    incorrectPasswordToast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 50);
                    incorrectPasswordToast.setDuration(Toast.LENGTH_SHORT);
                    incorrectPasswordToast.setView(layout);
                    incorrectPasswordToast.show();

                }
            }
        });

        // Set the click listener for the home button
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close the PasswordActivity and return to the MainActivity
            }
        });
    }
}
