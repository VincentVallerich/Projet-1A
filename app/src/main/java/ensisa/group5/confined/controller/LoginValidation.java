package ensisa.group5.confined.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.lang.NonNull;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.core.auth.StitchUser;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateOptions;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateResult;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ensisa.group5.confined.R;

/**
 * Author VALLERICH Vincent on 04-06-2020
 */

public class LoginValidation {

    private Context context;
    private static SharedPreferences preferences;

    public static final int MIN_LEN_INPUT_USERNAME = 2;
    public static final int MIN_LEN_INPUT_PASSWORD = 6;
    String usernameKey;
    String mailKey;

    /**
     * @param context
     * @param preferences
     */
    public LoginValidation(Context context, SharedPreferences preferences) {
        this.context = context;
        this.preferences = preferences;

        usernameKey = context.getResources().getString(R.string.PREF_KEY_USERNAME);
        mailKey = context.getResources().getString(R.string.PREF_KEY_MAIL);






    }




    /**
     * @param username
     * @return true if user exist false otherwise
     * */
    public boolean isUsernameExist(String username) {
        if (isEmailValid(username))
            return preferences.getString(mailKey,null) != null;
        else
            return preferences.getString(usernameKey, null) != null;
    }

    /**
     * @param username
     * @return true if the username pattern is correct false otherwise
     * */
    public boolean isUsernameFormatCorrect(String username) {
        return username.length() >= MIN_LEN_INPUT_USERNAME;
    }

    /**
     * @param password
     * @return true if the password pattern is correct false otherwise
     * */
    public boolean isPasswordFormatCorrect(String password) {
        return password.length() >= MIN_LEN_INPUT_PASSWORD;
    }

    /**
     * @param password
     * @param confirm
     * @return true if password and confirm same false otherwise
     */
    public boolean isPasswordConfirmMatch(String password, String confirm) {
        return password == confirm;
    }

    /**
     * method is used for checking valid email id format.
     *
     * @param email
     * @return boolean true for valid false for invalid
     */
    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}

/*

final StitchAppClient client =
                            Stitch.initializeDefaultAppClient("apptest-vzuxl");

                    final RemoteMongoClient mongoClient =
                            client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");

                    final RemoteMongoCollection<Document> coll =
                            mongoClient.getDatabase("<DATABASE>").getCollection("<COLLECTION>");

                    client.getAuth().loginWithCredential(new AnonymousCredential()).continueWithTask(
                            new Continuation<StitchUser, Task<RemoteUpdateResult>>() {

                                @Override
                                public Task<RemoteUpdateResult> then(@NonNull Task<StitchUser> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        Log.e("STITCH", "Login failed!");
                                        throw task.getException();
                                    }

                                    final Document updateDoc = new Document(
                                            "owner_id",
                                            task.getResult().getId()
                                    );

                                    updateDoc.put("number", 42);
                                    return coll.updateOne(
                                            null, updateDoc, new RemoteUpdateOptions().upsert(true)
                                    );
                                }
                            }
                    ).continueWithTask(new Continuation<RemoteUpdateResult, Task<List<Document>>>() {
                        @Override
                        public Task<List<Document>> then(@NonNull Task<RemoteUpdateResult> task) throws Exception {
                            if (!task.isSuccessful()) {
                                Log.e("STITCH", "Update failed!");
                                throw task.getException();
                            }
                            List<Document> docs = new ArrayList<>();
                            return coll
                                    .find(new Document("owner_id", client.getAuth().getUser().getId()))
                                    .limit(100)
                                    .into(docs);
                        }
                    }).addOnCompleteListener(new OnCompleteListener<List<Document>>() {
                        @Override
                        public void onComplete(@NonNull Task<List<Document>> task) {
                            if (task.isSuccessful()) {
                                Log.d("STITCH", "Found docs: " + task.getResult().toString());
                                return;
                            }
                            Log.e("STITCH", "Error: " + task.getException().toString());
                            task.getException().printStackTrace();
                        }
                    });
 */
