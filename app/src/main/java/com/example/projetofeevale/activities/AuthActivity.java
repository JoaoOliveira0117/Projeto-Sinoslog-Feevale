
package com.example.projetofeevale.activities;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.projetofeevale.R;
import com.example.projetofeevale.services.Auth;
import com.example.projetofeevale.ui.auth.fragment.Login;
import com.example.projetofeevale.ui.auth.fragment.Register;
import com.example.projetofeevale.ui.conta.Conta;
import com.example.projetofeevale.ui.contatos.Contatos;
import com.example.projetofeevale.ui.createOccurrence.fragment.BaseOccurrence;
import com.example.projetofeevale.ui.paginaInicial.PaginaInicial;
import com.example.projetofeevale.ui.meusPins.MeusPins;
import com.example.projetofeevale.services.LocationService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AuthActivity extends SislogActivity {
    private Auth authService;
    private Login login;
    private Register register;
    private LocationService locationService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        authService = new Auth(this);
        login = new Login();
        register = new Register();

        replaceFragment(login, false, false);
    }


    private void replaceFragment(Fragment fragment, boolean animated, boolean toRight) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (animated && toRight) {
            fragmentTransaction.setCustomAnimations(
                    R.anim.slide_in_from_right,
                    R.anim.slide_out_to_left,
                    R.anim.slide_in_from_left,
                    R.anim.slide_out_to_right
            );
        }

        if (animated && !toRight) {
            fragmentTransaction.setCustomAnimations(
                    R.anim.slide_in_from_left,
                    R.anim.slide_out_to_right,
                    R.anim.slide_in_from_right,
                    R.anim.slide_out_to_left
            );
        }

        fragmentTransaction.replace(R.id.auth_layout, fragment);
        fragmentTransaction.commit();
    }

    public void setRegisterFragment() {
        replaceFragment(register, true, true);
    }

    public void setLoginFragment() {
        replaceFragment(login, true, false);
    }

    public Auth getAuthService() {
        return authService;
    }
}

