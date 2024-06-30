package com.example.projetofeevale.ui.conta;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.projetofeevale.R;
import com.example.projetofeevale.activities.MainActivity;
import com.example.projetofeevale.activities.SislogActivity;
import com.example.projetofeevale.data.model.response.UserResponse;
import com.example.projetofeevale.data.remote.api.ApiCallback;
import com.example.projetofeevale.data.remote.repository.AuthRepository;

public class Conta extends Fragment {
    private LinearLayout exitButton;

    private TextView userName;
    public Conta() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        MainActivity mainActivity = (MainActivity) requireActivity();

        exitButton = view.findViewById(R.id.exitButton);
        userName = view.findViewById(R.id.txt_Dados);

        new AuthRepository((SislogActivity) requireActivity()).getUserMe(new ApiCallback<UserResponse>() {
            @Override
            public void onSuccess(UserResponse data) {
                if (data == null) {
                    mainActivity.startLoginActivity();
                }

                userName.setText(data.getName());
            }

            @Override
            public void onFailure(String message, String cause, Throwable t) {

            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.getAuthService().finishSession();
            }
        });

        return view;
    }
}