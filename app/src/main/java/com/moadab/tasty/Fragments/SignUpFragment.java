package com.moadab.tasty.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.moadab.tasty.MainActivity;
import com.moadab.tasty.R;
import com.moadab.tasty.ui.home.HomeActivity;

import java.util.HashMap;
import java.util.Objects;


public class SignUpFragment extends Fragment {

    private TextInputEditText email;
    private TextInputEditText name;
    private TextInputEditText password;
    private TextView signUp;
    private DatabaseReference sRootRef;
    private FirebaseAuth sAuth;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        email = view.findViewById(R.id.etEmail);
        name = view.findViewById(R.id.etName);
        password = view.findViewById(R.id.etPassword);
        signUp = view.findViewById(R.id.tvSignUp);

        sRootRef = FirebaseDatabase.getInstance().getReference();
        sAuth = FirebaseAuth.getInstance();

        /* Create Progress Dialog for login user*/
        progressDialog=new ProgressDialog(getActivity());

        /* Button Sing Up */
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtName = name.getText().toString();
                String txtEmail = email.getText().toString();
                String txtPassword = password.getText().toString();
                if (TextUtils.isEmpty(txtName) || TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPassword)) {
                    Toast.makeText(getContext(), "!Values are empty!" , Toast.LENGTH_SHORT).show();
                } else if (txtPassword.length() < 6 ) {
                    Toast.makeText(getContext(), "!The password is less than 6 characters!" , Toast.LENGTH_SHORT).show();
                } else {
                    signUpUser(txtName , txtEmail , txtPassword);
                }
            }
        });


        return view;
    }

    /* Method User Sing Up in FireBase */
    private void signUpUser(final String name, final String email, final String password) {

        /* Show Customize Progress Dioalog after click sing up */
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

        /* Create User With Email And Password */
        sAuth.createUserWithEmailAndPassword(email , password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                HashMap<String, Object> mapSingUp = new HashMap<>();
                mapSingUp.put("name" , name);
                mapSingUp.put("email" , email);
                mapSingUp.put("id" , sAuth.getCurrentUser().getUid());
                mapSingUp.put("bio" , "Biography");
                mapSingUp.put("imageurlProfile" , "default");

                /* Insert Users information into RealTime Database */
                sRootRef.child("Users").child(sAuth.getCurrentUser().getUid()).setValue(mapSingUp).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(),"Registration was successful",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity() , HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
            }
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}