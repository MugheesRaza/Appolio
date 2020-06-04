package com.example.appolio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appolio.fragments.child_history;
import com.example.appolio.fragments.profile;
import com.example.appolio.fragments.send_mail;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;

public class drawer_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    String cnic ,username,emial;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);

        cnic = getIntent().getStringExtra("cnic");
        username = getIntent().getStringExtra("username");
        emial = getIntent().getStringExtra("userEmail");

       // Toast.makeText(getApplicationContext(),cnic,Toast.LENGTH_SHORT).show();

        NavigationView navigationView = findViewById(R.id.nav_view);
         navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView nav_email = (TextView) headerView.findViewById(R.id.emialNav);
        nav_email.setText(emial);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new profile()).commit();
            navigationView.setCheckedItem(R.id.userProfile);
        }

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.userProfile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new profile()).commit();
                break;
            case R.id.child_history:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new child_history()).commit();
                break;
            case R.id.addchild:
                Intent i = new Intent(getApplicationContext(),child_vaccine.class);
                i.putExtra("cnic",cnic);
                startActivity(i);
                break;

            case R.id.mail_parent:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new send_mail()).commit();
                break;

            case R.id.map:
                Intent imap = new Intent(getApplicationContext(),Map.class);
                startActivity(imap);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public String getCnic(){
        return cnic;
    }

    public String getUsername(){
        return username;
    }

    public String getEmial(){
        return emial;
    }
}
