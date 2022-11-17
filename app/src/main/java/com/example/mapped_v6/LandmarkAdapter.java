package com.example.mapped_v6;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class LandmarkAdapter extends ArrayAdapter<Favourites> {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabase = database.getReference("Users");
    private String id;

    public LandmarkAdapter(@NonNull Context context, int resource, ArrayList<Favourites> favouritesArrayList) {
        super(context, resource);
    }

    @Override
    public View getView (int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Favourites favourites = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.fragment_show_information, parent, false);
        }
        id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        TextView favName = (TextView) convertView.findViewById(R.id.favName);
        TextView favAddress = (TextView) convertView.findViewById(R.id.favAddress);

        Button btnRemove = (Button) convertView.findViewById(R.id.btnRemove);

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   mDatabase.child(id).child()
            }
        });

        favName.setText(favourites.placeName);
        favAddress.setText(favourites.placeID);

        return convertView;
    }

}
