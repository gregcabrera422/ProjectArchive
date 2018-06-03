package com.untitledhorton.projectarchive.utility;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.untitledhorton.projectarchive.MainActivity;
import com.untitledhorton.projectarchive.model.Note;
import com.untitledhorton.projectarchive.model.SharedNote;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


/**
 * Created by Greg on 03/05/2018.
 */

public class FirebaseOperation implements FirebaseCommand {

    public static void retrieveNotes(final ProgressBar pb, final ArrayList<Note> notes, final CustomNoteAdapter noteAdapter,
                                     final TextView tvEmpty){
        NOTES_TABLE.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Note note;
                notes.clear();
                for (DataSnapshot objSnapshot: snapshot.getChildren()) {
                    Object key = objSnapshot.getKey();

                    NOTES_TABLE.child(key.toString());
                    note = objSnapshot.getValue(Note.class);

                    if(note.getYear().equals("0")) {
                        note.setId(key.toString());
                        notes.add(note);
                    }

                    noteAdapter.notifyDataSetChanged();
                }
                pb.setVisibility(View.INVISIBLE);
                tvEmpty.setText("Add A Note");
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });
    }

    public static void insertNote(String title, String note, String priority){
        HashMap<String, Object> result = new HashMap<>();

        result.put("title", title);
        result.put("note", note);
        result.put("priority", priority);
        result.put("day", "0");
        result.put("month", "0");
        result.put("year", "0");
        result.put("status", "notDone");

        NOTES_TABLE.push().setValue(result).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                } else {
                }
            }
        });
    }

    public static void removeNote(String item){
        NOTES_TABLE.child(item).removeValue();
    }

    public static void moveNote(String item, String day, String month, String year){
        HashMap<String, Object> date = new HashMap<>();
        date.put("day", day);
        date.put("month", month);
        date.put("year", year);

        NOTES_TABLE.child(item).updateChildren(date);
    }

    public static void editNote(String title, String note, String priority, String item){
        HashMap<String, Object> updatedNote = new HashMap<>();
        updatedNote.put("title", title);
        updatedNote.put("note", note);
        updatedNote.put("priority", priority);

        NOTES_TABLE.child(item).updateChildren(updatedNote);
    }

    public static void retrieveNoteDates(final CompactCalendarView compactCalendar){
        NOTES_TABLE.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Note note;

                for (DataSnapshot objSnapshot: snapshot.getChildren()) {
                    Object key = objSnapshot.getKey();

                    NOTES_TABLE.child(key.toString());
                    note = objSnapshot.getValue(Note.class);
                    note.setId(key.toString());

                    String eventDate =  note.getMonth()+"/"+note.getDay()+"/"+note.getYear();

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");

                    try
                    {
                        Date date = simpleDateFormat.parse(eventDate);

                        switch(note.getPriority()){
                            case "High":
                                compactCalendar.addEvent(new Event(Color.RED, date.getTime(), note.getTitle() + "(High Priority): " + note.getNote()));
                                break;
                            case "Medium":
                                compactCalendar.addEvent(new Event(Color.rgb(255,165,0), date.getTime(), note.getTitle() + "(Medium Priority): " +note.getNote()));
                                break;
                            case "Low":
                                compactCalendar.addEvent(new Event(Color.YELLOW, date.getTime(), note.getTitle() + "(Low Priority): " +note.getNote()));
                                break;
                        }

                    }
                    catch (ParseException e)
                    {
                        System.out.println("Exception "+e);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });
    }

    public static void retrieveMonth(final ProgressBar pb, final ArrayList<Note> notes, final CustomNoteAdapter noteAdapter,
                                     final TextView tvEmpty, final String month){
        Calendar cal = Calendar.getInstance();
        final String year = Integer.toString(cal.get(Calendar.YEAR));

        NOTES_TABLE.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Note note;
                notes.clear();
                for (DataSnapshot objSnapshot: snapshot.getChildren()) {
                    Object key = objSnapshot.getKey();
                    NOTES_TABLE.child(key.toString());
                    note = objSnapshot.getValue(Note.class);

                    if(note.getMonth().equals(month)&&note.getYear().equals(year)) {
                        note.setId(key.toString());
                        notes.add(note);
                    }

                    noteAdapter.notifyDataSetChanged();
                }
                pb.setVisibility(View.INVISIBLE);
                tvEmpty.setText("You Have No Notes This Month");
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });
    }

    public static void searchNote(final ProgressBar pb, final ArrayList<Note> notes, final CustomNoteAdapter noteAdapter,
                                     final TextView tvEmpty, final String searchText){

        NOTES_TABLE.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Note note;
                notes.clear();
                for (DataSnapshot objSnapshot: snapshot.getChildren()) {
                    Object key = objSnapshot.getKey();

                    NOTES_TABLE.child(key.toString());
                    note = objSnapshot.getValue(Note.class);

                    if(note.getYear().equals("0")) {
                        if(note.getTitle().toLowerCase().startsWith(searchText.toLowerCase())){
                            note.setId(key.toString());
                            notes.add(note);
                        }
                    }

                    noteAdapter.notifyDataSetChanged();
                }
                pb.setVisibility(View.INVISIBLE);
                tvEmpty.setText("No Result");
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });
    }

    public static void searchRetrievedMonth(final ProgressBar pb, final ArrayList<Note> notes, final CustomNoteAdapter noteAdapter,
                                     final TextView tvEmpty, final String month, final String searchText){
        Calendar cal = Calendar.getInstance();
        final String year = Integer.toString(cal.get(Calendar.YEAR));

        NOTES_TABLE.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Note note;
                notes.clear();
                for (DataSnapshot objSnapshot: snapshot.getChildren()) {
                    Object key = objSnapshot.getKey();
                    NOTES_TABLE.child(key.toString());
                    note = objSnapshot.getValue(Note.class);

                    if(note.getMonth().equals(month)&&note.getYear().equals(year)) {
                        if(note.getTitle().toLowerCase().startsWith(searchText.toLowerCase())){
                            note.setId(key.toString());
                            notes.add(note);
                        }
                    }

                    noteAdapter.notifyDataSetChanged();
                }
                pb.setVisibility(View.INVISIBLE);
                tvEmpty.setText("No Result");
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });
    }

    public static void retrieveDailyNotes(final ProgressBar pb, final ArrayList<Note> notes, final CustomNoteAdapter noteAdapter,
                                     final TextView tvEmpty, final String day, final String month){
        Calendar cal = Calendar.getInstance();
        final String year = Integer.toString(cal.get(Calendar.YEAR));

        NOTES_TABLE.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Note note;
                notes.clear();
                for (DataSnapshot objSnapshot: snapshot.getChildren()) {
                    Object key = objSnapshot.getKey();
                    NOTES_TABLE.child(key.toString());
                    note = objSnapshot.getValue(Note.class);

                    if(note.getDay().equals(day)&&note.getMonth().equals(month)&&note.getYear().equals(year)) {
                        note.setId(key.toString());
                        notes.add(note);
                    }

                    noteAdapter.notifyDataSetChanged();
                }
                pb.setVisibility(View.INVISIBLE);
                tvEmpty.setText("No Notes for Today");
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });
    }

    public static void retrieveNumberOfNotes(){
        NOTES_TABLE.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Note note;

                int noteCtr = 0;
                Calendar calendar = Calendar.getInstance();

                String month = "";
                String day = "";
                for (DataSnapshot objSnapshot: snapshot.getChildren()) {
                    Object key = objSnapshot.getKey();

                    NOTES_TABLE.child(key.toString());
                    note = objSnapshot.getValue(Note.class);
                    note.setId(key.toString());

                    if(calendar.get(Calendar.DAY_OF_MONTH)<10){
                        day = "0"+(calendar.get(Calendar.DAY_OF_MONTH));
                    }else{
                        day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
                    }

                    if(calendar.get(Calendar.MONTH)<10){
                        month = "0"+(calendar.get(Calendar.MONTH)+1);

                    }else{
                        month = Integer.toString(calendar.get(Calendar.MONTH)+1);
                    }

                    if(note.getDay().equals(day)&&note.getMonth().equals(month)&&note.getStatus().equals("notDone")){
                        ++noteCtr;
                    }

                }

                calendar.set(Calendar.HOUR_OF_DAY, 6);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);

                if(noteCtr == 0){
                    MainActivity.notification(calendar, "You have no note/s to do. Good job!");
                } else{
                    MainActivity.notification(calendar, "You have " + noteCtr + " note/s to do.");
                }
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });
    }

    public static void doneNote(String item){
        HashMap<String, Object> updatedNote = new HashMap<>();
        updatedNote.put("status", "done");

        NOTES_TABLE.child(item).updateChildren(updatedNote);
    }

    public static void insertSharedNote(String owner, String title, String note, String priority, String[] receivers){
        HashMap<String, Object> result = new HashMap<>();

        result.put("owner", owner);
        result.put("title", title);
        result.put("note", note);
        result.put("priority", priority);
        result.put("status", "notDone");

        for (String receiver: receivers) {
            int index = receiver.indexOf('@');

            SHARED_NOTES_TABLE.child(receiver.substring(0,index)).push().setValue(result).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                } else {
                }
            }
        });
        }

    }

    public static void retrieveSharedNotes(final ProgressBar pb, final ArrayList<SharedNote> sharedNotes, final CustomSharedNoteAdapter noteAdapter,
                                     final TextView tvEmpty){
        RETRIEVE_SHARED_NOTES_TABLE.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                SharedNote sharedNote;
                sharedNotes.clear();
                for (DataSnapshot objSnapshot: snapshot.getChildren()) {
                    Object key = objSnapshot.getKey();

                    RETRIEVE_SHARED_NOTES_TABLE.child(key.toString());
                    sharedNote = objSnapshot.getValue(SharedNote.class);

                    sharedNote.setId(key.toString());
                    sharedNotes.add(sharedNote);

                    noteAdapter.notifyDataSetChanged();
                }
                pb.setVisibility(View.INVISIBLE);
                tvEmpty.setText("No Shared Notes");
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });
    }

    public static void removeSharedNote(String item){
        RETRIEVE_SHARED_NOTES_TABLE.child(item).removeValue();
    }

    public static void searchSharedNote(final ProgressBar pb, final ArrayList<SharedNote> sharedNotes, final CustomSharedNoteAdapter noteAdapter,
                                  final TextView tvEmpty, final String searchText){

        RETRIEVE_SHARED_NOTES_TABLE.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                SharedNote sharedNote;
                sharedNotes.clear();
                for (DataSnapshot objSnapshot: snapshot.getChildren()) {
                    Object key = objSnapshot.getKey();

                    NOTES_TABLE.child(key.toString());
                    sharedNote = objSnapshot.getValue(SharedNote.class);

                    if(sharedNote.getTitle().toLowerCase().startsWith(searchText.toLowerCase())){
                        sharedNote.setId(key.toString());
                        sharedNotes.add(sharedNote);
                    }

                    noteAdapter.notifyDataSetChanged();
                }
                pb.setVisibility(View.INVISIBLE);
                tvEmpty.setText("No Result");
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });
    }

}
