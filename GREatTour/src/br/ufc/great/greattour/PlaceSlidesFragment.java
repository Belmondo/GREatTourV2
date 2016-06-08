package br.ufc.great.greattour;

import android.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

public final class PlaceSlidesFragment extends Fragment {
    int imageResourceId;

    public PlaceSlidesFragment(int i) {
        imageResourceId = i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        ImageView image = new ImageView(getActivity());
        image.setImageResource(imageResourceId);

        LinearLayout layout = new LinearLayout(getActivity());


        layout.setGravity(Gravity.CENTER);
        layout.addView(image);

        return layout;
    }
}
