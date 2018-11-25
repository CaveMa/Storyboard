package com.example.neema.storyboard;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Settings extends AppCompatActivity{
    Button changeUsername, changePassword;
    EditText username, oldPass, newPass, newPassConfirm, changeBio;
    String usernameString, bioString, oldPasswordString, newPasswordString, newPassConfirmString;
    static String email;

    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRefUserTable = mFirebaseDatabase.getReference("UserTable");
    private String currentUser = mFirebaseAuth.getCurrentUser().getUid();

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState );
        setContentView(R.layout.settings);

        changeUsername = findViewById(R.id.usernameButton);
        changePassword = findViewById(R.id.passwordButton);

        username = findViewById(R.id.changeUsername);
        oldPass = findViewById(R.id.oldpass);
        newPass = findViewById(R.id.newPass);
        newPassConfirm = findViewById(R.id.newPassConfirm);
        changeBio = findViewById(R.id.changeBio);

        mRefUserTable.child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                email = (String) dataSnapshot.child("email").getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    boolean isInfoEntered(String input){return input.length() > 0;}

    public void usernamePressed(View v){
        usernameString = username.getText().toString();
        if (isInfoEntered(usernameString)){
           mRefUserTable.child(currentUser).child("username").setValue(usernameString);
        }
        else {username.setError("Not a Username");}

    }

    public void passPressed(View v){
        oldPasswordString = oldPass.getText().toString();
        newPasswordString = newPass.getText().toString();
        newPassConfirmString = newPassConfirm.getText().toString();
        if (isInfoEntered(oldPasswordString) && isInfoEntered(newPassConfirmString) && isInfoEntered(newPassConfirmString)) {
            if (newPasswordString.length() > 5) {
                AuthCredential credential = EmailAuthProvider.getCredential(email, oldPasswordString);
                mFirebaseAuth.getCurrentUser().reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mFirebaseAuth.getCurrentUser().updatePassword(newPassConfirmString).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Password Updated", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                        else {
                            oldPass.setError("Current Password Incorrect");
                        }
                    }
                });


            }
            else {newPass.setError("Password needs to be more than 5 characters"); }
        }
        else{
            oldPass.setError("Must enter password");
            newPass.setError("Must enter password");
            newPassConfirm.setError("Must enter password");
        }

    }

    public void bioPressed(View v){
        bioString = changeBio.getText().toString();
        if (isInfoEntered(bioString)){
            mRefUserTable.child(currentUser).child("bio").setValue(bioString);
        }
        else{changeBio.setError("Must enter Bio");}
    }
}
