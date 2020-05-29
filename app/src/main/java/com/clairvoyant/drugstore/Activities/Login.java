package com.clairvoyant.drugstore.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.clairvoyant.drugstore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Login";
    private TextInputEditText nameText, phoneText, codeText;
    private Button getCodeButton, loginButton;
    TextInputLayout nameLayout, codeLayout, phonelayout;
    private ProgressBar progressBarVerify,progressBarLogin;
    private FirebaseAuth mAuth;
    private String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nameText      = findViewById(R.id.nameEditText);
        phoneText     = findViewById(R.id.phoneEditText);
        codeText      = findViewById(R.id.codeEditText);
        getCodeButton = findViewById(R.id.getCodeButton);
        loginButton   = findViewById(R.id.loginButton);
        nameLayout    = findViewById(R.id.outlinedNameTextField);
        codeLayout    = findViewById(R.id.outlinedCodeTextField);
        phonelayout   = findViewById(R.id.outlinedPhoneTextField);

        progressBarVerify = findViewById(R.id.progressBarVerify);
        progressBarLogin = findViewById(R.id.progressBarLogin);

        mAuth = FirebaseAuth.getInstance();
        mAuth.setLanguageCode("en");

        getCodeButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);

//        Objects.requireNonNull(getSupportActionBar()).setTitle("Login");
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.getCodeButton:
                if (TextUtils.isEmpty(phoneText.getText())){
                    phoneText.setError("Enter a phone number first!");
                    phoneText.requestFocus();
                }else {
                    sendVerificationCode(phoneText.getText().toString().trim());
                }
                break;
            case R.id.loginButton:
                if (codeLayout.getVisibility() == View.VISIBLE) {
                    if (TextUtils.isEmpty(codeText.getText())){
                        codeText.setError("Enter the verification code that you have just received!");
                        codeText.requestFocus();
                    }else
                        verifyVerificationCode(codeText.getText().toString().trim());
                }
                break;
            default:
                view.clearFocus();
        }
    }

    private void sendVerificationCode(String mobile) {
        progressBarVerify.setVisibility(View.VISIBLE);
        Log.d(TAG,"inside sendVerificationCode = "+"YES");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+88" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            Log.d(TAG,"inside OnVerificationStateChangedCallbacks = "+"YES");
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerificationId = s;
            mResendToken = forceResendingToken;
            Log.d(TAG,"mVerificationId = "+s);
            codeLayout.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.VISIBLE);
            nameLayout.setVisibility(View.GONE);
            phonelayout.setVisibility(View.GONE);
            getCodeButton.setVisibility(View.GONE);
            progressBarVerify.setVisibility(View.GONE);
        }
    };

    private void verifyVerificationCode(String otp) {
        progressBarLogin.setVisibility(View.VISIBLE);
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
//                            String user = task.getResult().getUser().getPhoneNumber();
//                            Log.d(TAG,"user = "+user);

                            progressBarLogin.setVisibility(View.GONE);
                            //verification successful we will start the profile activity
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            snackbar.show();
                        }
                    }
                });
    }
}