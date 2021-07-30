package com.moadab.tasty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.moadab.tasty.Fragments.UserFragment;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserActivity extends AppCompatActivity {

    /* Initialize variables */
    CircleImageView proImage;
    ImageView back,changeProImage;
    TextView userNameProfile,userEmailProfile,userBioProfile;
    FirebaseAuth mAuth;
    TextView nSave,nCancel,bSave,bCancel,tvLogout;
    EditText changeName,changeBio;
    public String txtName,txtBio;
    ProgressDialog progressDialog;
    private Uri imageUri;
    private StorageReference storageReference;
    private DatabaseReference reference;
    private String myUri = "";
    private StorageTask uploadTask;
    TextView btnSignout,s_cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        /* Access to activity elements */
        proImage = findViewById(R.id.ivAccount);
        changeProImage = findViewById(R.id.ivChangePicture);
        back = findViewById(R.id.back_account);
        tvLogout = findViewById(R.id.tvLogout);
        userNameProfile = findViewById(R.id.c_user);
        userEmailProfile = findViewById(R.id.c_email);
        userBioProfile = findViewById(R.id.c_bio);
        mAuth = FirebaseAuth.getInstance();

        readInfoUser();

        back.setOnClickListener(v -> finish());

        /* Access to Instance  Firebase Database */
        reference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        /* click on insert or change image profile in Firebase Database */
        changeProImage.setOnClickListener(v ->
                CropImage.activity()
                        .setAspectRatio(1,1)
                        .start(UserActivity.this));

        userNameProfile.setOnClickListener(v -> changeNameDialog());
        userBioProfile.setOnClickListener(v -> changeBioDialog());


        /* Create Progress Dialog*/
        progressDialog = new ProgressDialog(UserActivity.this);

        tvLogout.setOnClickListener(v -> LogoutDialog());

    }


    private String getFileExtention(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.getContentResolver().getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            proImage.setImageURI(imageUri);
        }
        else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            finish();
        }


        if(imageUri != null){

            if (resultCode == RESULT_OK) {

                /* Show Customize Progress Dioalog after click crop image */
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

                if (imageUri != null) {

                    /* insert or change image profile in Firebase Database */
                    final StorageReference filePath = storageReference.child("imageProfile")
                            .child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid() + getFileExtention(imageUri));

                    uploadTask = filePath.putFile(imageUri);
                    uploadTask.continueWithTask((Continuation) task -> {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return filePath.getDownloadUrl();
                    }).addOnCompleteListener((OnCompleteListener<Uri>) task -> {
                        Uri downloadUri = task.getResult();
                        myUri = downloadUri.toString();

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users")
                                .child(mAuth.getCurrentUser().getUid()).child("imageurlProfile");
                        ref.setValue(myUri);
                        progressDialog.dismiss();



                    }).addOnFailureListener(e -> Toast.makeText(UserActivity.this ,"Try Again!", Toast.LENGTH_SHORT).show());
                }
            }

        }

    }

    /* Show Customize change Bio Dialog */
    private void changeBioDialog() {

        AlertDialog.Builder alert;
        alert = new AlertDialog.Builder(this,R.style.Theme_MaterialComponents_Dialog_Alert);
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.change_bio_dialog,null);

        changeBio = view.findViewById(R.id.change_bio);
        bCancel =  view.findViewById(R.id.b_cancel);
        bSave = view.findViewById(R.id.b_save);

        alert.setView(view);
        alert.setCancelable(false);

        AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

        dialog.show();

        bSave.setOnClickListener(v -> {
            txtBio = changeBio.getText().toString().trim();
            if(TextUtils.isEmpty(txtBio)){
                Toast.makeText(UserActivity.this, "Your bio is empty" , Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
            else {
                updateUserBio(txtBio);
                dialog.dismiss();
            }
        });

        bCancel.setOnClickListener(v -> dialog.dismiss());
    }

    /* Show Customize sign Out  Dialog */
    private void LogoutDialog() {

        AlertDialog.Builder alert;
        alert = new AlertDialog.Builder(this,R.style.Theme_MaterialComponents_Dialog_Alert);
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.logout_dialog,null);

        s_cancel =  view.findViewById(R.id.s_cancel);
        btnSignout = view.findViewById(R.id.btn_signout);

        alert.setView(view);
        alert.setCancelable(false);

        AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

        dialog.show();

        btnSignout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(UserActivity.this , LoginOrSignUpActivity.class));
            finish();

            Toast.makeText(UserActivity.this, "Sign out is Successful" , Toast.LENGTH_SHORT).show();
        });

        s_cancel.setOnClickListener(v -> dialog.dismiss());
    }


    /* Show Customize change Name Dialog */
    private void changeNameDialog() {

        AlertDialog.Builder alert;
        alert = new AlertDialog.Builder(this,R.style.Theme_MaterialComponents_Dialog_Alert);
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.change_name_dialog,null);

        changeName = view.findViewById(R.id.change_name);
        nCancel =  view.findViewById(R.id.n_cancel);
        nSave = view.findViewById(R.id.n_save);

        alert.setView(view);
        alert.setCancelable(false);

        AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

        dialog.show();

        nSave.setOnClickListener(v -> {
            txtName = changeName.getText().toString().trim();
            if(TextUtils.isEmpty(txtName)){
                Toast.makeText(UserActivity.this, "Your name is empty" , Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
            else {
                updateUserInfo(txtName);
                dialog.dismiss();
            }
        });

        nCancel.setOnClickListener(v -> dialog.dismiss());

    }
    /* Method User change user name in FireBase */
    private void updateUserInfo(String txtName) {

        FirebaseDatabase.getInstance().getReference().child("Users").
                child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        dataSnapshot.getRef().child("name").setValue(txtName);

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

        readInfoUser();

    }
    /* Method User change user bio in FireBase */
    private void updateUserBio(String txtBio) {

        FirebaseDatabase.getInstance().getReference()
                .child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        dataSnapshot.getRef().child("bio").setValue(txtBio);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        readInfoUser();
    }

    /* get user information in firebase */
    private void readInfoUser() {

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(mAuth.getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String txtName = Objects.requireNonNull(snapshot.child("name").getValue()).toString();
                String txtEmail = Objects.requireNonNull(snapshot.child("email").getValue()).toString();
                String imgProfile = Objects.requireNonNull(snapshot.child("imageurlProfile").getValue().toString());
                String txtBio = Objects.requireNonNull(snapshot.child("bio").getValue().toString());

                userNameProfile.setText(txtName);
                userEmailProfile.setText(txtEmail);
                userBioProfile.setText(txtBio);

                if (!imgProfile.equals("default")) {
                    Picasso.get()
                            .load(imgProfile)
                            .placeholder(R.drawable.profile)
                            .error(R.drawable.profile)
                            .into(proImage);

                }else {
                    proImage.setImageResource(R.drawable.profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}