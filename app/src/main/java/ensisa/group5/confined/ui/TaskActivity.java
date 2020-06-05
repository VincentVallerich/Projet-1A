package ensisa.group5.confined.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ensisa.group5.confined.R;
import ensisa.group5.confined.controller.MainActivity;

public class TaskActivity extends AppCompatActivity implements View.OnClickListener
{
    private TaskActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_activity);

        activity = this;

        ImageButton taskButton = findViewById(R.id.task_button);
        taskButton.setOnClickListener(this);

        ImageButton leaderboardButton = findViewById(R.id.leaderboard_button);
        leaderboardButton.setOnClickListener(this);

        ImageButton profileButton = findViewById(R.id.profile_button);
        profileButton.setOnClickListener(this);

        /*  // Get a remote client
                           final RemoteMongoClient remoteMongoClient =
                                   Stitch.getDefaultAppClient().getServiceClient(RemoteMongoClient.factory, "Mongo-Confined");
                           RemoteMongoCollection<Document> collection = remoteMongoClient.getDatabase("Confined_Project").getCollection("Users_Score");
                           StitchUser user = Stitch.getDefaultAppClient().getAuth().getUser();
                           final Task <Document> findOneAndUpdateTask =  collection.findOne(eq("_id",new ObjectId(user.getId())));
                           findOneAndUpdateTask.addOnCompleteListener(new OnCompleteListener <Document> () {
                               @Override
                               public void onComplete(@NonNull Task <Document> task) {
                                   if (task.getResult() == null) {
                                       Log.d("app", String.format("No document matches the provided query"));
                                   }
                                   else if (task.isSuccessful()) {
                                       Log.d("app", String.format("Successfully found document: %s", task.getResult()));
                                       try {
                                           JSONObject obj = new JSONObject(task.getResult().toJson());
                                           String score = obj.getString("score");
                                           Log.d("app",user.getProfile().getEmail());
                                           Log.d("app",user.getId());
                                           Log.d("app",score);
                                       } catch (JSONException e) {
                                           e.printStackTrace();
                                       }

                                   } else {
                                       Log.e("app", "Failed to findOne: ", task.getException());
                                   }
                               }
                           });

                           */

    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.task_button:
                Toast.makeText(activity, "Task!", Toast.LENGTH_SHORT).show();
             //   Intent intent = new Intent(this, MainActivity.class);
               // startActivity(intent);
                break;
            case R.id.leaderboard_button:
                Toast.makeText(activity, "Leaderboard!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.profile_button:
                Toast.makeText(activity, "Profile!", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}

