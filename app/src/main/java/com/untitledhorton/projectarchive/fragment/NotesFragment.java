package com.untitledhorton.projectarchive.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.melnykov.fab.FloatingActionButton;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.untitledhorton.projectarchive.AddActivity;
import com.untitledhorton.projectarchive.EditActivity;
import com.untitledhorton.projectarchive.NoteDetailActivity;
import com.untitledhorton.projectarchive.R;
import com.untitledhorton.projectarchive.model.Note;
import com.untitledhorton.projectarchive.utility.CustomNoteAdapter;
import com.untitledhorton.projectarchive.utility.FirebaseOperation;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Greg on 23/05/2018.
 */

public class NotesFragment extends Fragment implements View.OnClickListener{

    private FloatingActionButton fab;
    private ArrayList<Note> notes;
    private SwipeMenuListView lvNotes;
    private CustomNoteAdapter noteAdapter;
    private ProgressBar pb;
    private String day, month, year, searchText;
    private TextView tvEmpty;
    private EditText etSearch;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Notes");

        lvNotes = view.findViewById(R.id.lvReminders);
        pb = view.findViewById(R.id.pb);
        fab = view.findViewById(R.id.fab);
        tvEmpty = view.findViewById(R.id.tvEmpty);
        etSearch = view.findViewById(R.id.etSearch);

        notes = new ArrayList<Note>();
        fab.attachToListView(lvNotes);

        noteAdapter = new CustomNoteAdapter(getActivity(), notes);
        lvNotes.setEmptyView(tvEmpty);

        FirebaseOperation.retrieveNotes(pb, notes, noteAdapter, tvEmpty);

        lvNotes.setAdapter(noteAdapter);

        fab.setOnClickListener(this);
        swipeMenuCreator(lvNotes);

        lvNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                Note item = notes.get(position);

                Intent intent = new Intent(getActivity(), NoteDetailActivity.class);
                intent.putExtra("title", item.getTitle());
                intent.putExtra("note", item.getNote());
                intent.putExtra("priority", item.getPriority());
                getActivity().startActivity(intent);

            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchText = editable.toString();
                FirebaseOperation.searchNote(pb, notes, noteAdapter, tvEmpty, searchText);
            }
        });


    }

    public void swipeMenuCreator(SwipeMenuListView lvNotes){

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem moveNote = new SwipeMenuItem(
                        getActivity());
                moveNote.setBackground(new ColorDrawable(Color.rgb(204, 153, 255)));
                moveNote.setWidth(200);
                moveNote.setIcon(R.drawable.ic_date_range_black_32dp);
                moveNote.setTitleSize(18);
                moveNote.setTitleColor(Color.WHITE);
                menu.addMenuItem(moveNote);

                SwipeMenuItem editNote = new SwipeMenuItem(
                        getActivity());
                editNote.setBackground(new ColorDrawable(Color.rgb(153,204,255)));
                editNote.setWidth(200);
                editNote.setIcon(R.drawable.ic_edit_black_32dp);
                editNote.setTitleSize(18);
                editNote.setTitleColor(Color.WHITE);
                menu.addMenuItem(editNote);

                SwipeMenuItem deleteNote = new SwipeMenuItem(
                        getActivity());
                deleteNote.setBackground(new ColorDrawable(Color.rgb(255, 153, 153)));
                deleteNote.setWidth(200);
                deleteNote.setIcon(R.drawable.ic_delete_black_32dp);
                deleteNote.setTitleColor(Color.WHITE);
                deleteNote.setTitleSize(18);

                menu.addMenuItem(deleteNote);
            }
        };

        lvNotes.setMenuCreator(creator);
        lvNotes.setCloseInterpolator(new BounceInterpolator());

        lvNotes.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Note item = notes.get(position);

                switch (index) {
                    case 0:
                        Calendar cal = Calendar.getInstance();
                        final String moveKey = item.getId();

                        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int i, int i1, int i2) {
                                year = Integer.toString(i);
                                month = Integer.toString(i1 + 1);
                                day = Integer.toString(i2);

                                if(Integer.parseInt(day)<10){
                                    day = "0"+day;
                                }

                                if(Integer.parseInt(month)<10){
                                    month = "0"+month;
                                }
                                FirebaseOperation.moveNote(moveKey, day, month, year);
                                Toast.makeText(getActivity(), "Note added to calendar.", Toast.LENGTH_LONG).show();
                            }
                        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.show();

                        break;
                    case 1:
                        final String editKey = item.getId();
                        final String editTitle = item.getTitle();
                        final String editNote = item.getNote();

                        Intent intent = new Intent(getActivity(), EditActivity.class);
                        intent.putExtra("editKey", editKey);
                        intent.putExtra("editTitle", editTitle);
                        intent.putExtra("editNote", editNote);
                        getActivity().startActivity(intent);
                        break;
                    case 2:
                        final String removeKey = item.getId();

                        DialogPlus removeDialog = DialogPlus.newDialog(getActivity())
                                .setHeader(R.layout.remove_note_header)
                                .setExpanded(true, 280)
                                .setContentHolder(new ViewHolder(R.layout.remove_note_dialog))
                                .setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(DialogPlus dialog, View view) {
                                        switch(view.getId()){
                                            case R.id.btnRemoveNote:
                                                FirebaseOperation.removeNote(removeKey);
                                                notes.clear();
                                                noteAdapter.notifyDataSetChanged();
                                                dialog.dismiss();
                                                Toast.makeText(getActivity(), "Note removed.", Toast.LENGTH_LONG).show();
                                                break;
                                            case R.id.btnNo:
                                                dialog.dismiss();
                                                break;
                                        }
                                    }
                                })
                                .create();
                        removeDialog.show();
                        break;
                }

                return false;
            }

        });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.fab:
                Intent intent = new Intent(getActivity(), AddActivity.class);
                getActivity().startActivity(intent);
                break;
        }
    }
}
