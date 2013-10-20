package net.teerapap.whatnext;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment for WhatNext page. It is the main fragment for the application.
 * Created by teerapap on 10/21/13.
 */
public class WhatNextFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_whatnext, container, false);
        return rootView;
    }
}
