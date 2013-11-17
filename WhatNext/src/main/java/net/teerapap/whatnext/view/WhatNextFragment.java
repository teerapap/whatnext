package net.teerapap.whatnext.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import net.teerapap.whatnext.R;
import net.teerapap.whatnext.model.Task;
import net.teerapap.whatnext.model.WhenCondition;
import net.teerapap.whatnext.service.TaskSchedulingListener;
import net.teerapap.whatnext.service.TaskService;

import java.util.Date;

/**
 * Fragment for WhatNext page. It is the main fragment for the application.
 * Created by teerapap on 10/21/13.
 */
public class WhatNextFragment extends Fragment implements TaskSchedulingListener, TaskService.TaskDoneCallback, TaskService.TaskDeletedCallback {

    private static String TAG = "WhatNextFragment";

    private Task mCurrentTask;

    private Button mClearBtn;
    private Button mNextTaskBtn;
    private Button mDoneBtn;
    private TextView mTaskTitleText;
    private WhenConditionViewGroup mWhenCondViewGroup;
    private TextView mLastDoneTaskText;
    private TextView mLastDoneTaskTime;

    private TaskService mTaskService;

    private boolean mDoneBtnLongClickSuccess;

    /**
     * Static method to initialize the fragment
     *
     * @return
     */
    public static WhatNextFragment newInstance() {
        WhatNextFragment f = new WhatNextFragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initMemberViews();
        initTaskService();
    }

    @Override
    public void onStart() {
        super.onStart();

        // Request data to show
        mTaskService.requestLastDoneTask(this);
        mTaskService.requestCurrentScheduledTask(this);
        // Register all event listeners
        setUpEventListeners(true);
    }

    @Override
    public void onStop() {
        super.onStop();

        // Unregister all event listeners
        setUpEventListeners(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_whatnext, container, false);
        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action buttons
        switch (item.getItemId()) {
            case R.id.action_delete_task:
                deleteCurrentTask();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Initialize member variables from children views.
     */
    private void initMemberViews() {
        Activity activity = getActivity();
        mNextTaskBtn = (Button) activity.findViewById(R.id.next_btn);
        mDoneBtn = (Button) activity.findViewById(R.id.done_btn);
        mTaskTitleText = (TextView) activity.findViewById(R.id.task_title);
        mWhenCondViewGroup = new WhenConditionViewGroup(activity);
        mClearBtn = (Button) activity.findViewById(R.id.clear_btn);
        mLastDoneTaskText = (TextView) activity.findViewById(R.id.last_done_task_title);
        mLastDoneTaskTime = (TextView) activity.findViewById(R.id.last_done_time);
    }

    /**
     * Initialize {@link net.teerapap.whatnext.service.TaskService}
     */
    private void initTaskService() {
        // Initialize TaskService
        mTaskService = TaskService.getInstance(getActivity().getApplicationContext());
    }

    /**
     * Setup proper event listeners inside fragment
     *
     * @param register register if true. unregister if false.
     */
    private void setUpEventListeners(boolean register) {
        if (register) {
            // Listen for next task scheduled
            mTaskService.setTaskSchedulingListener(this);

            mClearBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mWhenCondViewGroup.resetToggleButtons();
                }
            });
            mNextTaskBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nextTask();
                }
            });
            mDoneBtn.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // Handle logic about long click.
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        mDoneBtnLongClickSuccess = false;
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (!mDoneBtnLongClickSuccess) {
                            // Show dialog
                            Toast.makeText(getActivity().getApplicationContext(), "Press long to mark done.", Toast.LENGTH_SHORT)
                                 .show();
                        }
                    }
                    return false;
                }
            });
            mDoneBtn.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    markTaskDone();
                    mDoneBtnLongClickSuccess = true;
                    return true;
                }
            });

            mWhenCondViewGroup.setOnConditionChanged(new WhenConditionViewGroup.OnConditionChangeListener() {
                @Override
                public void onConditionChanged(WhenCondition currentCondition) {
                    mTaskService.rescheduleTasks(currentCondition);
                }
            });
        } else {
            mTaskService.setTaskSchedulingListener(null);
            mClearBtn.setOnClickListener(null);
            mNextTaskBtn.setOnClickListener(null);
            mDoneBtn.setOnTouchListener(null);
            mDoneBtn.setOnLongClickListener(null);
            mWhenCondViewGroup.setOnConditionChanged(null);
        }
    }

    private void nextTask() {
        // Request for next task
        mTaskService.requestNextTask();
    }

    private void markTaskDone() {
        mTaskService.markTaskDone(mCurrentTask, this);
    }

    private void deleteCurrentTask() {
        if (mCurrentTask == null) {
            return;
        }

        DialogFragment confirmDialog = new DialogFragment() {
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                // Build the dialog and set up the button click handlers
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(mCurrentTask.getTitle())
                       .setTitle(R.string.delete_task_confirm_title)
                       .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int id) {
                               mTaskService.deleteTask(mCurrentTask, WhatNextFragment.this);
                           }
                       })
                       .setNegativeButton(android.R.string.no, null);
                return builder.create();
            }
        };
        confirmDialog.show(getFragmentManager(), "confirm_delete");
    }

    @Override
    public void onTaskScheduled(final Task task) {
        final String title = (task != null) ? task.getTitle() : getString(R.string.no_task);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCurrentTask = task;
                mTaskTitleText.setText(title);
            }
        });
    }

    @Override
    public void onError(Error error) {
        Log.e(TAG, "Error on next task", error);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity().getApplicationContext(), "Error occurred", Toast.LENGTH_SHORT)
                     .show();
            }
        });
    }

    @Override
    public void onNoTask() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // TODO: Implement this properly, turn off done and action button.
                mCurrentTask = null;
                mTaskTitleText.setText(getString(R.string.no_task));
                Toast.makeText(getActivity().getApplicationContext(), "Hooray! No tasks left.", Toast.LENGTH_SHORT)
                     .show();
            }
        });
    }

    @Override
    public void onTaskDone(final Task t, Date doneTime) {
        // Convert to relative human-readable time
        final CharSequence period;
        long doneMillis = doneTime.getTime();
        long currentMillis = System.currentTimeMillis();
        if (currentMillis - doneMillis < DateUtils.SECOND_IN_MILLIS) {
            // To avoid sub-second period
            period = "just now";
        } else {
            period = DateUtils.getRelativeTimeSpanString(doneMillis, currentMillis, DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE);
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLastDoneTaskTime.setText(period + " | ");
                mLastDoneTaskText.setText(t.getTitle());
            }
        });
    }

    @Override
    public void onTaskDeleted(final Task t) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity().getApplicationContext(), "\"" + t.getTitle() + "\" deleted", Toast.LENGTH_SHORT)
                     .show();
            }
        });
    }
}
