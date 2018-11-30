package com.example.neema.storyboard;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PromptActivity extends AppCompatActivity {

    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mRef = mDatabase.getReference("CardTable");

    EditText draftText;
    TextView visibilityText;
    TextView titleText;
    Switch privacySwitch;
    boolean isPublic;
    private String CardID, Uid, privacyText;

    //For editing title
    String titlePlaceholderText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prompt);
        Intent intent = getIntent();

        draftText = findViewById(R.id.draftText);
        visibilityText = findViewById(R.id.visibilityText);
        titleText = findViewById(R.id.toolbarTitle);
        privacySwitch = findViewById(R.id.privacySwitch);
        isPublic = privacySwitch.isChecked();

        if (intent.getExtras()!= null){
            draftText.setText(intent.getStringExtra("Text"));
            titleText.setText(intent.getStringExtra("Title"));
            CardID = intent.getStringExtra("CardId");
            Uid = intent.getStringExtra("uid");
            isPublic = (intent.getBooleanExtra("Pub", false));

            if (isPublic){
                visibilityText.setText("Public");
                isPublic = true;
                privacySwitch.setChecked(true);
            }
            else {
                visibilityText.setText("Private");
                isPublic = false;
                privacySwitch.setChecked(false);
            }

        }
//        FloatingActionButton saveButton = (FloatingActionButton) findViewById(R.id.saveButton);
        FloatingActionButton uploadButton = (FloatingActionButton) findViewById(R.id.uploadButton);
        FloatingActionButton titleButton = (FloatingActionButton) findViewById(R.id.titleButton);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        privacySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    visibilityText.setText("Public");
                }
                else {
                    visibilityText.setText("Private");
                }
                isPublic = isChecked;
            }
        });

//        saveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String cardId = mRef.child(currentUser).child("Cards").push().getKey();
//                Card card = new Card(CardType.PROMPT, currentUser, cardId, titleText.getText().toString(), draftText.getText().toString(), false);
//
//                mRef.child(currentUser).child("Cards").child(cardId).setValue(card);
//                Snackbar.make(view, "Save button pressed", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder inputDialog = new AlertDialog.Builder(PromptActivity.this);
                if(isPublic){
                    inputDialog.setTitle("Are you sure you want to upload this prompt to the community?");

                } else {
                    inputDialog.setTitle("Are you sure you want to post this prompt privately?");
                }

                inputDialog.setNegativeButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        /*
                    TODO: Update card if (intent != null)
                    the fields are updated for isPublic, title and text. use dont update cardID and use Uid
                    */

                        //TODO: UPLOAD PROMPT TEXT AND TITLE TO COMMUNITY
                        //TODO: UPLOAD PROMPT BASED ON PRIVACY
                        Toast.makeText(getApplicationContext(),
                                "Uploaded successfully!",
                                Toast.LENGTH_LONG).show();
                    }
                });

                inputDialog.setPositiveButton("Back to drafting", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                inputDialog.show();
            }
        });

        titleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),
                        "Title edit button pressed",
                        Toast.LENGTH_SHORT).show();
                AlertDialog.Builder inputDialog = new AlertDialog.Builder(PromptActivity.this);
                inputDialog.setTitle("Enter New Title:");

                final EditText input = new EditText(PromptActivity.this);
                input.setPadding(80, 25, 80, 0);
                input.setBackground(null);
                input.setInputType(InputType.TYPE_CLASS_TEXT);

                inputDialog.setView(input);

                inputDialog.setNegativeButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        titlePlaceholderText = input.getText().toString();
                        titleText.setText(titlePlaceholderText);
                    }
                });
                inputDialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                inputDialog.show();
            }
        });
    }
}
