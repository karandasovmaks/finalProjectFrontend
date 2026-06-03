package com.example.finalaapplication.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.finalaapplication.R;

public class Nav_frag extends Fragment {
    AppCompatButton firstFragment;
    AppCompatButton secondFragment;
    AppCompatButton thirdFragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nav_frag, container, false);
        firstFragment = view.findViewById(R.id.book);
        secondFragment = view.findViewById(R.id.towpers);
        thirdFragment = view.findViewById(R.id.onepers);
        firstFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.popularTests2);
            }
        });

        secondFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.second_Screen);

            }
        });

        thirdFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.fragmentperson);
            }});
        return view;
    }
}
