package ensisa.group5.confined.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.auth.providers.userpassword.UserPasswordAuthProviderClient;

import java.util.concurrent.atomic.AtomicReference;

import ensisa.group5.confined.R;
import ensisa.group5.confined.ui.TaskActivity;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEdit;
    private EditText pseudoEdit;
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

        dataBase = new DataBase(this, preferences);
        usernameEdit = (EditText) findViewById(R.id.login_username_edit);
        pseudoEdit = (EditText) findViewById(R.id.login_pseudo_edit);
        passwordEdit = (EditText) findViewById(R.id.login_password_edit);
        confirmEdit = (EditText) findViewById(R.id.login_confirm_edit);
        signinBtn = (Button) findViewById(R.id.signin_btn);
        registerBtn = (Button) findViewById(R.id.register_btn);

        /* for clear all preferences, to use in the case of disconnect */
        preferences.edit().clear().apply();

        /* if user connected so redirect instantly */
        if (preferences.contains(getString(R.string.PREF_KEY_MAIL))) {
            dataBase.initClient();
            startBoardActivity(getApplicationContext());
        }

        signinBtn.setEnabled(false);

        usernameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                signinBtn.setEnabled(dataBase.isUsernameFormatCorrect(s.toString()) &&
                        dataBase.isPasswordFormatCorrect(passwordEdit.getText().toString()) &&
                        pseudoEdit.getVisibility() != View.VISIBLE);
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
                        dataBase.isPasswordFormatCorrect(s.toString()) &&
                        pseudoEdit.getVisibility() != View.VISIBLE);
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
                    startBoardActivity(getApplicationContext());
                } else {
                    if (dataBase.isUserAuthenticated(username,pswd)) {
                        // enregistrer les preferences
                        preferences.edit().putString(getString(R.string.PREF_KEY_MAIL), username).apply();
                        startBoardActivity(getApplicationContext());
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        registerBtn.setOnClickListener( v -> {
            signinBtn.setEnabled(false);
            String username = usernameEdit.getText().toString();
            String pswd = passwordEdit.getText().toString();

            confirmEdit.getLayoutParams().height = (int) getResources().getDimension(R.dimen.login_edit_height);
            confirmEdit.setVisibility(View.VISIBLE);

            pseudoEdit.getLayoutParams().height = (int) getResources().getDimension(R.dimen.login_edit_height);
            pseudoEdit.setVisibility(View.VISIBLE);

            String pseudo = pseudoEdit.getText().toString();
            if (dataBase.isUsernameFormatCorrect(username) && dataBase.isUsernameFormatCorrect(pseudo)) {
                Thread register_thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        dataBase.initClient();
                        UserPasswordAuthProviderClient emailPassClient = Stitch.getDefaultAppClient().getAuth().getProviderClient(UserPasswordAuthProviderClient.factory);
                        emailPassClient.registerWithEmail(username, pswd).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                preferences.edit().putString(getString(R.string.PREF_KEY_MAIL), username).apply();
                                startBoardActivity(getApplicationContext());
                                finish();
                                Toast.makeText(getApplicationContext(),"Inscription réussie"   , Toast.LENGTH_SHORT).show();
                                try {
                                    if (dataBase.isUserAuthenticated(username,pswd)){
                                     startBoardActivity(getApplicationContext());
                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        }
                });
                register_thread.start();

            }
        });
    }

    public void startBoardActivity(Context context) { startActivity(new Intent(context, TaskActivity.class)); }
}
