package net.teerapap.whatnext.view;

import android.app.Activity;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import net.teerapap.whatnext.R;
import net.teerapap.whatnext.model.WhenCondition;

/**
 * It wraps the behaviors of when condition views to be reused in many activities and fragments.
 * <p/>
 * Current views must be 4 ToggleButtons with these id.
 * * R.id.when_home_toggle
 * * R.id.when_free_toggle
 * * R.id.when_work_toggle
 * * R.id.when_shopping_toggle
 * <p/>
 * Created by teerapap on 10/29/13.
 */
public class WhenConditionViewGroup {

    public static interface OnConditionChangeListener {
        void onConditionChanged(WhenCondition currentCondition);
    }

    private ToggleButton mWhenHomeToggleBtn;
    private ToggleButton mWhenFreeToggleBtn;
    private ToggleButton mWhenWorkToggleBtn;
    private ToggleButton mWhenShoppingToggleBtn;

    public WhenConditionViewGroup(Activity activity) {
        mWhenHomeToggleBtn = (ToggleButton) activity.findViewById(R.id.when_home_toggle);
        mWhenFreeToggleBtn = (ToggleButton) activity.findViewById(R.id.when_free_toggle);
        mWhenWorkToggleBtn = (ToggleButton) activity.findViewById(R.id.when_work_toggle);
        mWhenShoppingToggleBtn = (ToggleButton) activity.findViewById(R.id.when_shopping_toggle);
    }

    /**
     * Reset toggle buttons
     */
    public void resetToggleButtons() {
        mWhenHomeToggleBtn.setChecked(false);
        mWhenFreeToggleBtn.setChecked(false);
        mWhenWorkToggleBtn.setChecked(false);
        mWhenShoppingToggleBtn.setChecked(false);
    }

    /**
     * Register a callback to be invoked when the condition changed.
     *
     * @param listener
     */
    public void onConditionChanged(final OnConditionChangeListener listener) {
        CompoundButton.OnCheckedChangeListener checkedListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listener.onConditionChanged(getCondition());
            }
        };

        mWhenHomeToggleBtn.setOnCheckedChangeListener(checkedListener);
        mWhenFreeToggleBtn.setOnCheckedChangeListener(checkedListener);
        mWhenWorkToggleBtn.setOnCheckedChangeListener(checkedListener);
        mWhenShoppingToggleBtn.setOnCheckedChangeListener(checkedListener);
    }

    /**
     * Get current when condition from the toggle buttons
     *
     * @return
     */
    public WhenCondition getCondition() {
        return new WhenCondition()
                .atHome(mWhenHomeToggleBtn.isChecked())
                .freeTime(mWhenFreeToggleBtn.isChecked())
                .atWork(mWhenWorkToggleBtn.isChecked())
                .shopping(mWhenShoppingToggleBtn.isChecked());
    }
}
