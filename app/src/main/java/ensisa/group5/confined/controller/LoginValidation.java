package ensisa.group5.confined.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.auth.StitchUser;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.auth.providers.userpassword.UserPasswordCredential;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
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
    /*
     *Retourne un task qui contient une liste de documents
     * Les Documents contiennent les JSON des informations des tâches assignées à l'utilisateur connecté. Utile pour l'onglet " Mes tâches "
     * Les tâches peuvent être celles en cours ou celles déjà terminées.
     */
    public RemoteFindIterable<Document> getTasksByUser() {
        try {
             RemoteMongoClient remoteMongoClient = Stitch.getDefaultAppClient().getServiceClient(RemoteMongoClient.factory, "Mongo-Confined");
            RemoteMongoCollection<Document> collection = remoteMongoClient.getDatabase("Confined_Project").getCollection("Tasks");
            Log.d("stitch", "Récupération des tâches d'un utilisateur");
            StitchUser user = Stitch.getDefaultAppClient().getAuth().getUser();
            if (user != null) {
                return collection.find(eq("user_id", user.getId()));
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    /*
     *Retourne un task qui contient une liste de documents
     * Les Documents contiennent les JSON des informations des tâches non assignées. Utile pour que les utilisateurs puissent les choisir
     */
    public RemoteFindIterable<Document>  getNonAssignedTasks() {
        try {
             RemoteMongoClient remoteMongoClient = Stitch.getDefaultAppClient().getServiceClient(RemoteMongoClient.factory, "Mongo-Confined");
            RemoteMongoCollection<Document> collection = remoteMongoClient.getDatabase("Confined_Project").getCollection("Tasks");
            Log.d("stitch", "Récupération des tâches non assignées");
            List<Document> docs = new ArrayList<Document>();
            return collection.find(eq("task_status", 0 ));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    /*
     *Retourne un task qui contient une liste de documents
     * Les Documents contiennent les JSON des informations des utilisateurs. Utile pour récupérer tous les scores
     */
    public RemoteFindIterable<Document> getLeaderBoard() {

            Log.d("stitch", "Récupération des utilisateurs pour afficher leurs scores");
            RemoteMongoClient remoteMongoClient = Stitch.getDefaultAppClient().getServiceClient(RemoteMongoClient.factory, "Mongo-Confined");
            Log.d("stitch", "okokokok");
            RemoteMongoCollection<Document> collection = remoteMongoClient.getDatabase("Confined_Project").getCollection("Users_data");
            Log.d("stitch", "Récupération des utilisateurs pour afficher leurs scores");

            return collection.find();

    }


    /*
    *Retourne un task qui contient un document
    * Le Document contient je JSON des informations de l'utilisateur connecté à l'application
     */
   /* public Task<Document> getUserInfo() {
        try {
            final RemoteMongoClient remoteMongoClient = Stitch.getDefaultAppClient().getServiceClient(RemoteMongoClient.factory, "Mongo-Confined");
            RemoteMongoCollection<Document> collection = remoteMongoClient.getDatabase("Confined_Project").getCollection("Users_data");
            Log.d("stitch", "Récupération des utilisateurs pour afficher leurs scores");
            StitchUser user = Stitch.getDefaultAppClient().getAuth().getUser();
            List<Document> docs = new ArrayList<>();
            return collection.findOne(new Document("user_id", user.getId()));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    */

    public void assignTaskToUser(){

    }
    public void createTask(){

    }

    public void deleteTask() {

    }
    public void removeTaskFromUser(){

    }
    /*
     *Retourne un un booléen
     * Le boolean indique si les credentials peuvent connecter l'utilisateur
     */

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
    /*
     *Retourne un un booléen
     * Le boolean indique si l'utilisateur a bien été crée
     */
    public boolean createUser() {
        return true;
    }

    @Override
    public void execute(Runnable command) {

    }
}
