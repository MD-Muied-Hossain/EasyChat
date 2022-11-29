package com.muiedhossain.easychat20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.muiedhossain.easychat20.ChatList.FriendListActivity;
import com.muiedhossain.easychat20.ChatList.User;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private EditText name, email, password;
    private AppCompatButton submitBtn;
    private TextView LoginInfoPage, headLine, forgetPasswordTv;
    private boolean isSigningUp = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Easy Chat");

        name = findViewById(R.id.edtName);
        password = findViewById(R.id.edtPassword);
        email = findViewById(R.id.edtEmail);
        submitBtn = findViewById(R.id.submitBtn);
        forgetPasswordTv = findViewById(R.id.tv_forgetPassword);
        LoginInfoPage = findViewById(R.id.login_info_TV_btn);
        headLine = findViewById(R.id.headLine);

        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(MainActivity.this, FriendListActivity.class));
            finish();
        }

        forgetPasswordTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ForgetPasswordActivity.class));
                finish();
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                    if (isSigningUp && name.getText().toString().isEmpty()) {
                        Toast.makeText(MainActivity.this, "Fields can not be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    if (isSigningUp) {
                        handleSignUp();
                    } else {
                        handleLogin();
                    }
                }
            }
        });

        LoginInfoPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSigningUp) {
                    isSigningUp = false;
                    headLine.setText("Login To Your Account");
                    name.setVisibility(View.GONE);
                    submitBtn.setText("Log in");
                    forgetPasswordTv.setVisibility(View.VISIBLE);
                    LoginInfoPage.setText("Don't Have an Account? Sign up?");
                } else {
                    isSigningUp = true;
                    headLine.setText("Register Your Account");
                    name.setVisibility(View.VISIBLE);
                    submitBtn.setText("Sign up");
                    forgetPasswordTv.setVisibility(View.GONE);
                    LoginInfoPage.setText("Already Have an Account?");
                }
            }
        });

        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });


    }

    private void handleSignUp() {
        firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    firebaseDatabase.getReference("user/" + firebaseAuth.getCurrentUser().getUid()).setValue(new User(name.getText().toString(), email.getText().toString(), ""));
                    startActivity(new Intent(MainActivity.this, FriendListActivity.class));
                    Toast.makeText(MainActivity.this, "SignedUp Successfully", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MainActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void handleLogin() {
        firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(MainActivity.this, FriendListActivity.class));
                    Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, "please wait", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (view != null && view instanceof EditText) {
                Rect r = new Rect();
                view.getGlobalVisibleRect(r);
                int rawX = (int) ev.getRawX();
                int rawY = (int) ev.getRawY();
                if (!r.contains(rawX, rawY)) {
                    view.clearFocus();
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}