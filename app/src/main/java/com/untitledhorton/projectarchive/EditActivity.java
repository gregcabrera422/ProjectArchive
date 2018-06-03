package com.untitledhorton.projectarchive;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.untitledhorton.projectarchive.utility.FirebaseOperation;

public class EditActivity extends AppCompatActivity {

    private EditText etTitle, etNote;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Toolbar toolBar;
    String editKey, editTitle, editNote, priority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        toolBar = findViewById(R.id.toolbar);

        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Edit Note");
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        etNote = findViewById(R.id.etNote);
        etTitle = findViewById(R.id.etTitle);
        radioGroup = findViewById(R.id.radioGroup);

        Bundle extras = getIntent().getExtras();
        editKey = extras.getString("editKey");
        editTitle = extras.getString("editTitle");
        editNote = extras.getString("editNote");

        etTitle.setText(editTitle);
        etNote.setText(editNote);
        priority = "High";
    }

    public void checkButton(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);

        priority = radioButton.getText().toString();
    }

    public void editNote(View view){
        String title = etTitle.getText().toString();
        String note = etNote.getText().toString();
        FirebaseOperation.editNote(title, note, priority, editKey);
        this.finish();
    }

    public void cancel(View view){
        this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
