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
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.untitledhorton.projectarchive.EditActivity;
import com.untitledhorton.projectarchive.NoteDetailActivity;
import com.untitledhorton.projectarchive.R;
import com.untitledhorton.projectarchive.model.Note;
import com.untitledhorton.projectarchive.utility.CustomNoteAdapter;
import com.untitledhorton.projectarchive.utility.FirebaseCommand;
import com.untitledhorton.projectarchive.utility.FirebaseOperation;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;

public class MonthLogFragment extends Fragment implements FirebaseCommand {
    private ArrayList<Note> notes;
    private TextView tvEmpty;
    private Spinner spinner;
    private SwipeMenuListView lvNotes;
    private CustomNoteAdapter noteAdapter;
    private ProgressBar pb;
    private String day, month, year, selectedMonth, searchText;
    private EditText etSearch;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_month_log, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Month Log");

        lvNotes = view.findViewById(R.id.lvReminders);
        pb = view.findViewById(R.id.pb);
        tvEmpty = view.findViewById(R.id.tvEmpty);
        spinner = view.findViewById(R.id.spinner);
        etSearch = view.findViewById(R.id.etSearch);
        notes = new ArrayList<Note>();
        noteAdapter = new CustomNoteAdapter(getActivity(), notes);

        lvNotes.setEmptyView(tvEmpty);

        lvNotes.setAdapter(noteAdapter);
        swipeMenuCreator(lvNotes);

        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.months));
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(monthAdapter);

        Calendar cal = Calendar.getInstance();

        selectedMonth = new DateFormatSymbols().getMonths()[cal.get(Calendar.MONTH)];
        int spinnerPosition = monthAdapter.getPosition(selectedMonth);
        spinner.setSelection(spinnerPosition);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("month selected: " + adapterView.getSelectedItem().toString());
                switch(adapterView.getSelectedItem().toString()){
                    case "January":
                        selectedMonth = "01";
                        break;
                    case "February":
                        selectedMonth = "02";
                        break;
                    case "March":
                        selectedMonth = "03";
                        break;
                    case "April":
                        selectedMonth = "04";
                        break;
                    case "May":
                        selectedMonth = "05";
                        break;
                    case "June":
                        selectedMonth = "06";
                        break;
                    case "July":
                        selectedMonth = "07";
                        break;
                    case "August":
                        selectedMonth = "08";
                        break;
                    case "September":
                        selectedMonth = "09";
                        break;
                    case "October":
                        selectedMonth = "10";
                        break;
                    case "November":
                        selectedMonth = "11";
                        break;
                    case "December":
                        selectedMonth = "12";
                        break;
                }
                FirebaseOperation.retrieveMonth(pb, notes, noteAdapter, tvEmpty, selectedMonth);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
                FirebaseOperation.searchRetrievedMonth(pb, notes, noteAdapter, tvEmpty, selectedMonth, searchText);
            }
        });

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

    }

    public void swipeMenuCreator(SwipeMenuListView lvReminders){

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem doneNote = new SwipeMenuItem(
                        getActivity());
                doneNote.setBackground(new ColorDrawable(Color.rgb(153, 255, 153)));
                doneNote.setWidth(200);
                doneNote.setIcon(R.drawable.ic_check_black_32dp);
                doneNote.setTitleSize(18);
                doneNote.setTitleColor(Color.WHITE);
                menu.addMenuItem(doneNote);

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

        lvReminders.setMenuCreator(creator);
        lvReminders.setCloseInterpolator(new BounceInterpolator());

        lvReminders.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Note item = notes.get(position);
                switch (index) {
                    case 0:
                        final String doneKey = item.getId();

                        DialogPlus doneDialog = DialogPlus.newDialog(getActivity())
                                .setHeader(R.layout.done_note_header)
                                .setExpanded(true, 280)
                                .setContentHolder(new ViewHolder(R.layout.done_note_dialog))
                                .setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(DialogPlus dialog, View view) {
                                        switch(view.getId()){
                                            case R.id.btnDone:
                                                FirebaseOperation.doneNote(doneKey);
                                                notes.clear();
                                                noteAdapter.notifyDataSetChanged();
                                                dialog.dismiss();
                                                break;
                                            case R.id.btnCancel:
                                                dialog.dismiss();
                                                break;
                                        }
                                    }
                                })
                                .create();
                        doneDialog.show();

                        break;
                    case 1:
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
                                Toast.makeText(getActivity(), "Changed date of note.", Toast.LENGTH_LONG).show();
                            }
                        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.show();

                        break;
                    case 2:
                        final String editKey = item.getId();
                        final String editTitle = item.getTitle();
                        final String editNote = item.getNote();

                        Intent intent = new Intent(getActivity(), EditActivity.class);
                        intent.putExtra("editKey", editKey);
                        intent.putExtra("editTitle", editTitle);
                        intent.putExtra("editNote", editNote);
                        getActivity().startActivity(intent);
                        break;
                    case 3:
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
}
