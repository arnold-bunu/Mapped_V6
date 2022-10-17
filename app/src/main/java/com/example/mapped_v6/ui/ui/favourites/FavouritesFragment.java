package com.example.mapped_v6.ui.ui.favourites;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.mapped_v6.Favourites;
import com.example.mapped_v6.R;
import com.example.mapped_v6.databinding.FragmentFavouritesBinding;

import java.util.ArrayList;

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

    public FavouritesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //firebase

        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        favouriteList();
    }

    private void favouriteList() {
        //firebase
        
        createList();
        createClickListen();
    }

    private void createClickListen() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //Favourites selectLandmark = (Favourites) (listView.getItemIdAtPosition(position));

            }
        });
    }

    private void createList() {
        if(getActivity()!=null) {
            listView = (ListView) getView().findViewById(R.id.FavLandmarksList);
            //LandmarkAdapter adapter = new LandmarkAdapter (getActivity(), 0, favouritesArrayList);

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
