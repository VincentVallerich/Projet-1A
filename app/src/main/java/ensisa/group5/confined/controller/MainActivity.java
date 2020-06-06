package ensisa.group5.confined.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
<<<<<<< HEAD
=======
<<<<<<< HEAD
=======
import android.util.Log;


import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.lang.NonNull;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.core.auth.StitchUser;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential;
import com.mongodb.stitch.core.auth.providers.userpassword.UserPasswordCredential;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteFindOptions;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateOptions;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateResult;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
>>>>>>> taches

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

        signinBtn.setEnabled(true);

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
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

<<<<<<< HEAD
        signinBtn.setOnClickListener(v -> {
            String username = usernameEdit.getText().toString();
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
=======
        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEdit.getText().toString();
                //DataBase db = new DataBase();
                MongoDatabase db = new MongoDatabase();
                Thread thread = new Thread(db);
                thread.start();
                String pswd = passwordEdit.getText().toString();
                //String status = getResources().getString(R.string.STATUS_SUCCESS);
                String finalUsername = username;
                new Thread(new Runnable() {
                    public void run() {
                        UserPasswordCredential credential = new UserPasswordCredential(finalUsername,  pswd );
                        Stitch.initializeDefaultAppClient("apptest-vzuxl");
                        Stitch.getDefaultAppClient().getAuth().loginWithCredential(credential)
                                .addOnCompleteListener(new OnCompleteListener<StitchUser>() {
                                       @Override
                                       public void onComplete(@NonNull final Task<StitchUser> task) {
                                           if (task.isSuccessful()) {
                                               Log.d("stitch", "Successfully logged in as user " + task.getResult().getId());
                                               Log.d("stitch", finalUsername);
                                               Log.d("stitch", pswd);
                                           } else {
                                               Log.e("stitch", "Error logging in with email/password auth:", task.getException());

                                           }
                                       }
                                   }
                                );


                    }
                }).start();
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
                    /*try {
                        System.out.println(db.execute("select * from panier"));
                    } catch (DataBaseException e) {
                        e.printStackTrace();
                    }*/
>>>>>>> taches
                }
            } else {
                confirmEdit.getLayoutParams().height = (int) getResources().getDimension(R.dimen.login_edit_height);
                confirmEdit.setVisibility(View.VISIBLE);
            }
        });
    }
}