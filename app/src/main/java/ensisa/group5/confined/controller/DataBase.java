package ensisa.group5.confined.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.core.auth.StitchUser;
import com.mongodb.stitch.android.core.auth.providers.userpassword.UserPasswordAuthProviderClient;
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
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;

import ensisa.group5.confined.R;
import ensisa.group5.confined.model.CTask;

import static com.mongodb.client.model.Filters.eq;

public class DataBase implements Executor {

    private Context context;
    private static SharedPreferences preferences;
    private StitchAppClient client = null;

    public static final int MIN_LEN_INPUT_USERNAME = 2;
    public static final int MIN_LEN_INPUT_PASSWORD = 6;
    private String usernameKey;
    private String mailKey;
    public static final String serviceName = "Mongo-Confined";
    public static final String databaseName = "Confined_Project";
    public static final String collectionNameTasks = "Tasks";
    public static final String collectionNameUsersData = "Users_data";
    public static final String clientAppId = "apptest-vzuxl";
    public static final String field_id = "user_id";
    public static final String field_task_status = "task_status";
    public static final String field_task_name = "task_name";
    public static final String field_task_priority = "task_priority";
    public static final String field_task_description = "task_description";
    public static final String field_task_score = "task_score";
    public static final String field_user_score = "score";
    public static final String field_user_pseudo = "pseudo";
    public static final String field_user_master = "master";

    public static final int DEFAULT_SCORE = 0;


    public DataBase() {}

    /**
     * @param context
     * @param preferences
     */
    public DataBase(Context context, SharedPreferences preferences) {
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
            RemoteMongoClient remoteMongoClient = Stitch.getDefaultAppClient().getServiceClient(RemoteMongoClient.factory, serviceName);
            RemoteMongoCollection<Document> collection = remoteMongoClient.getDatabase(databaseName).getCollection(collectionNameTasks);
            Log.d("stitch", "Récupération des tâches d'un utilisateur");
            StitchUser user = Stitch.getDefaultAppClient().getAuth().getUser();
            if (user != null) {
                return collection.find(eq(field_id, user.getId()));
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
            RemoteMongoClient remoteMongoClient = Stitch.getDefaultAppClient().getServiceClient(RemoteMongoClient.factory, serviceName);
            RemoteMongoCollection<Document> collection = remoteMongoClient.getDatabase(databaseName).getCollection(collectionNameTasks);
            Log.d("stitch", "Récupération des tâches non assignées");
            List<Document> docs = new ArrayList<Document>();
            return collection.find(eq(field_task_status, CTask.State.NON_ATTRIBUATE.toString()));
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
        RemoteMongoClient remoteMongoClient = Stitch.getDefaultAppClient().getServiceClient(RemoteMongoClient.factory, serviceName);
        RemoteMongoCollection<Document> collection = remoteMongoClient.getDatabase(databaseName).getCollection(collectionNameUsersData);

        return collection.find();
    }
    /*
     *Retourne un task qui contient un document
     * Le Document contient le JSON des informations de l'utilisateur connecté à l'application
     */
   /* public Task<Document> getUserInfo() {
        try {
            final RemoteMongoClient remoteMongoClient = Stitch.getDefaultAppClient().getServiceClient(RemoteMongoClient.factory, "Mongo-Confined");
            RemoteMongoCollection<Document> collection = remoteMongoClient.getDatabase(databaseName).getCollection("Users_data");
            Log.d("stitch", "Récupération des utilisateurs pour afficher leurs scores");
            StitchUser user = Stitch.getDefaultAppClient().getAuth().getUser();
            return collection.findOne(new Document("_id", new ObjectId (user.getId())));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    */

    public  Task<RemoteUpdateResult> startTask(String taskid){
        RemoteMongoClient remoteMongoClient = Stitch.getDefaultAppClient().getServiceClient(RemoteMongoClient.factory, serviceName);
        RemoteMongoCollection<Document> collection = remoteMongoClient.getDatabase(databaseName).getCollection(collectionNameTasks);
        final Document filterDoc = new Document( "_id", new ObjectId(taskid));
        StitchUser user = Stitch.getDefaultAppClient().getAuth().getUser();
        Document updateDoc = new Document().append("$set",new Document().append(field_task_status, CTask.State.IN_PROGRESS)).append(field_id,user.getId());

        return collection.updateOne(filterDoc, updateDoc);
    }

    /**
     *
     * @param task
     * @return the current task into database
     */
    public Task <RemoteInsertOneResult> createTask(CTask task){
        RemoteMongoClient remoteMongoClient = Stitch.getDefaultAppClient().getServiceClient(RemoteMongoClient.factory, serviceName);
        RemoteMongoCollection<Document> collection = remoteMongoClient.getDatabase(databaseName).getCollection(collectionNameTasks);
        Document newTask = new Document()
                .append(field_task_name, task.getName())
                .append(field_task_status, task.getState().toString())
                .append(field_task_priority, task.getPriority().toString())
                .append(field_task_description, task.getDescription())
                .append(field_task_score, String.valueOf(task.getPoints()));

        return collection.insertOne(newTask);
    }

    /**
     *
     * @param taskid
     * @return
     */
    public Task<RemoteUpdateResult> finishTask(String taskid){
        RemoteMongoClient remoteMongoClient = Stitch.getDefaultAppClient().getServiceClient(RemoteMongoClient.factory, serviceName);
        RemoteMongoCollection<Document> collection = remoteMongoClient.getDatabase(databaseName).getCollection(collectionNameTasks);
        final Document filterDoc = new Document( "_id", new ObjectId(taskid));
        Document updateDoc = new Document().append("$set",new Document().append(field_task_status, CTask.State.FINISHED.toString()));

        return collection.updateOne(filterDoc, updateDoc);
    }

    /*
     * SEULEUMENT SI LA TACHE ETAIT EN COURS ! ( si le status de la tache est de 1 ) on peut pas abandonner une tâche terminée
     */
    public Task<RemoteUpdateResult> abandonTask(String taskid) {
        RemoteMongoClient remoteMongoClient = Stitch.getDefaultAppClient().getServiceClient(RemoteMongoClient.factory, serviceName);
        RemoteMongoCollection<Document> collection = remoteMongoClient.getDatabase(databaseName).getCollection(collectionNameTasks);
        final Document filterDoc = new Document( "_id", new ObjectId(taskid));
        Document updateDoc = new Document().append("$set",new Document().append(field_task_status, CTask.State.NON_ATTRIBUATE.toString())).append("user_id","");;
        return collection.updateOne(filterDoc, updateDoc);
    }
    public Task<RemoteDeleteResult> deleteTask(String taskid){
        RemoteMongoClient remoteMongoClient = Stitch.getDefaultAppClient().getServiceClient(RemoteMongoClient.factory, serviceName);
        RemoteMongoCollection<Document> collection = remoteMongoClient.getDatabase(databaseName).getCollection(collectionNameTasks);
        final Document filterDoc = new Document( "_id", new ObjectId(taskid));
        return collection.deleteOne(filterDoc);
    }
    /*
     *Retourne un un booléen
     * Le boolean indique si les credentials peuvent connecter l'utilisateur
     */
    public boolean isUserAuthenticated(String email, String password) throws InterruptedException {
        UserPasswordCredential credential = new UserPasswordCredential(email, password );
        if (client == null)
            client = Stitch.initializeDefaultAppClient(clientAppId);
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
        return res;
    }

    public boolean registerUser(String username, String pseudo, String password) {
        AtomicReference<Boolean> res = new AtomicReference<>(false);
        if (client == null)
            client = Stitch.initializeDefaultAppClient(clientAppId);
        UserPasswordAuthProviderClient emailPassClient = Stitch.getDefaultAppClient().getAuth()
                .getProviderClient(UserPasswordAuthProviderClient.factory);

        emailPassClient.registerWithEmail(username, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        res.set(true);
                        try {
                            if (isUserAuthenticated(username, password)) {
                                RemoteMongoClient remoteMongoClient = Stitch.getDefaultAppClient().getServiceClient(RemoteMongoClient.factory, serviceName);
                                RemoteMongoCollection<Document> collection = remoteMongoClient.getDatabase(databaseName).getCollection(collectionNameUsersData);
                                StitchUser user = Stitch.getDefaultAppClient().getAuth().getUser();
                                Document registerUser = new Document("_id", new ObjectId(user.getId()))
                                        .append(field_user_pseudo, pseudo)
                                        .append(field_user_score, DEFAULT_SCORE)
                                        .append(field_user_master, false);

                                collection.insertOne(registerUser);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.d("stitch", "Successfully sent account confirmation email");
                    } else {
                        Log.e("stitch", "Error registering new user:");
                    }
                });
        return res.get();
    }

    /**
     *
     * @param usename
     * @return true if usename length is >= 2 false otherwise
     */
    public boolean isUsernameFormatCorrect(String usename) { return usename.trim().length() >= MIN_LEN_INPUT_USERNAME; }

    /**
     *
     * @param password
     * @return true if password length is >= 6 false otherwise
     */
    public boolean isPasswordFormatCorrect(String password) { return password.trim().length() >= MIN_LEN_INPUT_PASSWORD; }

    /**
     *
     * @param password
     * @param confirm
     * @return true if password and confirm password equals and password length >= 6 false otherwise
     */
    public boolean isPasswordConfirmEquals(String password, String confirm) { return password.equals(confirm) && isPasswordFormatCorrect(password); }

    @Override
    public void execute(Runnable command) {

    }
}
