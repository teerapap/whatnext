package net.teerapap.whatnext.service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import net.teerapap.whatnext.model.Task;
import net.teerapap.whatnext.model.TaskDbHelper;
import net.teerapap.whatnext.model.When;

/**
 * Main task service which is responsible for getting next task
 * and adding new tasks.
 * Created by teerapap on 10/26/13.
 */
public class TaskService {

    private static final String TAG = "TaskService";

    private static TaskService sTaskService;

    private NextTaskListener mNextTaskListener;
    private TaskDbHelper mTaskDbHelper;

    private TaskService(Context context) {
        mTaskDbHelper = new TaskDbHelper(context);
    }

    public static TaskService getInstance(Context context) {
        if (sTaskService == null) {
            sTaskService = new TaskService(context);
        }
        return sTaskService;
    }

    public void setNextTaskListener(NextTaskListener listener) {
        mNextTaskListener = listener;
    }

    /**
     * Asynchronously find next task suitable for when condition.
     * When it finishes its operation, it will call an appropriate method of {@link net.teerapap.whatnext.service.NextTaskListener}.
     */
    public void requestNextTask(When condition) {
        // TODO: Implement this properly
        if (mNextTaskListener != null) {
            mNextTaskListener.onNoMoreTask();
        }
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

        }.execute(task);
    }

}
