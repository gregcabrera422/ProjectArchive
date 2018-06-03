package com.untitledhorton.projectarchive;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.untitledhorton.projectarchive.utility.FirebaseOperation;

public class NoteDetailActivity extends AppCompatActivity {

    private Toolbar toolBar;
    String title, note, priority, id;
    TextView tvTitle, tvNote, tvPriority;
    Button btnShare;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        toolBar = findViewById(R.id.toolbar);

        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Note");
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvTitle = findViewById(R.id.tvTitle);
        tvNote = findViewById(R.id.tvNote);
        tvPriority = findViewById(R.id.tvPriority);

        Bundle extras = getIntent().getExtras();
        title = extras.getString("title");
        note = extras.getString("note");
        priority = extras.getString("priority");

        tvTitle.setText(title);
        tvNote.setText(note);
        tvPriority.setText("Priority: " + priority);
    }

    public void shareNote(View v){
        Intent intent = new Intent(this, AddRecipientActivity.class);
        intent.putExtra("id", user.getDisplayName());
        intent.putExtra("title", title);
        intent.putExtra("note", note);
        intent.putExtra("priority", priority);
        this.startActivity(intent);
        //FirebaseOperation.insertSharedNote(user.getDisplayName(), title, note, priority, "201501101");
    }
}
