package com.example.etta;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {


    public BlankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       ProgressBar uploadbar=getActivity().findViewById(R.id.upload_progressBar);
        if (uploadbar!=null){
            uploadbar.setVisibility(View.GONE);
        }
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

}
