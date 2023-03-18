package com.ai.game.kshudha.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ai.game.kshudha.Adapter.NgoAdapter;
import com.ai.game.kshudha.Model.Ngo;
import com.ai.game.kshudha.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class HomeFragment extends Fragment {
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    RecyclerView recyclerView;
    ArrayList<Ngo> ngos;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ArrayList<Ngo> list = new ArrayList<>();
        list.add(new Ngo("A NGO", "76sf89s7d6fd6f9s8", "skdjvsdjvsbvs", 22.543022, 76.442187, new ArrayList<String>(Collections.singleton("Food, Clothing"))));
        list.add(new Ngo("B NGO", "5345987nf4fd234dd", "skdjvsdjvsbvs", 19.445896, 80.331347, new ArrayList<String>(Collections.singleton("School Supplies, Clothing"))));
        list.add(new Ngo("C NGO", "76sf89s7d6fd6f9s8", "skdjvsdjvsbvs", 19.963045, 78.101122, new ArrayList<String>(Collections.singleton("Toys, School Supplies"))));
        list.add(new Ngo("D NGO", "76sf89s7d6fd6f9s8", "skdjvsdjvsbvs", 22.766073, 75.332568, new ArrayList<String>(Collections.singleton("Clothing"))));
        list.add(new Ngo("E NGO", "76sf89s7d6fd6f9s8", "skdjvsdjvsbvs", 27.595955, 81.100390, new ArrayList<String>(Collections.singleton("Food"))));
        list.add(new Ngo("F NGO", "76sf89s7d6fd6f9s8", "skdjvsdjvsbvs", 18.469211, 82.330858, new ArrayList<String>(Collections.singleton("School Supplies"))));

        Button button = view.findViewById(R.id.button);
//        button.setEnabled(false);
        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i < list.size(); i++){
                    database.getReference().child("NGO").push().setValue(list.get(i));
                }
            }
        });


        recyclerView = view.findViewById(R.id.ngos_rv);
        ngos = new ArrayList<>();

        NgoAdapter adapter = new NgoAdapter(ngos, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        database.getReference().child("NGO").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    ngos.clear();
                    for(DataSnapshot snap : snapshot.getChildren()){
                        Ngo data = snap.getValue(Ngo.class);
                        ngos.add(data);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recyclerView.setAdapter(adapter);
        return view;
    }
}