package net.teerapap.whatnext.service;

import net.teerapap.whatnext.model.Task;
import net.teerapap.whatnext.model.TaskDbHelper;
import net.teerapap.whatnext.model.When;

import java.util.List;

/**
 * It schedules the tasks based on a condition.
 * It orders the tasks by similarity between the condition and task's condition.
 * Created by teerapap on 10/31/13.
 */
public class SimpleTaskScheduler implements TaskScheduler {

    public SimpleTaskScheduler() {
    }

    @Override
    public void reschedule(When condition) {

    }

    @Override
    public void setNextTaskListener(NextTaskListener listener) {

    }

    @Override
    public void requestNextTask() {

    }

    @Override
    public Task getCurrentTask() {
        return null;
    }

    @Override
    public void addTask(Task task) {

    }

    @Override
    public void removeTask(Task task) {

    }
}
