package net.teerapap.whatnext.view;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ToggleButton;

import net.teerapap.whatnext.R;

/**
 * Fragment for WhatNext page. It is the main fragment for the application.
 * Created by teerapap on 10/21/13.
 */
public class WhatNextFragment extends Fragment {

    private Button mClearBtn;
    private ToggleButton mWhenHomeToggleBtn;
    private ToggleButton mWhenFreeToggleBtn;
    private ToggleButton mWhenWorkToggleBtn;
    private ToggleButton mWhenShoppingToggleBtn;

    /**
     * Static method to initialize the fragment
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
        mClearBtn = (Button) activity.findViewById(R.id.clear_btn);
        mWhenHomeToggleBtn = (ToggleButton) activity.findViewById(R.id.when_home_toggle);
        mWhenFreeToggleBtn = (ToggleButton) activity.findViewById(R.id.when_free_toggle);
        mWhenWorkToggleBtn = (ToggleButton) activity.findViewById(R.id.when_work_toggle);
        mWhenShoppingToggleBtn = (ToggleButton) activity.findViewById(R.id.when_shopping_toggle);
    }

    /**
     * Setup proper event listeners inside fragment
     */
    private void setUpEventListeners() {
        mClearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetWhenToggleBtns();
            }
        });
    }

    /**
     * Reset when toggle buttons
     */
    public void resetWhenToggleBtns() {
        mWhenHomeToggleBtn.setChecked(false);
        mWhenFreeToggleBtn.setChecked(false);
        mWhenWorkToggleBtn.setChecked(false);
        mWhenShoppingToggleBtn.setChecked(false);
    }
}
