package net.teerapap.whatnext;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class NewTaskActivity extends Activity {

    private EditText mTaskTitleEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        // Show the Up button in the action bar.
        setupActionBar();

        mTaskTitleEdit = (EditText) findViewById(R.id.task_title_edit);
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
        },200);
    }

    /**
     * Set up the {@link android.app.ActionBar}.
     */
    private void setupActionBar() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
