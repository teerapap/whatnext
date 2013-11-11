package net.teerapap.whatnext.service;

import net.teerapap.whatnext.model.Task;
import net.teerapap.whatnext.model.When;

/**
 * It schedules tasks based on some behaviors or conditions.
 * When it schedules new task, it calls {@link TaskSchedulingListener} methods.
 * Created by teerapap on 10/31/13.
 */
public interface TaskScheduler {

    /**
     * Reschedule with new condition.
     *
     * @param condition
     */
    void reschedule(When condition);

    /**
     * Request next task to be scheduled.
     *
     * @return next scheduled task. Null means no task.
     */
    void requestNextTask();

    /**
     * Get current doing task
     *
     * @return a task. null means no tasks.
     */
    Task getCurrentTask();

    /**
     * Add new task to the scheduler.
     *
     * @param task
     */
    void addTask(Task task);

    /**
     * Remove task from the scheduler.
     *
     * @param task
     */
    void removeTask(Task task);

    /**
     * Register a listener which will be invoked on scheduling events.
     *
     * @param listener
     */
    void setTaskSchedulingListener(TaskSchedulingListener listener);
}
