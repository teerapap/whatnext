package net.teerapap.whatnext.service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import net.teerapap.whatnext.model.Task;
import net.teerapap.whatnext.model.TaskDbHelper;
import net.teerapap.whatnext.model.When;
import net.teerapap.whatnext.model.WhenCondition;

import java.util.AbstractMap;
import java.util.Date;
import java.util.List;

import static android.os.AsyncTask.SERIAL_EXECUTOR;

/**
 * Main task service which is responsible for getting next task
 * and adding new tasks.
 * <p/>
 * Most of methods in this class must be called in UI thread.
 * <p/>
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
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                List<Task> tasks = mTaskDbHelper.listOngoingTasks();
                for (Task t : tasks) {
                    mTaskScheduler.addTask(t);
                }
                return null;
            }

        }.executeOnExecutor(SERIAL_EXECUTOR, null);
    }

    public void setTaskSchedulingListener(TaskSchedulingListener listener) {
        mTaskScheduler.setTaskSchedulingListener(listener);
    }

    /**
     * Reschedule tasks based on new condition.
     *
     * @param when
     */
    public void rescheduleTasks(WhenCondition when) {
        new AsyncTask<When, Void, Void>() {
            @Override
            protected Void doInBackground(When... params) {
                mTaskScheduler.reschedule(params[0]);
                return null;
            }
        }.executeOnExecutor(SERIAL_EXECUTOR, when);
    }

    /**
     * Asynchronously find next task suitable for when condition. Must be called in UI thread.
     * When it finishes its operation, it will call an appropriate method of {@link TaskSchedulingListener}
     * (not guarantee to call on UI thread).
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
                        mTaskScheduler.addTask(t);

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

    public void requestLastDoneTask(TaskDoneCallback c) {
        new AsyncTask<TaskDoneCallback, Void, Void>() {

            @Override
            protected Void doInBackground(TaskDoneCallback... callbacks) {
                AbstractMap.SimpleImmutableEntry<Task, Date> o = mTaskDbHelper.getLastDoneTaskAndTime();
                if (o != null) {
                    for (TaskDoneCallback c : callbacks) {
                        c.onTaskDone(o.getKey(), o.getValue());
                    }
                }
                return null;
            }
        }.executeOnExecutor(SERIAL_EXECUTOR, c);
    }

    public void requestCurrentScheduledTask(TaskSchedulingListener listener) {
        new AsyncTask<TaskSchedulingListener, Void, Void>() {

            @Override
            protected Void doInBackground(TaskSchedulingListener... listeners) {
                Task t = mTaskScheduler.getCurrentTask();
                for (TaskSchedulingListener l : listeners) {
                    l.onTaskScheduled(t);
                }
                return null;
            }
        }.executeOnExecutor(SERIAL_EXECUTOR, listener);
    }

    public void markTaskDone(final Task task, TaskDoneCallback taskDoneCallback) {
        if (task == null) {
            mTaskScheduler.requestNextTask();
            return;
        }

        new AsyncTask<TaskDoneCallback, Void, Void>() {

            @Override
            protected Void doInBackground(TaskDoneCallback... callbacks) {
                Date dt = mTaskDbHelper.markTaskDone(task);
                for (TaskDoneCallback c : callbacks) {
                    c.onTaskDone(task, dt);
                }
                mTaskScheduler.removeTask(task);
                return null;
            }
        }.executeOnExecutor(SERIAL_EXECUTOR, taskDoneCallback);
    }

    public void deleteTask(final Task task, TaskDeletedCallback taskDeletedCallback) {
        if (task == null) {
            mTaskScheduler.requestNextTask();
            return;
        }

        new AsyncTask<TaskDeletedCallback, Void, Void>() {

            @Override
            protected Void doInBackground(TaskDeletedCallback... callbacks) {
                mTaskDbHelper.deleteTask(task);
                for (TaskDeletedCallback c : callbacks) {
                    c.onTaskDeleted(task);
                }
                mTaskScheduler.removeTask(task);
                return null;
            }

        }.executeOnExecutor(SERIAL_EXECUTOR, taskDeletedCallback);
    }

    public static interface TaskDoneCallback {

        void onTaskDone(Task t, Date doneTime);

    }

    public static interface TaskDeletedCallback {

        void onTaskDeleted(Task t);

    }
}
