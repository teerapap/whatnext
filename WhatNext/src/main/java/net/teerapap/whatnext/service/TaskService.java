package net.teerapap.whatnext.service;

import net.teerapap.whatnext.model.Task;
import net.teerapap.whatnext.model.When;

/**
 * Main task service which is responsible for getting next task
 * and adding new tasks.
 * Created by teerapap on 10/26/13.
 */
public class TaskService {

    private static TaskService sTaskService;

    private NextTaskListener mNextTaskListener;

    public static TaskService getInstance() {
        if (sTaskService == null) {
            sTaskService = new TaskService();
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
     * Saves new task.
     * @param task
     */
    public void addTask(Task task) {
        // TODO: Implement this properly
        mNextTaskListener.onNextTask(task);
    }

}
