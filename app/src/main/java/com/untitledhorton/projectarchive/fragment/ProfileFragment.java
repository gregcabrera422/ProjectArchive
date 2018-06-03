package com.untitledhorton.projectarchive.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.untitledhorton.projectarchive.R;

import java.util.ArrayList;

/**
 * Created by Greg on 23/05/2018.
 */

public class ProfileFragment extends Fragment{

    private TextView tvName, tvEmail;
    private ImageView ivProfile;
    private ArrayList<String> subject = new ArrayList<>();
    private ListView lvSubjects;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Profile");

        ivProfile = view.findViewById(R.id.ivProfile);
        tvName = view.findViewById(R.id.tvName);
        tvEmail = view.findViewById(R.id.tvEmail);
        lvSubjects = view.findViewById(R.id.lvSubjects);

        String name = user.getDisplayName();
        String email = user.getEmail();
        Uri photoUrl = user.getPhotoUrl();

        System.out.println("URL: "+ photoUrl);
        Picasso.get().load(photoUrl).into(ivProfile);
        tvName.setText(name);
        tvEmail.setText(email);

        subject.add("DATCOMM - Data Communications and Computer Networking");
        subject.add("HUCOMIN - Human-Computer Interaction");
        subject.add("ETHPRAC - Professional Ethics and Practices");
        subject.add("SELECT418 - Python (Elective 4)");
        subject.add("RIZLIFE - Rizal's Life, Works and Writings");
        subject.add("WDTHS2 - Web Development Thesis 2");
        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, subject);
        lvSubjects.setAdapter(subjectAdapter);
    }

}
