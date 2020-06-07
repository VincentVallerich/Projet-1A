package ensisa.group5.confined.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.auth.StitchUser;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.auth.providers.userpassword.UserPasswordCredential;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateResult;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import ensisa.group5.confined.R;

import static com.mongodb.client.model.Filters.eq;

/**
 * Author VALLERICH Vincent on 04-06-2020
 */

public class LoginValidation implements Executor {

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
    public Task<List<Document>>  getTasksByUser() throws JSONException {
        try {
            final RemoteMongoClient remoteMongoClient = Stitch.getDefaultAppClient().getServiceClient(RemoteMongoClient.factory, "Mongo-Confined");
            RemoteMongoCollection<Document> collection = remoteMongoClient.getDatabase("Confined_Project").getCollection("Users_data");
            Log.d("stitch", "Récupération des tâches d'un utilisateur");
            StitchUser user = Stitch.getDefaultAppClient().getAuth().getUser();
            Log.d("stitch", user.toString());
            return collection.findOne(eq("_id", new ObjectId(user.getId()))).continueWithTask(new Continuation<Document, Task<Document>>() {
                @Override
                public Task<Document> then(@NonNull Task<Document> task) throws Exception {
                    return task;
                }
            }).continueWithTask(new Continuation<Document, Task<List<Document>>>() {
                @Override
                public Task<List<Document>> then(@NonNull Task<Document> task) throws Exception {
                    if (!task.isSuccessful()) {
                        Log.e("STITCH", "Update failed!");
                        throw task.getException();
                    }
                    List<Document> docs = new ArrayList<>();
                    RemoteMongoCollection<Document> collection2 = remoteMongoClient.getDatabase("Confined_Project").getCollection("Tasks");
                    return collection2
                            .find(new Document("user_id", user.getId()))
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

        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public JSONObject geNotDoneTasks() {

        return null;
    }
    public JSONObject getLeaderBoard() {

        return null;
    }

    public void assignTaskToUser(){

    }
    public void createTask(){

    }

    public void deleteTask() {

    }
    public void removeTaskFromUser(){

    }





    public  boolean isUserAuthenticated(String email, String password) throws InterruptedException {
        UserPasswordCredential credential = new UserPasswordCredential(email, password );
        Stitch.initializeDefaultAppClient("apptest-vzuxl");
        Stitch.getDefaultAppClient().getAuth().loginWithCredential(credential);
        Boolean res = false;
        if (Stitch.getDefaultAppClient().getAuth().isLoggedIn() ) {
            Log.d("stitch","successful login");
            res =true;

        }
        else {
            Log.d("stitch","non successful login");
            res =false;
        }

        Log.d("stitch",String.valueOf(res));
        return res;
    }

    @Override
    public void execute(Runnable command) {

    }
}
