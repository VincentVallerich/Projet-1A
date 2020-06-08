package ensisa.group5.confined.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import ensisa.group5.confined.R;
import ensisa.group5.confined.controller.AsyncTask.LoginAsyncTask;
import ensisa.group5.confined.game.ScoreBordActivity;
import ensisa.group5.confined.ui.TaskActivity;

public class MainActivity extends AppCompatActivity  {

    private EditText usernameEdit;
    private EditText passwordEdit;
    private EditText confirmEdit;
    private Button signinBtn;
    private Button gameBtn;
    private Button uiBtn;

    private SharedPreferences preferences;
    private LoginValidation loginValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getPreferences(MODE_PRIVATE);
        /* just for tests */
        //preferences.edit().clear().apply();
        loginValidation = new LoginValidation(this, preferences);
        usernameEdit = (EditText) findViewById(R.id.login_username_edit);
        passwordEdit = (EditText) findViewById(R.id.login_password_edit);
        confirmEdit = (EditText) findViewById(R.id.login_confirm_edit);
        signinBtn = (Button) findViewById(R.id.signin_btn);
        gameBtn = (Button) findViewById(R.id.game_btn);
        uiBtn = (Button) findViewById(R.id.ui_btn);

        signinBtn.setEnabled(true);

        usernameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

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

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEdit.getText().toString();
                String pswd = passwordEdit.getText().toString();
                new LoginAsyncTask();
                /*try {
                    if (loginValidation.isUserAuthenticated(username,pswd)) {
                        // enregistrer les preferences
                        // redirect sur une autre page
                        Intent taskactivity = new Intent(MainActivity.this, TaskActivity.class);
                        startActivity(taskactivity);
                    }
                    else {
                        confirmEdit.getLayoutParams().height = (int) getResources().getDimension(R.dimen.login_edit_height);
                        confirmEdit.setVisibility(View.VISIBLE);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
            }
        });

        gameBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, ScoreBordActivity.class));
        });

        uiBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, TaskActivity.class));
        });
    }
}
