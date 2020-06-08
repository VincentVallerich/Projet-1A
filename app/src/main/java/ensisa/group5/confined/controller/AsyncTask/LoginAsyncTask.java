package ensisa.group5.confined.controller.AsyncTask;

import android.os.AsyncTask;

import ensisa.group5.confined.controller.DataBase;

/**
 * Author VALLERICH Vincent on 06-06-2020
 */

public class LoginAsyncTask extends AsyncTask<Void,Void,Boolean> {

    @Override
    protected Boolean doInBackground(Void... voids) {
        DataBase db = new DataBase();
        Thread thread = new Thread(db);
        thread.start();
        return false;
    }
}
