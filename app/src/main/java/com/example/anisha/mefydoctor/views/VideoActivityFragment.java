package com.example.anisha.mefydoctor.views;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anisha.mefydoctor.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class VideoActivityFragment extends Fragment {

    public VideoActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video, container, false);
    }
}
