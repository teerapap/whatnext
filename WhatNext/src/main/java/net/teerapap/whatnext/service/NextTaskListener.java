package net.teerapap.whatnext.service;

import net.teerapap.whatnext.model.Task;

/**
 * Use this listener to listen for next task after {@link net.teerapap.whatnext.service.TaskService#requestNextTask(net.teerapap.whatnext.model.When)} has been called.
 * Created by teerapap on 10/26/13.
 */
public interface NextTaskListener {

    /**
     * Get called when found next task.
     *
     * @param task
     */
    void onNextTask(Task task);

    /**
     * Get called when encounter an error.
     *
     * @param error
     */
    void onError(Error error);

    /**
     * Get called when no more task found.
     */
    void onNoMoreTask();

}
