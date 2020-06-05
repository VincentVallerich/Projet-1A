package ensisa.group5.confined.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;


import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.lang.NonNull;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.core.auth.StitchUser;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.auth.internal.CoreStitchUser;
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential;
import com.mongodb.stitch.core.auth.providers.userpassword.UserPasswordCredential;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteFindOptions;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateOptions;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateResult;

import org.bson.BsonString;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import ensisa.group5.confined.R;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.lt;

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



        /* partie tests */




        usernameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                signinBtn.setEnabled(loginValidation.isUsernameFormatCorrect(s.toString()));
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
                signinBtn.setEnabled(loginValidation.isPasswordFormatCorrect(s.toString()));
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

        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEdit.getText().toString();
                 String pswd = passwordEdit.getText().toString();
                //String status = getResources().getString(R.string.STATUS_SUCCESS);
                String finalUsername = username;
                new Thread(new Runnable() {
                    public void run() {
                        UserPasswordCredential credential = new UserPasswordCredential(finalUsername,  pswd );
                        Stitch.initializeDefaultAppClient("apptest-vzuxl");
                        StitchAppClient appclient = Stitch.getDefaultAppClient();
                        Stitch.getDefaultAppClient().getAuth().loginWithCredential(credential)
                                .addOnCompleteListener(new OnCompleteListener<StitchUser>() {
                                       @Override
                                       public void onComplete(@NonNull final Task<StitchUser> task) {
                                           if (task.isSuccessful()) {
                                               Log.d("stitch", "Successfully logged in as user " + task.getResult().getId());
                                               // Get a remote client
                                               final RemoteMongoClient remoteMongoClient =
                                                       Stitch.getDefaultAppClient().getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
                                               RemoteMongoCollection<Document> collection = remoteMongoClient.getDatabase("Confined_Project").getCollection("Users_Score");
                                                 String userid = Stitch.getDefaultAppClient().getAuth().getUser().getId();
                                               Task<Document> res = collection.findOne(eq("_id",new ObjectId("5eda3134355c4069c9c2ab49")));
                                                   // Print documents to the log.
                                                   Log.d("stitch", "Got document: " + res.toString());
                                               Log.d("stitch", pswd);

                                           } else {
                                               Log.d("stitch", "Error logging in with email/password auth:", task.getException());
                                               Log.d("stitch", pswd);
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
                }
            }
        });
    }
}

/*
final StitchAppClient client =
                                Stitch.initializeDefaultAppClient("apptest-vzuxl");
                        final RemoteMongoClient mongoClient =
                                client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
                        final RemoteMongoCollection<Document> coll =
                                mongoClient.getDatabase("sample_airbnb").getCollection("listingsAndReviews");

                        Document filterDoc = new Document();

                        RemoteFindIterable findResults = coll
                                .find(filterDoc);
                        Task <List<Document>> itemsTask = findResults.into(new ArrayList<Document>());
                        itemsTask.addOnCompleteListener(new OnCompleteListener <List<Document>> () {
                            @Override
                            public void onComplete(@NonNull Task<List<Document>> task) {
                                if (task.isSuccessful()) {
                                    List<Document> items = task.getResult();
                                    Log.d("app", String.format("successfully found %d documents", items.size()));
                                    for (Document item: items) {
                                        Log.d("app", String.format("successfully found:  %s", item.toString()));
                                    }
                                } else {
                                    Log.e("app", "failed to find documents with: ", task.getException());
                                }
                            }
                        });


 */