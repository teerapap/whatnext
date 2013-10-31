package net.teerapap.whatnext.service;

import android.content.Context;
import android.os.AsyncTask;
import static android.os.AsyncTask.SERIAL_EXECUTOR;
import android.util.Log;

import net.teerapap.whatnext.model.Task;
import net.teerapap.whatnext.model.TaskDbHelper;
import net.teerapap.whatnext.model.When;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * Main task service which is responsible for getting next task
 * and adding new tasks.
 *
 * Most of methods in this class must be called in UI thread.
 *
 * Created by teerapap on 10/26/13.
 */
public class TaskService {

    private static final String TAG = "TaskService";

    private static TaskService sTaskService;

    private TaskDbHelper mTaskDbHelper;
    private TaskScheduler mTaskScheduler;


    private TaskService(Context context) {
        mTaskDbHelper = new TaskDbHelper(context);
        mTaskScheduler = new SimpleTaskScheduler();
    }

    public static TaskService getInstance(Context context) {
        if (sTaskService == null) {
            sTaskService = new TaskService(context);
            sTaskService.reloadOngoingTasks();
        }
        return sTaskService;
    }

    /**
     * Reload ongoing tasks to database. Called once only on initialization.
     */
    private void reloadOngoingTasks() {
        new AsyncTask<Void, Void, List<Task>>() {

            @Override
            protected List<Task> doInBackground(Void... params) {
                return mTaskDbHelper.listOngoingTasks();
            }

            @Override
            protected void onPostExecute(List<Task> tasks) {
                for(Task t : tasks) {
                    mTaskScheduler.addTask(t);
                }

            }
        }.executeOnExecutor(SERIAL_EXECUTOR, null);
    }

    public void setNextTaskListener(NextTaskListener listener) {
        mTaskScheduler.setNextTaskListener(listener);
    }

    /**
     * Asynchronously find next task suitable for when condition. Must be called in UI thread.
     * When it finishes its operation, it will call an appropriate method of {@link net.teerapap.whatnext.service.NextTaskListener}.
     */
    public void requestNextTask() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                mTaskScheduler.requestNextTask();
                return null;
            }
        }.executeOnExecutor(SERIAL_EXECUTOR, null);
    }

    /**
     * Saves new task. Must be called in UI thread.
     *
     * @param task
     */
    public void addTask(Task task) {
        // Do adding new task asynchronously
        new AsyncTask<Task, Task, Void>() {

            @Override
            protected Void doInBackground(Task... tasks) {
                for (Task t : tasks) {
                    try {
                        // Add task
                        mTaskDbHelper.addTask(t);

                        // TODO: Print info log here.

                        // Publish added task
                        publishProgress(t);
                    } catch (Exception e) {
                        // TODO: Find a way to tell user.
                        Log.e(TAG, "Cannot add task title: " + t.getTitle(), e);
                    }
                    if (isCancelled()) break;
                }
                return null;
            }

        }.executeOnExecutor(SERIAL_EXECUTOR, task);
    }

}
