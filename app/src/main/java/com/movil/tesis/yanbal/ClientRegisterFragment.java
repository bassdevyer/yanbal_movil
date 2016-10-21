package com.movil.tesis.yanbal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by mac on 10/18/16.
 */

public class ClientRegisterFragment extends Fragment {

    public ClientRegisterFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_client_register, container, false);
        inflateViews(rootView);
        return rootView;
    }

    private void inflateViews(View rootView) {
        ImageButton locationImageButton = (ImageButton) rootView.findViewById(R.id.clientLocationButton);
        locationImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapActivityIntent = new Intent(getActivity(), MapActivity.class);
                getActivity().startActivity(mapActivityIntent);
            }
        });

    }
}
