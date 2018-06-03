package com.untitledhorton.projectarchive.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.picasso.Picasso;
import com.untitledhorton.projectarchive.EditActivity;
import com.untitledhorton.projectarchive.NoteDetailActivity;
import com.untitledhorton.projectarchive.R;
import com.untitledhorton.projectarchive.SharedNoteDetailActivity;
import com.untitledhorton.projectarchive.model.Note;
import com.untitledhorton.projectarchive.model.SharedNote;
import com.untitledhorton.projectarchive.utility.CustomNoteAdapter;
import com.untitledhorton.projectarchive.utility.CustomSharedNoteAdapter;
import com.untitledhorton.projectarchive.utility.FirebaseOperation;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Greg on 23/05/2018.
 */

public class SharedNotesFragment extends Fragment{

    private ArrayList<SharedNote> sharedNotes;
    private SwipeMenuListView lvNotes;
    private CustomSharedNoteAdapter noteAdapter;
    private ProgressBar pb;
    private String day, month, year, searchText;
    private TextView tvEmpty;
    private EditText etSearch;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shared_notes, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Shared Notes");

        lvNotes = view.findViewById(R.id.lvReminders);
        pb = view.findViewById(R.id.pb);
        tvEmpty = view.findViewById(R.id.tvEmpty);
        etSearch = view.findViewById(R.id.etSearch);
        sharedNotes = new ArrayList<SharedNote>();

        noteAdapter = new CustomSharedNoteAdapter(getActivity(), sharedNotes);
        lvNotes.setEmptyView(tvEmpty);

        FirebaseOperation.retrieveSharedNotes(pb, sharedNotes, noteAdapter, tvEmpty);

        lvNotes.setAdapter(noteAdapter);

        swipeMenuCreator(lvNotes);

        lvNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                SharedNote item = sharedNotes.get(position);

                Intent intent = new Intent(getActivity(), SharedNoteDetailActivity.class);
                intent.putExtra("title", item.getTitle());
                intent.putExtra("owner", item.getOwner());
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
                FirebaseOperation.searchSharedNote(pb, sharedNotes, noteAdapter, tvEmpty, searchText);
            }
        });
    }

    public void swipeMenuCreator(SwipeMenuListView lvNotes){

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
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
                SharedNote item = sharedNotes.get(position);

                switch (index) {
                    case 0:
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
                                                FirebaseOperation.removeSharedNote(removeKey);
                                                sharedNotes.clear();
                                                noteAdapter.notifyDataSetChanged();
                                                dialog.dismiss();
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

}
