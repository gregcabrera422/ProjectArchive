package com.untitledhorton.projectarchive.utility;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Greg on 03/05/2018.
 */

public interface FirebaseCommand {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String email = user.getEmail();
    int index = email.indexOf('@');

    DatabaseReference NOTES_TABLE = FirebaseDatabase.getInstance().getReference().child("Notes").child(user.getUid());
    DatabaseReference SHARED_NOTES_TABLE = FirebaseDatabase.getInstance().getReference().child("SharedNotes");
    DatabaseReference RETRIEVE_SHARED_NOTES_TABLE = FirebaseDatabase.getInstance().getReference().child("SharedNotes").child(email.substring(0,index));
}
