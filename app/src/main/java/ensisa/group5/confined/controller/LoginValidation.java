package ensisa.group5.confined.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.common.util.Hex;
import com.google.android.gms.tasks.Task;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.auth.StitchUser;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.auth.providers.userpassword.UserPasswordCredential;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteDeleteResult;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteInsertOneResult;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateResult;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
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
     * Les Documents contiennent les JSON des informations des tâches non assignées ( task_status = 0 ). Utile pour que les utilisateurs puissent les choisir
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
            RemoteMongoCollection<Document> collection = remoteMongoClient.getDatabase("Confined_Project").getCollection("Users_data");


            return collection.find();

    }
    /*
    *Retourne un task qui contient un document
    * Le Document contient je JSON des informations de l'utilisateur connecté à l'application
     */
    public Task<Document> getUserInfo() {
        Log.d("stitch", "Récupération des utilisateurs pour afficher leurs scores");
        RemoteMongoClient remoteMongoClient = Stitch.getDefaultAppClient().getServiceClient(RemoteMongoClient.factory, "Mongo-Confined");
        RemoteMongoCollection<Document> collection = remoteMongoClient.getDatabase("Confined_Project").getCollection("Users_data");
        StitchUser user = Stitch.getDefaultAppClient().getAuth().getUser();
        return collection.findOne(new Document("_id",new ObjectId("5eda3134355c4069c9c2ab49")));

    }

    public  Task<RemoteUpdateResult> startTask(String taskid){

        RemoteMongoClient remoteMongoClient = Stitch.getDefaultAppClient().getServiceClient(RemoteMongoClient.factory, "Mongo-Confined");
        RemoteMongoCollection<Document> collection = remoteMongoClient.getDatabase("Confined_Project").getCollection("Tasks");
        final Document filterDoc = new Document( "_id", new ObjectId(taskid));
        StitchUser user = Stitch.getDefaultAppClient().getAuth().getUser();
        Document updateDoc = new Document().append("$set",new Document().append("task_status", 1)).append("user_id",user.getId());
        return collection.updateOne(filterDoc, updateDoc);

    }
    public Task <RemoteInsertOneResult> createTask(Task task){
        RemoteMongoClient remoteMongoClient = Stitch.getDefaultAppClient().getServiceClient(RemoteMongoClient.factory, "Mongo-Confined");
        RemoteMongoCollection<Document> collection = remoteMongoClient.getDatabase("Confined_Project").getCollection("Tasks");
        Document newItem = new Document()
                .append("task_name", "tâche test")
                .append("task_status",0 )
                .append("task_priority", 5)
                .append("task_desc", "blablablalzla test wsh")
                .append("task_score",5);
        return collection.insertOne(newItem);

    }
    public Task<RemoteUpdateResult> finishTask(String taskid){
        RemoteMongoClient remoteMongoClient = Stitch.getDefaultAppClient().getServiceClient(RemoteMongoClient.factory, "Mongo-Confined");
        RemoteMongoCollection<Document> collection = remoteMongoClient.getDatabase("Confined_Project").getCollection("Tasks");
        final Document filterDoc = new Document( "_id", new ObjectId(taskid));
        Document updateDoc = new Document().append("$set",new Document().append("task_status", 2));
        return collection.updateOne(filterDoc, updateDoc);
    }

    /*
    * SEULEUMENT SI LA TACHE ETAIT EN COURS ! ( si le status de la tache est de 1 ) on peut pas abandonner une tâche terminée
     */
    public Task<RemoteUpdateResult> abandonTask(String taskid) {
        RemoteMongoClient remoteMongoClient = Stitch.getDefaultAppClient().getServiceClient(RemoteMongoClient.factory, "Mongo-Confined");
        RemoteMongoCollection<Document> collection = remoteMongoClient.getDatabase("Confined_Project").getCollection("Tasks");
        final Document filterDoc = new Document( "_id", new ObjectId(taskid));
        Document updateDoc = new Document().append("$set",new Document().append("task_status", 0)).append("user_id","");;
        return collection.updateOne(filterDoc, updateDoc);

    }
    public Task<RemoteDeleteResult> deleteTask(String taskid){
        RemoteMongoClient remoteMongoClient = Stitch.getDefaultAppClient().getServiceClient(RemoteMongoClient.factory, "Mongo-Confined");
        RemoteMongoCollection<Document> collection = remoteMongoClient.getDatabase("Confined_Project").getCollection("Tasks");
        final Document filterDoc = new Document( "_id", new ObjectId(taskid));
        return collection.deleteOne(filterDoc);

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
