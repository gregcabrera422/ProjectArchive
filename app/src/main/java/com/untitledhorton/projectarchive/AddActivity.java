package com.untitledhorton.projectarchive;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.untitledhorton.projectarchive.utility.FirebaseOperation;

public class AddActivity extends AppCompatActivity {
    private EditText etTitle, etNote;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Toolbar toolBar;
    private String title, note, priority;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        toolBar = findViewById(R.id.toolbar);

        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Add Note");
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        etTitle = findViewById(R.id.etTitle);
        etNote = findViewById(R.id.etNote);
        radioGroup = findViewById(R.id.radioGroup);
        priority = "High";
    }

    public void checkButton(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);

        priority = radioButton.getText().toString();
    }

    public void addNote(View v){
        title = etTitle.getText().toString();
        note = etNote.getText().toString();
        FirebaseOperation.insertNote(title, note, priority);
        this.finish();
    }

    public void cancel(View v){
        this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
