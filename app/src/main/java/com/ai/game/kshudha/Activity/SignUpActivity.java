package com.ai.game.kshudha.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ai.game.kshudha.Model.Firebase_User;
import com.ai.game.kshudha.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity implements View.OnKeyListener{
    public TextInputEditText name, eMail, password, confirmPassword;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        name = findViewById(R.id.name_text);
        eMail = findViewById(R.id.email_text);
        password = findViewById(R.id.password_text);
        confirmPassword = findViewById(R.id.confirmPassword_text);
    }

    public void logIn(View view){
        finish();
    }

    private void showAlert(String title, String message, boolean error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.cancel();
                    // don't forget to change the line below with the names of your Activities
                    if (!error) {
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        mAuth.signOut();
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }

    public void signUp(View view){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        if(TextUtils.isEmpty(name.getText()) || TextUtils.isEmpty(eMail.getText()) || TextUtils.isEmpty(password.getText()) || TextUtils.isEmpty(confirmPassword.getText())) {
            if(TextUtils.isEmpty(eMail.getText())) eMail.setError("Email-Id Required");
            if(TextUtils.isEmpty(name.getText())) name.setError("Name Required");
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
        else if(!password.getText().toString().equals(confirmPassword.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Password Mismatch", Toast.LENGTH_SHORT).show();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

        else {
            ProgressBar bar = findViewById(R.id.progressBar);
            bar.setVisibility(View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(eMail.getText().toString().trim(), password.getText().toString().trim())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Firebase_User user = new Firebase_User(
                                        name.getText().toString(),
                                        eMail.getText().toString(),
                                        password.getText().toString()
                                );
                                String uid = task.getResult().getUser().getUid();
                                database.getReference().child("Users").child(uid).setValue(user);
                                if(mAuth.getCurrentUser() != null){
                                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            bar.setVisibility(View.GONE);
                                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                            if(task.isSuccessful()){
                                                Toast.makeText(SignUpActivity.this, "Verification Email sent!", Toast.LENGTH_SHORT).show();
                                                showAlert("Account Created Successfully!", "Please verify your email before Login. Please check your spam folder for verification email.", false);
                                            }
                                            else{
                                                showAlert("error!", task.getException().getMessage(), true);
                                            }
                                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                        }
                                    });
                                }
                            } else {
                                bar.setVisibility(View.GONE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                // If sign in fails, display a message to the user.
                                showAlert("Error : Account Creation failed", "Account could not be created" + " :" + task.getException().getMessage(), true);
                            }
                        }
                    });
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
            signUp(v);
        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
}