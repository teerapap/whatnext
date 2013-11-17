package net.teerapap.whatnext.view;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import net.teerapap.whatnext.R;
import net.teerapap.whatnext.model.DoneTasksLoader;
import net.teerapap.whatnext.model.TaskDbHelper;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Fragment for Done menu. It shows a list of done tasks.
 * Created by teerapap on 10/21/13.
 */
public class DoneFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "DoneFragment";
    public static final int DONE_TASK_LOADER = 1;

    private SimpleCursorAdapter mAdapter;

    /**
     * Static method to initialize the fragment
     *
     * @return
     */
    public static DoneFragment newInstance() {
        DoneFragment f = new DoneFragment();
        return f;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Show no items with icon
        setEmptyText("No items :)");

        // This fragment has options menu
        setHasOptionsMenu(true);

        // Create cursor adapter
        mAdapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_2, null,
                new String[] {TaskDbHelper.TaskEntry.COLUMN_NAME_TITLE, TaskDbHelper.TaskEntry.COLUMN_NAME_DONE_TIME },
                new int[] { android.R.id.text1, android.R.id.text2 }, 0) {

            @Override
            public void setViewText(TextView v, String text) {
                if (v.getId() == android.R.id.text2) {
                    try {
                        // Convert done time to human readable
                        Date doneTime = TaskDbHelper.UTC_DATE_FORMAT.parse(text);
                        text = "" + DateUtils.getRelativeDateTimeString(getActivity(), doneTime.getTime(), DateUtils.SECOND_IN_MILLIS, DateUtils.YEAR_IN_MILLIS, 0);
                    } catch (ParseException e) {
                        Log.e(TAG, "Cannot parse this data "+ text, e);
                    }
                }
                super.setViewText(v, text);
            }
        };
        setListAdapter(mAdapter);

        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(DONE_TASK_LOADER, null, this);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // Disable delete action for now.
        menu.findItem(R.id.action_delete_task).setVisible(false);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        DoneTasksLoader d = new DoneTasksLoader(getActivity());
        return d;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
