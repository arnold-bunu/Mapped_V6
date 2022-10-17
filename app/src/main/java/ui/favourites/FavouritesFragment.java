package ui.favourites;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private  FavViewModel favViewModel;
    private FragmentFavouritesBinding binding;
    private ListView FavLandmarksList;
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
    }

    private void createList() {
        if(getActivity()!=null) {
            FavLandmarksList = (ListView) getView().findViewById(R.id.FavLandmarksList);


            ////////////////////////




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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourites, container, false);
    }
}