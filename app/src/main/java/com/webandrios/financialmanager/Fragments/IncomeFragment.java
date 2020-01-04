package com.webandrios.financialmanager.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.webandrios.financialmanager.AddIncome;
import com.webandrios.financialmanager.ListForRecyclerView;
import com.webandrios.financialmanager.ProfileActivity;
import com.webandrios.financialmanager.R;

import de.hdodenhof.circleimageview.CircleImageView;


public class IncomeFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    /******************************************************************************/
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;
    String uid = "";
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        this.uid = firebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("income");
        databaseReference.keepSynced(true);
        recyclerView = getView().findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FloatingActionButton fab = getView().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                startActivity(new Intent(getActivity(), AddIncome.class));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<ListForRecyclerView,ListViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ListForRecyclerView, ListViewHolder>
                (ListForRecyclerView.class,R.layout.recyclerview_row,ListViewHolder.class,databaseReference) {
            @Override
            protected void populateViewHolder(ListViewHolder listViewHolder, ListForRecyclerView listForRecyclerView, int i) {


                listViewHolder.setName(listForRecyclerView.getName());
                listViewHolder.setDate(listForRecyclerView.getDate());
                listViewHolder.setAmount(listForRecyclerView.getAmount());
                listViewHolder.setImage(getContext(),listForRecyclerView.getImageUrl());



            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder{
        View view;
        public ListViewHolder(View itemView){
            super(itemView);
            view = itemView;
        }
        public void setName(String name){
            TextView incomeName = view.findViewById(R.id.incomeTitle);
            incomeName.setText(name);
        }
        public void setDate(String date){
            TextView incomeDate = view.findViewById(R.id.incomeDate);
            incomeDate.setText(date);
        }
        public void setAmount(String amount){
            TextView incomeAmount = view.findViewById(R.id.incomeAmount);
            incomeAmount.setText(amount);
        }
        public void setImage(Context ctx, String imageUrl){
            CircleImageView billImage = view.findViewById(R.id.billImage);
            if (!imageUrl.isEmpty()){
                billImage.setVisibility(View.VISIBLE);
                Glide.with(ctx).load(imageUrl).into(billImage);
            }
            else{
                billImage.setVisibility(View.GONE);
            }

        }
    }



    /*************************************************************************************/

    public IncomeFragment() {
        // Required empty public constructor
    }

    public static IncomeFragment newInstance(String param1, String param2) {
        IncomeFragment fragment = new IncomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_income, container, false);
    }






}
