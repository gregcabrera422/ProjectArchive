package com.untitledhorton.projectarchive;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.picasso.Picasso;
import com.untitledhorton.projectarchive.fragment.CalendarFragment;
import com.untitledhorton.projectarchive.fragment.ClassroomFragment;
import com.untitledhorton.projectarchive.fragment.DashboardFragment;
import com.untitledhorton.projectarchive.fragment.MonthLogFragment;
import com.untitledhorton.projectarchive.fragment.NotesFragment;
import com.untitledhorton.projectarchive.fragment.ProfileFragment;
import com.untitledhorton.projectarchive.fragment.SharedNotesFragment;
import com.untitledhorton.projectarchive.utility.FirebaseOperation;
import com.untitledhorton.projectarchive.utility.NotificationService;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ImageView ivUser;
    TextView tvName, tvEmail;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private static Context context;
    Fragment fragment = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        tvName = hView.findViewById(R.id.tvName);
        tvEmail = hView.findViewById(R.id.tvEmail);
        ivUser = hView.findViewById(R.id.ivProfile);

        String name = user.getDisplayName();
        String email = user.getEmail();
        Uri photoUrl = user.getPhotoUrl();

        Picasso.get().load(photoUrl).into(ivUser);
        tvName.setText(name);
        tvEmail.setText(email);

        navigationView.setNavigationItemSelectedListener(this);

        context = getApplicationContext();

        FirebaseOperation.retrieveNumberOfNotes();

        fragment = new DashboardFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        ft.replace(R.id.screen_area, fragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            fragment = new DashboardFragment();
        } else if (id == R.id.nav_notes) {
            fragment = new NotesFragment();
        }else if (id == R.id.nav_monthly_log) {
            fragment = new MonthLogFragment();
        } else if (id == R.id.nav_calendar) {
            fragment = new CalendarFragment();
        } else if (id == R.id.nav_classroom) {
            fragment = new ClassroomFragment();
        } else if (id == R.id.nav_profile) {
            fragment = new ProfileFragment();
        } else if (id == R.id.nav_sharedNotes) {
            fragment = new SharedNotesFragment();
        } else if (id == R.id.nav_logout) {
            DialogPlus removeDialog = DialogPlus.newDialog(this)
                    .setHeader(R.layout.confirmation_header)
                    .setExpanded(true, 350)
                    .setContentHolder(new ViewHolder(R.layout.confirmation_dialog))
                    .setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(DialogPlus dialog, View view) {
                            switch(view.getId()){
                                case R.id.btnYes:
                                    FirebaseAuth.getInstance().signOut();

                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(intent);
                                    dialog.dismiss();
                                    break;
                                case R.id.btnNo:
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    })
                    .setGravity(Gravity.CENTER)
                    .create();
            removeDialog.show();
        }

        if(fragment != null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();

            ft.replace(R.id.screen_area, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static void notification(Calendar calendar, String message) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        Intent intent = new Intent(context.getApplicationContext(), NotificationService.class);
        intent.putExtra("message", message);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 60 * 6, pendingIntent);

    }


}
