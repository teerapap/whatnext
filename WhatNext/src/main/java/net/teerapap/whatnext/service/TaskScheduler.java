package net.teerapap.whatnext.service;

import net.teerapap.whatnext.model.Task;
import net.teerapap.whatnext.model.When;

/**
 * It schedules tasks based on some behaviors or conditions.
 * When it schedules new task, it calls {@link net.teerapap.whatnext.service.NextTaskListener} methods.
 * Created by teerapap on 10/31/13.
 */
public interface TaskScheduler {

    /**
     * Reschedule with new condition
     * @param condition
     */
    void reschedule(When condition);

    /**
     * Set next task listener.
     * @param listener
     */
    void setNextTaskListener(NextTaskListener listener);

    /**
     * Next task explicitly.
     * When it finishes its operation, it will call an appropriate method of {@link net.teerapap.whatnext.service.NextTaskListener}.
     */
    Task nextTask();

    /**
     * Get current doing task
     * @return a task. null means no tasks.
     */
    Task getCurrentTask();

    /**
     * Add new task to the scheduler.
     * @param task
     */
    void addTask(Task task);

    /**
     * Remove task from the scheduler.
     * @param task
     */
    void removeTask(Task task);

}
