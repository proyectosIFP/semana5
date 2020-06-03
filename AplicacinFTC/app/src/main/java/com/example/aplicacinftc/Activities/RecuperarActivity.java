package com.example.aplicacinftc.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aplicacinftc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class RecuperarActivity extends AppCompatActivity {

    private EditText editEmail;
    private Button resetearContra;
    private Button volverLogin;

    private String email;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar);

        editEmail =(EditText) findViewById(R.id.emailResetear);

        mAuth = FirebaseAuth.getInstance();

        resetearContra =(Button) findViewById(R.id.buttonResetarContra);
        resetearContra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editEmail.getText().toString();
                if (!email.isEmpty() && !email.trim().isEmpty()){
                    editEmail.setText("");
                    mAuth.setLanguageCode("es");
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplication(), "SE HA ENVIADO UN CORREO ELECTRÓNICO A: "+email, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RecuperarActivity.this, LoginActivity.class));
                                finish();
                            }else{
                                Toast.makeText(getApplication(), "NO SE HA PODIDO ENVIAR", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else{
                    Toast.makeText(getApplication(), "INTRODUZCA UN CORREO ELECTRÓNICO", Toast.LENGTH_SHORT).show();
                }
            }
        });

        volverLogin =(Button) findViewById(R.id.buttonLoginUserLlamarSegundo);
        volverLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecuperarActivity.this, LoginActivity.class));
                finish();
            }
        });

    }

}
