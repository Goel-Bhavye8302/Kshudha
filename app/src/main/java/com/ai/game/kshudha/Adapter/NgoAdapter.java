package com.ai.game.kshudha.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ai.game.kshudha.Model.Firebase_User;
import com.ai.game.kshudha.Model.Ngo;
import com.ai.game.kshudha.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NgoAdapter extends RecyclerView.Adapter<NgoAdapter.ViewHolder>{
    ArrayList<Ngo> ngos;
    Context context;

    FirebaseAuth mAuth;
    FirebaseDatabase database;

    private static final double r2d = 180.0D / 3.141592653589793D;
    private static final double d2r = 3.141592653589793D / 180.0D;
    private static final double d2km = 111189.57696D * r2d;
    public static double distance(double lt1, double ln1, double lt2, double ln2) {
        double x = lt1 * d2r;
        double y = lt2 * d2r;
        return (Math.acos( Math.sin(x) * Math.sin(y) + Math.cos(x) * Math.cos(y) * Math.cos(d2r * (ln1 - ln2))) * d2km)/1000;
    }

    public NgoAdapter(ArrayList<Ngo> ngos, Context context) {
        this.ngos = ngos;
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    @NonNull
    @Override
    public NgoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ngo_data, parent, false);
        return new NgoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NgoAdapter.ViewHolder holder, int position) {
        Ngo item = ngos.get(position);
        holder.name.setText(item.getName());
        holder.description.setText(item.getDescription());

        database.getReference().child("Users").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Firebase_User user = snapshot.getValue(Firebase_User.class);
                double temp = distance(user.getLat(), user.getLon(), item.getLat(), item.getLon());
                holder.distance.setText(Long.toString(Math.round(temp)) + " KM");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        String s = "";
        for(int i = 0; i < item.getType().size(); i++){
            s += item.getType().get(i);
            s += ", ";
        }
        holder.categories.setText(s);

    }

    @Override
    public int getItemCount() {
        return ngos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name, description, categories, distance;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.ngo_name);
            description = itemView.findViewById(R.id.description);
            categories = itemView.findViewById(R.id.categories);
            distance = itemView.findViewById(R.id.distance);
        }
    }
}
