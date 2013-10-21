package net.teerapap.whatnext;

import android.app.ListFragment;
import android.os.Bundle;
import android.widget.ArrayAdapter;

/**
 * Fragment for Done menu. It shows a list of done tasks.
 * Created by teerapap on 10/21/13.
 */
public class DoneFragment extends ListFragment {

    /**
     * Static method to initialize the fragment
     * @return
     */
    public static DoneFragment newInstance() {
        DoneFragment f = new DoneFragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: Read done list from database
        this.setListAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, new String[] {}));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Show no items with icon
        this.setEmptyText("No items :)");
    }
}
