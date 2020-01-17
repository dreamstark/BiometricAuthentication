package com.example.fingerprintauth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.dreamstark.fingerprintauthentication.FingerPrintAuth;
import com.dreamstark.fingerprintauthentication.FingerPrintAuthDialog;

public class MainActivity extends AppCompatActivity {

    private FingerPrintAuthDialog fingerPrintAuthDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final boolean hasFingerprintSupport = FingerPrintAuth.hasFingerprintSupport(this);

        findViewById(R.id.openDialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fingerPrintAuthDialog = null;
                if (hasFingerprintSupport)
                    createAndShowDialog();
                else Toast.makeText(MainActivity.this, "Your device does not support fingerprint authentication", Toast.LENGTH_SHORT).show();
            }
        });
        
    }

    private void createAndShowDialog() {

        fingerPrintAuthDialog = new FingerPrintAuthDialog(this)
                .setTitle("Sign in")
                .setCancelable(false)
                .setPositiveButton("Use password", null)
                .setNegativeButton("Cancel", null)
                .setOnFingerAuthListener(new FingerPrintAuth.OnFingerAuthListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(MainActivity.this, "onSuccess", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(MainActivity.this, "onFailure", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(MainActivity.this, "onError", Toast.LENGTH_SHORT).show();
                    }
                });
        fingerPrintAuthDialog.show();

    }
}
