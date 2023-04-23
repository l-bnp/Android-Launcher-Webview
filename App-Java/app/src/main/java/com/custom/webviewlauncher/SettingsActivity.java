// Beginning of SettingsActivity.java

package com.custom.webviewlauncher;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        EditText addressInput = findViewById(R.id.address_input);
        Button applyButton = findViewById(R.id.apply_button);
        Button homeButton = findViewById(R.id.home_button);

        // Get the current URL from MainActivity
        String currentUrl = MainActivity.getUrl();
        // Set the current URL as the default text in the EditText
        addressInput.setText(currentUrl);

        // Set the click listener for the apply button
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUrl = addressInput.getText().toString();
                MainActivity.setUrl(newUrl);
                // finish();

                // Inflate the custom toast layout
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast, findViewById(R.id.custom_toast_root));
                // Set the toast text
                TextView text = layout.findViewById(R.id.custom_toast_text);
                text.setText("O Endereço Foi Salvo");
                // Create and show the custom toast
                Toast incorrectPasswordToast = new Toast(getApplicationContext());
                incorrectPasswordToast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 50);
                incorrectPasswordToast.setDuration(Toast.LENGTH_SHORT);
                incorrectPasswordToast.setView(layout);
                incorrectPasswordToast.show();
            }
        });

        // Set the click listener for the home button
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close the SettingsActivity and return to the MainActivity
            }
        });

        Button systemSettingsButton = findViewById(R.id.system_settings_button);
        systemSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSystemSettings();
            }
        });
    }

    // Method to open the system settings
    private void openSystemSettings() {
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        startActivity(intent);
    }

}
// End of SettingsActivity.java