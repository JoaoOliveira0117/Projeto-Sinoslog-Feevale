package com.example.projetofeevale.ui.auth.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.projetofeevale.R;
import com.example.projetofeevale.activities.AuthActivity;
import com.example.projetofeevale.data.model.request.UserRequest;
import com.example.projetofeevale.data.model.response.UserResponse;
import com.example.projetofeevale.data.remote.api.ApiCallback;
import com.example.projetofeevale.data.remote.repository.UserRepository;

public class Register extends Fragment {
    private TextView errorLabel;
    private EditText name;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;

    public Register() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_register, container, false);

        AuthActivity authActivity = (AuthActivity) requireActivity();
        errorLabel = view.findViewById(R.id.errorLabel);
        TextView LoginButton = view.findViewById(R.id.button_login);
        Button registerButton = view.findViewById(R.id.button_register);

        name = view.findViewById(R.id.nomeInput);
        email = view.findViewById(R.id.emailInput);
        password = view.findViewById(R.id.passwordInput);
        confirmPassword = view.findViewById(R.id.confirmPasswordInput);

        errorLabel.setText("");

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authActivity.setLoginFragment();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isFormValid()) {
                    return;
                }

                UserRequest userRequest = new UserRequest(
                        name.getText().toString(),
                        email.getText().toString(),
                        password.getText().toString()
                );

                new UserRepository().createUser(userRequest, new ApiCallback<UserResponse>() {
                    @Override
                    public void onSuccess(UserResponse data) {
                        authActivity.setLoginFragment();
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
        String nameValue = name.getText().toString();
        String emailValue = email.getText().toString();
        String passwordValue = password.getText().toString();
        String confirmPasswordValue = confirmPassword.getText().toString();

        if (nameValue.isEmpty() && emailValue.isEmpty() && passwordValue.isEmpty() && confirmPasswordValue.isEmpty()) {
            errorLabel.setText("Campos Obrigatórios");
            return false;
        }

        if (!confirmPasswordValue.equals(passwordValue)) {
            errorLabel.setText("As senhas não coincidem");
            return false;
        }

        return true;
    }
}