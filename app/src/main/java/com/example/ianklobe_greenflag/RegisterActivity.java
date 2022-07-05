package com.example.ianklobe_greenflag;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    // Start Global Variables
    ImageButton ibtnSubmit;
    ImageButton ibtnBack;
    EditText etEmail;
    EditText etPassword;
    EditText etPasswordRetype;
    TextView tvError;
    ImageView ivEmail;
    ImageView ivPassword;
    ImageView ivPasswordRetype;
    LinearLayout llPassword;
    LinearLayout llEmail;

    // Regex Password Checkers
    final String LOWERCASE = "^.*[a-z]+.*$";
    final String UPPERCASE = "^.*[A-Z]+.*$";
    final String NUMERICAL = "^.*[0-9]+.*$";
    final String OVERALL = "^[a-zA-Z0-9*_+()%$#!@^]{8,}$";

    // Booleans to determine valid input in the three fields
    boolean validRetype = false;
    boolean validPassword = false;
    boolean validEmail = false;

    // Database
    myDbAdapter database;
    // End Global Variables



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Setting id's of every resource that gets interacted with
        ibtnSubmit = findViewById(R.id.ibtn_submit);
        ibtnBack = findViewById(R.id.ibtn_back);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etPasswordRetype = findViewById(R.id.et_password_retype);
        ivEmail = findViewById(R.id.iv_email);
        ivPassword = findViewById(R.id.iv_password);
        ivPasswordRetype = findViewById(R.id.iv_password_retype);
        llPassword = findViewById(R.id.ll_password);
        llEmail = findViewById(R.id.ll_email);
        tvError = findViewById(R.id.tv_error);

        // Connecting to database
        database  = new myDbAdapter(RegisterActivity.this);

        ibtnSubmit.setEnabled(false);

        // ibtnBack sends you back to the main page
        ibtnBack.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        // ibtnSubmit adds user and resets the activity
        ibtnSubmit.setOnClickListener(view -> {
            database.addUser(etEmail.getText().toString().trim(), etPassword.getText().toString().trim());
            Toast.makeText(this, etEmail.getText() + " has been added.", Toast.LENGTH_SHORT).show();

            etEmail.setText("");
            etPassword.setText("");
            etPasswordRetype.setText("");

            finish();
        });

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // This will check if the user input (email) is valid and then check
                // if all three input fields are valid
                validEmail = isValidEmail(etEmail.getText());
                correctCredentials(validEmail, validPassword ,validRetype);
            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // This will check if the user input (password) is valid and then check
                // if all three input fields are valid
                validRetype = isValidRetype(etPassword.getText().toString(),
                        etPasswordRetype.getText().toString());
                validPassword = isValidPassword(etPassword.getText().toString());
                correctCredentials(validEmail, validPassword ,validRetype);
            }
        });

        etPasswordRetype.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // This will check if the user input (retype) is valid and then check
                // if all three input fields are valid
                validRetype = isValidRetype(etPassword.getText().toString(),
                        etPasswordRetype.getText().toString());
                correctCredentials(validEmail, validPassword ,validRetype);
            }
        });
    }

    // Returns boolean of if the email was valid
    public boolean isValidEmail(CharSequence email) {
        // Checks the email against the built in email regex and the database
        if(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() && !database.checkUser(email.toString())){
            // Sets the border to green, adds the checkbox,
            // and removes the error (if applicable)
            etEmail.setBackgroundResource(R.drawable.border_green);
            ivEmail.setVisibility(View.VISIBLE);
            llEmail.setVisibility(View.GONE);
            return true;
        }else if(email.length() == 0){
            // Sets the border to white, removes the checkbox (if applicable),
            // and removes the error (if applicable)
            etEmail.setBackgroundResource(R.color.white);
            ivEmail.setVisibility(View.INVISIBLE);
            llEmail.setVisibility(View.GONE);
            return false;
        }
        else{
            // Sets the border to red, removes the checkbox (if applicable),
            // and enables the error if the email is registered
            if(database.checkUser(email.toString())) {
                etEmail.setBackgroundResource(R.drawable.border_red);
                ivEmail.setVisibility(View.INVISIBLE);
                llEmail.setVisibility(View.VISIBLE);
            }

            return false;
        }
    }

    // Returns boolean of if the password was valid
    public boolean isValidPassword(String password) {
        // Checks password to custom password regexs
        if(password.matches(LOWERCASE)
                && password.matches(UPPERCASE)
                && password.matches(NUMERICAL)
                && password.matches(OVERALL)) {
            // Sets the border to green, adds the checkbox,
            // and removes the error (if applicable)
            etPassword.setBackgroundResource(R.drawable.border_green);
            ivPassword.setVisibility(View.VISIBLE);
            llPassword.setVisibility(View.GONE);

            return true;

        } else if (password.length()==0) {
            // Sets the border to white, removes the checkbox (if applicable),
            // and removes the error (if applicable)
            etPassword.setBackgroundResource(R.color.white);
            ivPassword.setVisibility(View.INVISIBLE);
            llPassword.setVisibility(View.GONE);

            return false;
        } else {
            // Sets the border to red, removes the checkbox (if applicable),
            // and enables the error, with the correct error message
            etPassword.setBackgroundResource(R.drawable.border_red);
            ivPassword.setVisibility(View.INVISIBLE);
            llPassword.setVisibility(View.VISIBLE);
            tvError.setText(R.string.invalid);

            return false;
        }
    }

    // Returns boolean of if the retyped password was valid
    public boolean isValidRetype(String password, String retype) {
        if(retype.equals(password) && retype.length() != 0){
            // Sets the border to green, adds the checkbox,
            // and removes the error (if applicable)
            etPasswordRetype.setBackgroundResource(R.drawable.border_green);
            ivPasswordRetype.setVisibility(View.VISIBLE);
            llPassword.setVisibility(View.GONE);

            return true;

        }else if(retype.length() == 0){
            // Sets the border to white, removes the checkbox (if applicable),
            // and removes the error (if applicable)
            etPasswordRetype.setBackgroundResource(R.color.white);
            ivPasswordRetype.setVisibility(View.INVISIBLE);
            llPassword.setVisibility(View.GONE);

            return false;
        }
        else{
            // Sets the border to red, removes the checkbox (if applicable),
            // and enables the error, with the correct error message
            etPasswordRetype.setBackgroundResource(R.drawable.border_red);
            ivPasswordRetype.setVisibility(View.INVISIBLE);
            llPassword.setVisibility(View.VISIBLE);
            tvError.setText(R.string.mismatch);

            return false;
        }
    }

    // Checks if the three input fields are correct, and enables the next button
    // if they are
    public void correctCredentials(boolean email, boolean password, boolean retype) {
        if(email && password && retype) {
            ibtnSubmit.setEnabled(true);
            ibtnSubmit.setAlpha(1.0f);
        } else {
            ibtnSubmit.setEnabled(false);
            ibtnSubmit.setAlpha(0.5f);
        }
    }

}
