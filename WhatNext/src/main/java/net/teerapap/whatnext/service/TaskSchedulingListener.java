package net.teerapap.whatnext.service;

import net.teerapap.whatnext.model.Task;

/**
 * Use this listener to listen for task scheduling in {@link net.teerapap.whatnext.service.TaskService}.
 * Created by teerapap on 10/26/13.
 */
public interface TaskSchedulingListener {

    /**
     * Get called when new task get scheduled now.
     *
     * @param task
     */
    void onTaskScheduled(Task task);

    /**
     * Get called when encounter an error.
     *
     * @param error
     */
    void onError(Error error);

    /**
     * Get called when no task found.
     */
    void onNoTask();

}
