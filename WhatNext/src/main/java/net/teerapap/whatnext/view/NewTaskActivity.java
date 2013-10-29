package net.teerapap.whatnext.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.teerapap.whatnext.R;
import net.teerapap.whatnext.model.Task;
import net.teerapap.whatnext.model.WhenCondition;
import net.teerapap.whatnext.service.TaskService;

public class NewTaskActivity extends Activity {

    private EditText mTaskTitleEdit;
    private WhenConditionViewGroup mWhenCondViewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        // Show the Up button in the action bar.
        setupActionBar();

        mTaskTitleEdit = (EditText) findViewById(R.id.task_title_edit);
        mWhenCondViewGroup = new WhenConditionViewGroup(this);

        // When click add new task button
        Button addNewTaskBtn = (Button) findViewById(R.id.add_task_btn);
        addNewTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewTask();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Focus on task title edit text
        mTaskTitleEdit.requestFocus();
        mTaskTitleEdit.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Show soft keyboard
                InputMethodManager keyboard = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(mTaskTitleEdit, 0);
            }
        }, 200);
    }

    /**
     * Set up the {@link android.app.ActionBar}.
     */
    private void setupActionBar() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void addNewTask() {
        String title = mTaskTitleEdit.getText().toString().trim();

        // Validate title
        if (title.length() < 1) {
            // Show toast message
            Toast.makeText(getApplicationContext(), "Title is too short.", Toast.LENGTH_SHORT)
                 .show();
            return;
        }

        // Construct Task object
        WhenCondition when = mWhenCondViewGroup.getCondition();
        Task task = new Task(title, when);

        // Add task to TaskService
        TaskService.getInstance(getApplicationContext()).addTask(task);

        // Clear text box
        clearTitleEditText();
    }

    private void clearTitleEditText() {
        mTaskTitleEdit.setText("");
    }

}
