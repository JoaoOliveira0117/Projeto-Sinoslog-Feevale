package com.example.projetofeevale.ui.auth.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.projetofeevale.R;
import com.example.projetofeevale.activities.AuthActivity;
import com.example.projetofeevale.activities.MainActivity;
import com.example.projetofeevale.data.model.request.AuthRequest;
import com.example.projetofeevale.data.model.response.AuthResponse;
import com.example.projetofeevale.data.remote.api.ApiCallback;
import com.example.projetofeevale.data.remote.repository.AuthRepository;
import com.example.projetofeevale.services.DataStore;

public class Login extends Fragment {
    private TextView errorLabel;
    private EditText email;
    private EditText password;


    public Login() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_login, container, false);
        AuthActivity authActivity = (AuthActivity) requireActivity();

        DataStore dataStore = DataStore.getInstance(requireContext());

        email = view.findViewById(R.id.emailInput);
        password = view.findViewById(R.id.passwordInput);
        errorLabel = view.findViewById(R.id.errorLabel);
        TextView createAccountButton = view.findViewById(R.id.button_register);
        Button loginButton = view.findViewById(R.id.button_login);

        errorLabel.setText("");

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authActivity.setRegisterFragment();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isFormValid()) {
                    return;
                }

                AuthRequest authRequest = new AuthRequest(email.getText().toString(), password.getText().toString());
                new AuthRepository().login(authRequest, new ApiCallback<AuthResponse>() {
                    @Override
                    public void onSuccess(AuthResponse data) {
                        authActivity.getAuthService().setToken(data.getToken());
                        authActivity.startMainActivity();
                    }

                    @Override
                    public void onFailure(String message, String cause, Throwable t) {
                        errorLabel.setText(message);
                    }
                });
            }
        });

        return view;
    }

    private boolean isFormValid() {
        errorLabel.setText("");
        String emailValue = email.getText().toString();
        String passwordValue = password.getText().toString();

        if (emailValue.isEmpty() && passwordValue.isEmpty()) {
            errorLabel.setText("Campos Obrigatórios");
            return false;
        }

        return true;
    }
}