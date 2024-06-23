package com.example.projetofeevale.fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projetofeevale.R;
import com.example.projetofeevale.ui.createOccurrence.fragment.CreateOccurrence;

public class HomeFragment extends Fragment {
    private Fragment mapFragment;

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mapFragment = new MapFragment(onAddPinListener());
        replaceFragment(mapFragment);

        return view;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.home_layout, fragment);
        fragmentTransaction.commit();
    }

    private View.OnClickListener onAddPinListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new CreateOccurrence(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        replaceFragment(mapFragment);
                    }
                }));
            }
        };
    }
}