package ensisa.group5.confined.model;

import com.google.android.gms.tasks.Task;

import java.util.TreeMap;

import ensisa.group5.confined.controller.DataBase;

/**
 * Author VALLERICH Vincent on 08-06-2020
 */

public class TaskManager {
    TreeMap<Task,User> taskList = new TreeMap<>();
    DataBase dataBase = new DataBase();

    public TaskManager() {
    }
}
