package com.webandrios.financialmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.flarebit.flarebarlib.FlareBar;
import com.flarebit.flarebarlib.Flaretab;
import com.flarebit.flarebarlib.TabEventObject;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.webandrios.financialmanager.Fragments.DebtsFragment;
import com.webandrios.financialmanager.Fragments.ExpenseFragment;
import com.webandrios.financialmanager.Fragments.HomeFragment;
import com.webandrios.financialmanager.Fragments.IncomeFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;
    FlareBar bottomBar;
    private DatabaseReference mDatabase;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    String uid,name,contact;
    //For profile image views
    ImageView  change_dp;
    CircleImageView profile_image;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        drawerLayout = findViewById(R.id.drawerLayout);
        setNavigationViewListener();
        flareBar();
        setToolBar();
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        getUserDetails();
    }
    private void setProfile_imageView(){
        NavigationView navigationView = findViewById(R.id.navigation_view_drawer);
        View headerView = navigationView.getHeaderView(0);
        profile_image = headerView.findViewById(R.id.profile_image);
        change_dp = headerView.findViewById(R.id.change_dp);
        change_dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
    }
    private void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profile_image.setImageBitmap(bitmap);
                uploadImage();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(){
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("users/"+uid+"/profileimage/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadUrl = uri.toString();
                                    database =  FirebaseDatabase.getInstance();
                                    DatabaseReference mRef =  database.getReference().child("users").child(uid);
                                    mRef.child("profileimage").setValue(downloadUrl);
                                    getUserDetails();
                                }
                            });
                            Toast.makeText(ProfileActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(ProfileActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }



    private void getUserDetails(){
        setProfile_imageView();
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
                String imageUrl = "";
                imageUrl = dataSnapshot.child("users").child(uid).child("profileimage").getValue(String.class);
                if(imageUrl != null && !imageUrl.isEmpty()){
                    Glide.with(ProfileActivity.this).load(imageUrl).into(profile_image);
                }
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
        actionbar.setHomeAsUpIndicator(R.drawable.menu);
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
                startActivity(new Intent(ProfileActivity.this,AboutActivity.class));
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
