package com.untitledhorton.projectarchive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.untitledhorton.projectarchive.utility.FirebaseOperation;

public class AddRecipientActivity extends AppCompatActivity {
    private Toolbar toolBar;
    private EditText etRecipient;
    String id, title, note, priority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipient);
        etRecipient = findViewById(R.id.etRecipient);

        toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Add Recipient");
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Bundle extras = getIntent().getExtras();
        id = extras.getString("id");
        title = extras.getString("title");
        note = extras.getString("note");
        priority = extras.getString("priority");

    }

    public void send(View v){
        String recpientList = etRecipient.getText().toString();
        String[] recipients = recpientList.split(",");

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, "ArcHive Shared Notes");
        intent.putExtra(Intent.EXTRA_TEXT, "I shared you a note!");

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose an email client"));
        FirebaseOperation.insertSharedNote(id, title, note, priority, recipients);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
