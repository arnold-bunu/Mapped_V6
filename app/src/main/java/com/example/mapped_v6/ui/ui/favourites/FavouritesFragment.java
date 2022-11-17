package com.example.mapped_v6.ui.ui.favourites;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.mapped_v6.Favourites;
import com.example.mapped_v6.LandmarkAdapter;
import com.example.mapped_v6.R;
import com.example.mapped_v6.databinding.FragmentFavouritesBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavouritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavouritesFragment extends Fragment {

    private ui.favourites.FavViewModel favViewModel;
    private FragmentFavouritesBinding binding;
    private ListView listView;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    private Favourites favourites;
    public static ArrayList<Favourites> favouritesArrayList = new ArrayList<Favourites>();
    private LinearLayout.LayoutParams params;
    private LinearLayout lm;
    private String  userId;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabase = database.getReference("Users");
    private ListView saveListView;
    private ArrayList <String> arrayList1= new ArrayList<>();
    public FavouritesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //firebase
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
       userId = user.getUid();
        mDatabase= FirebaseDatabase.getInstance().getReference();


        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        favouriteList();
    }

    private void favouriteList() {
        //firebase

        mDatabase.child("/Users").child(userId).child("Favourites").child("").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());

                }
                else {


                    for (DataSnapshot snap : task.getResult().getChildren()) {


                        String Names= snap.getKey().toString();

                        arrayList1.add(Names);



                    }

                    ListView lvHomePage = (ListView) getActivity().findViewById(R.id.FavLandmarksList);


                    lvHomePage.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1 , arrayList1));
                }}});












                    createList();
        createClickListen();
    }

    private void createClickListen() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
           //    Favourites selectLandmark = (Favourites) (listView.getItemIdAtPosition(position));

            }
        });
    }

    private void createList() {
        if(getActivity()!=null) {
            listView = (ListView) getView().findViewById(R.id.FavLandmarksList);
            LandmarkAdapter adapter = new LandmarkAdapter (getActivity(), 0, favouritesArrayList);
            listView.setAdapter(adapter);

        }
    }

    public static FavouritesFragment newInstance(String param1, String param2) {
        FavouritesFragment fragment = new FavouritesFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        favViewModel = new ViewModelProvider(this).get(ui.favourites.FavViewModel.class);
        binding = FragmentFavouritesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }
}
