package com.webandrios.financialmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flarebit.flarebarlib.FlareBar;
import com.flarebit.flarebarlib.Flaretab;
import com.flarebit.flarebarlib.TabEventObject;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.webandrios.financialmanager.Fragments.DebtsFragment;
import com.webandrios.financialmanager.Fragments.ExpenseFragment;
import com.webandrios.financialmanager.Fragments.HomeFragment;
import com.webandrios.financialmanager.Fragments.IncomeFragment;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;
    FlareBar bottomBar;
    private DatabaseReference mDatabase;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    String uid,name,contact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        findViewById(R.id.buttonLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this, LoginScreen.class));
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference();
        drawerLayout = findViewById(R.id.drawerLayout);
        setNavigationViewListener();
        flareBar();
        setToolBar();
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        getUserDetails();
    }

    private void getUserDetails(){
        this.uid = firebaseUser.getUid();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                NavigationView navigationView = findViewById(R.id.navigation_view_drawer);
                View headerView = navigationView.getHeaderView(0);
                TextView profileContact = headerView.findViewById(R.id.profileContact);
                TextView profileName = headerView.findViewById(R.id.profileName);
                name = dataSnapshot.child("users").child(uid).child("name").getValue(String.class);
                contact = dataSnapshot.child("users").child(uid).child("contact").getValue(String.class);
                profileContact.setText(contact);
                profileName.setText(name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        };
        mDatabase.addValueEventListener(postListener);
    }
    private void setToolBar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeAsUpIndicator(R.drawable.list);
        actionbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
    }




    private void flareBar() {
        bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setBarBackgroundColor(Color.parseColor("#FFFFFF"));
        ArrayList<Flaretab> tabs = new ArrayList<>();
        tabs.add(new Flaretab(getResources().getDrawable(R.drawable.home),"Home","#FFECB3"));
        tabs.add(new Flaretab(getResources().getDrawable(R.drawable.income),"Income","#80DEEA"));
        tabs.add(new Flaretab(getResources().getDrawable(R.drawable.expense),"Expense","#B39DDB"));
        tabs.add(new Flaretab(getResources().getDrawable(R.drawable.debt),"Debts","#EF9A9A"));
        //tabs.add(new Flaretab(getResources().getDrawable(R.drawable.settingsb),"Credit","#B2DFDB"));

        bottomBar.setTabList(tabs);
        bottomBar.attachTabs(ProfileActivity.this);
        bottomBar.setTabChangedListener(new TabEventObject.TabChangedListener() {
            @Override
            public void onTabChanged(LinearLayout selectedTab, int selectedIndex, int oldIndex) {
                //tabIndex starts from 0 (zero). Example : 4 tabs = last Index - 3

                Fragment fragment = null;

                switch (selectedIndex) {
                    case 0:
                        fragment = new HomeFragment();
                        break;

                    case 1:
                        fragment = new IncomeFragment();
                        break;

                    case 2:
                        fragment = new ExpenseFragment();
                        break;

                    case 3:
                        fragment = new DebtsFragment();
                        break;
                    case 4:
                        //fragment = new ProfileFragment();
                        break;
                    case 5:
                        //fragment = new SettingsFragment();
                        break;
                }
                loadFragment(fragment);

            }
        });
        loadFragment(new HomeFragment());
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.containerParent, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

            case R.id.navAboutUs: {
                Toast.makeText(this, "About Us", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.navContactUs: {
                Toast.makeText(this, "Contact Us", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.navReportProblem: {
                Toast.makeText(this, "Report Problem", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.navSuggestions: {
                Toast.makeText(this, "Suggestions", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.navLogout: {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this,LoginScreen.class));
                break;

            }
        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    private void setNavigationViewListener() {
        NavigationView navigationView = findViewById(R.id.navigation_view_drawer);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
    }

}
