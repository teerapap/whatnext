package net.teerapap.whatnext.view;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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

/**
 * Fragment for WhatNext page. It is the main fragment for the application.
 * Created by teerapap on 10/21/13.
 */
public class WhatNextFragment extends Fragment implements TaskSchedulingListener {

    private static String TAG = "WhatNextFragment";

    private Button mClearBtn;
    private Button mNextTaskBtn;
    private TextView mTaskTitleText;
    private WhenConditionViewGroup mWhenCondViewGroup;

    private TaskService mTaskService;

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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initMemberViews();
        initTaskService();
        setUpEventListeners();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_whatnext, container, false);
        return rootView;
    }

    /**
     * Initialize member variables from children views.
     */
    private void initMemberViews() {
        Activity activity = getActivity();
        mNextTaskBtn = (Button) activity.findViewById(R.id.next_btn);
        mTaskTitleText = (TextView) activity.findViewById(R.id.task_title);
        mWhenCondViewGroup = new WhenConditionViewGroup(activity);
        mClearBtn = (Button) activity.findViewById(R.id.clear_btn);
    }

    /**
     * Initialize {@link net.teerapap.whatnext.service.TaskService}
     */
    private void initTaskService() {
        // Initialize TaskService
        mTaskService = TaskService.getInstance(getActivity().getApplicationContext());

        // Subscribe for next task
        mTaskService.setTaskSchedulingListener(this);
    }

    /**
     * Setup proper event listeners inside fragment
     */
    private void setUpEventListeners() {
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

        mWhenCondViewGroup.onConditionChanged(new WhenConditionViewGroup.OnConditionChangeListener() {
            @Override
            public void onConditionChanged(WhenCondition currentCondition) {
                mTaskService.rescheduleTasks(currentCondition);
            }
        });
    }

    private void nextTask() {
        // Request for next task
        mTaskService.requestNextTask();
    }

    @Override
    public void onTaskScheduled(final Task task) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTaskTitleText.setText(task.getTitle());
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
                Toast.makeText(getActivity().getApplicationContext(), "Hooray! No tasks left.", Toast.LENGTH_SHORT)
                     .show();
            }
        });
    }
}
