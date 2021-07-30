package com.moadab.tasty.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.moadab.tasty.MainActivity;
import com.moadab.tasty.R;
import com.moadab.tasty.ui.home.HomeActivity;


public class LoginFragment extends Fragment {

    private TextInputEditText email;
    private TextInputEditText password;
    private TextView btnLogin;
    ProgressDialog progressDialog;

    /* Initialize variable of Firebase */
    private FirebaseAuth loginAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        email = view.findViewById(R.id.l_etEmail);
        password = view.findViewById(R.id.l_etPassword);
        btnLogin = view.findViewById(R.id.tvLogin);

        /* Create Progress Dialog for login user*/
        progressDialog=new ProgressDialog(getActivity());

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(getActivity() ,"Email or Password is Empty!", Toast.LENGTH_SHORT).show();
                } else {
                    loginUser(txt_email,txt_password);
                }
            }
        });

        /* Access to Instance  Firebase Database*/
        loginAuth = FirebaseAuth.getInstance();
        return view;
    }


    /* Method User Login in FireBase */
    void loginUser(String l_email, String l_password) {

        /* Show Customize Progress Dioalog after click sing up */
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

        /* Search User With Email And Password */
        loginAuth.signInWithEmailAndPassword(l_email , l_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Login is Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}