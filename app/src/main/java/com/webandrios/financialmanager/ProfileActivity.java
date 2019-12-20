package com.webandrios.financialmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.flarebit.flarebarlib.FlareBar;
import com.flarebit.flarebarlib.Flaretab;
import com.flarebit.flarebarlib.TabEventObject;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.webandrios.financialmanager.Fragments.DebtsFragment;
import com.webandrios.financialmanager.Fragments.ExpenseFragment;
import com.webandrios.financialmanager.Fragments.HomeFragment;
import com.webandrios.financialmanager.Fragments.IncomeFragment;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;
    FlareBar bottomBar;
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
        flareBar();
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        String uid = firebaseUser.getUid();
        Toast.makeText(this, "UID is : "+uid, Toast.LENGTH_SHORT).show();
        Log.v("UID",""+uid);
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

}
