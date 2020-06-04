package ensisa.group5.confined.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ensisa.group5.confined.R;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEdit;
    private EditText passwordEdit;
    private EditText confirmEdit;
    private Button signinBtn;

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

        signinBtn.setEnabled(false);

        usernameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                signinBtn.setEnabled(loginValidation.isUsernameFormatCorrect(s.toString()) &&
                         loginValidation.isPasswordFormatCorrect(
                                 passwordEdit.getText().toString()));
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
                signinBtn.setEnabled(loginValidation.isPasswordFormatCorrect(s.toString()) &&
                        loginValidation.isUsernameFormatCorrect(
                                usernameEdit.getText().toString()));
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
                signinBtn.setEnabled(loginValidation.isPasswordConfirmMatch(
                        passwordEdit.getText().toString(),
                        s.toString()));
                System.out.println(passwordEdit.getText().toString() + " : " + s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEdit.getText().toString();
                //System.out.println(new DataBase().execute("select * from panier"));
                //String status = getResources().getString(R.string.STATUS_SUCCESS);
                if (loginValidation.isUsernameExist(username)) {
                    if (loginValidation.isEmailValid(username)) {
                        username = preferences.getString(getResources()
                                        .getString(R.string.PREF_KEY_MAIL),
                                null);
                    }
                    else {
                        username = preferences.getString(getResources()
                                        .getString(R.string.PREF_KEY_USERNAME),
                                null);
                    }
                } else {
                    confirmEdit.getLayoutParams().height = (int) getResources().getDimension(R.dimen.login_edit_height);
                    confirmEdit.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}