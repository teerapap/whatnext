package net.teerapap.whatnext.model;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;

import net.teerapap.whatnext.service.TaskService;

import java.util.Date;

/**
 * Created by teerapap on 11/17/13.
 */
public class DoneTasksLoader extends AsyncTaskLoader<Cursor> implements TaskService.TaskDoneCallback {

    // We hold a reference to the Loader’s cursor here.
    private Cursor mCursor;
    private TaskDbHelper mDbHelper;
    private TaskService mTaskService;

    public DoneTasksLoader(Context ctx) {
        super(ctx);
        mDbHelper = new TaskDbHelper(ctx);
    }

    @Override
    public Cursor loadInBackground() {
        return mDbHelper.getDoneTasksCursor(0, -1);
    }

    @Override
    public void deliverResult(Cursor cursor) {
        if (isReset()) {
            // The Loader has been reset; ignore the result and invalidate the data.
            releaseResources(cursor);
            return;
        }

        // Hold a reference to the old data so it doesn't get garbage collected.
        // We must protect it until the new data has been delivered.
        Cursor oldCursor = mCursor;
        mCursor = cursor;

        if (isStarted()) {
            // If the Loader is in a started state, deliver the results to the
            // client. The superclass method does this for us.
            cursor.moveToFirst();
            super.deliverResult(cursor);
        }

        // Invalidate the old data as we don't need it any more.
        if (oldCursor != null && oldCursor != cursor) {
            releaseResources(oldCursor);
        }
    }

    @Override
    protected void onStartLoading() {
        if (mCursor != null) {
            // Deliver any previously loaded data immediately.
            deliverResult(mCursor);
        }

        // Begin monitoring the underlying data source.
        if (mTaskService == null) {
            mTaskService = TaskService.getInstance(getContext());
            mTaskService.setTaskDoneListener(this);
        }

        if (takeContentChanged() || mCursor == null) {
            // When the observer detects a change, it should call onContentChanged()
            // on the Loader, which will cause the next call to takeContentChanged()
            // to return true. If this is ever the case (or if the current data is
            // null), we force a new load.
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        // The Loader is in a stopped state, so we should attempt to cancel the
        // current load (if there is one).
        cancelLoad();

        // Note that we leave the observer as is. Loaders in a stopped state
        // should still monitor the data source for changes so that the Loader
        // will know to force a new load if it is ever started again.
    }

    @Override
    protected void onReset() {
        // Ensure the loader has been stopped.
        onStopLoading();

        // At this point we can release the resources associated with 'mCursor'.
        if (mCursor != null) {
            releaseResources(mCursor);
            mCursor = null;
        }

        // The Loader is being reset, so we should stop monitoring for changes.
        if (mTaskService != null) {
            // unregister the observer
            mTaskService.setTaskDoneListener(null);
            mTaskService = null;
        }
    }

    @Override
    public void onCanceled(Cursor cursor) {
        // Attempt to cancel the current asynchronous load.
        super.onCanceled(cursor);

        // The load has been canceled, so we should release the resources
        // associated with 'cursor'.
        releaseResources(cursor);
    }

    private void releaseResources(Cursor cursor) {
        // Close the cursor
        cursor.close();
    }

    @Override
    public void onTaskDone(Task t, Date doneTime) {
        // Signal loader that done task db has changed
        new Handler(getContext().getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                onContentChanged();
            }
        });
    }
}
