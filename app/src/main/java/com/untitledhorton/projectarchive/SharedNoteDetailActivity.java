package com.untitledhorton.projectarchive;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.untitledhorton.projectarchive.utility.FirebaseOperation;

public class SharedNoteDetailActivity extends AppCompatActivity {

    private Toolbar toolBar;
    String title, note, priority, owner;
    TextView tvTitle, tvNote, tvPriority, tvOwner;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_note_detail);
        toolBar = findViewById(R.id.toolbar);

        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Shared Note");
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvTitle = findViewById(R.id.tvTitle);
        tvOwner = findViewById(R.id.tvOwner);
        tvNote = findViewById(R.id.tvNote);
        tvPriority = findViewById(R.id.tvPriority);

        Bundle extras = getIntent().getExtras();
        title = extras.getString("title");
        owner = extras.getString("owner");
        note = extras.getString("note");
        priority = extras.getString("priority");

        tvTitle.setText(title);
        tvNote.setText(note);
        tvOwner.setText("created by " + owner);
        tvPriority.setText("Priority: " + priority);
    }

    public void addToNotes(View v){
        FirebaseOperation.insertNote(title, note, priority);
        Toast.makeText(this, "Shared note sent to your notes.", Toast.LENGTH_LONG);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
