package ensisa.group5.confined.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ensisa.group5.confined.R;
import ensisa.group5.confined.ui.ProfileActivity;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEdit;
    private EditText passwordEdit;
    private EditText confirmEdit;
    private Button signinBtn;
    private Button registerBtn;

    private SharedPreferences preferences;
    private DataBase dataBase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getPreferences(MODE_PRIVATE);
        /* just for tests */
        //preferences.edit().clear().apply();
        dataBase = new DataBase(this, preferences);
        usernameEdit = (EditText) findViewById(R.id.login_username_edit);
        passwordEdit = (EditText) findViewById(R.id.login_password_edit);
        confirmEdit = (EditText) findViewById(R.id.login_confirm_edit);
        signinBtn = (Button) findViewById(R.id.signin_btn);
        registerBtn = (Button) findViewById(R.id.register_btn);

        /* for clear all preferences, to use in the case of disconnect */
        preferences.edit().clear().apply();

        /* if user connected so redirect instantly */
        if (preferences.contains(getString(R.string.PREF_KEY_MAIL)))
            startBoardActivity(this);

        signinBtn.setEnabled(false);

        usernameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                signinBtn.setEnabled(dataBase.isUsernameFormatCorrect(s.toString()) &&
                        dataBase.isPasswordFormatCorrect(passwordEdit.getText().toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passwordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                signinBtn.setEnabled(dataBase.isUsernameFormatCorrect(usernameEdit.getText().toString()) &&
                        dataBase.isPasswordFormatCorrect(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        confirmEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                registerBtn.setEnabled(dataBase.isPasswordConfirmEquals(passwordEdit.getText().toString(), s.toString()) ||
                        confirmEdit.getVisibility() != View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signinBtn.setOnClickListener(v -> {
            String username = usernameEdit.getText().toString();
            String pswd = passwordEdit.getText().toString();
            try {
                if (preferences.contains(getString(R.string.PREF_KEY_MAIL))) {
                    startBoardActivity(this);
                } else {
                    if (dataBase.isUserAuthenticated(username,pswd)) {
                        // enregistrer les preferences
                        preferences.edit().putString(getString(R.string.PREF_KEY_MAIL), username).apply();
                        startBoardActivity(this);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        registerBtn.setOnClickListener( v -> {
            String username = usernameEdit.getText().toString();
            String pswd = passwordEdit.getText().toString();

            confirmEdit.getLayoutParams().height = (int) getResources().getDimension(R.dimen.login_edit_height);
            confirmEdit.setVisibility(View.VISIBLE);

            if (dataBase.isUsernameFormatCorrect(username)) {
                if (dataBase.registerUser(username, pswd)) {
                    preferences.edit().putString(getString(R.string.PREF_KEY_MAIL), username);
                    startBoardActivity(this);
                }
            }
        });
    }

    public void startBoardActivity(Context context) { startActivity(new Intent(context, ProfileActivity.class)); }
}
