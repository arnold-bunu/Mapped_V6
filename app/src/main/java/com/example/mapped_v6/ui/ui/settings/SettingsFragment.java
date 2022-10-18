package com.example.mapped_v6.ui.ui.settings;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mapped_v6.R;
import com.example.mapped_v6.databinding.FragmentSettingsBinding;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {


    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabase = database.getReference("Users");
    private String userId;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    private String distMeasureSystem;
    String LandmarkType;
    private RadioGroup distanceRg;
    private RadioButton imperialRb, metricRb, measurementRb;
    private Button btnSave;
    private Spinner spLandMarkType;
    private String gType;
    private String[] gTypes;
    private FragmentSettingsBinding binding;
    private ui.settings.SettingsViewModel settingsViewModel;
    private Spinner spLandMrkType;


    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        settingsViewModel = new ViewModelProvider(this).get(ui.settings.SettingsViewModel.class);
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Firebase




        imperialRb = (RadioButton) getView().findViewById(R.id.imperialRb);
        metricRb = (RadioButton) getView().findViewById(R.id.metricRb);
        distanceRg = (RadioGroup) getView().findViewById(R.id.distanceRg);

        imperialRb.isChecked();





        spLandMarkType = (Spinner) getView().findViewById(R.id.spLandMarkType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.landmarktypes, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spLandMarkType.setAdapter(adapter);
        spLandMarkType.setOnItemSelectedListener(this);

        gTypes = getContext().getResources().getStringArray(R.array.gtypes);
        settingsInput();

        btnSave = (Button) getView().findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsUpdate();
            }
        });


    }



    private void settingsUpdate() {

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();;
        userId=user.getUid();


        int distanceId = distanceRg.getCheckedRadioButtonId();
        measurementRb = (RadioButton) getView().findViewById(distanceId);

        mDatabase.child("Users/").child(userId).child("metricImperial").child("Type").setValue(measurementRb.getText().toString());


        String test3=spLandMarkType.getSelectedItem().toString();



        mDatabase.child("Users/").child(userId).child("LandmarkType").child("Landmark").setValue(test3);
        Toast.makeText(getActivity(), "settings Updated! ", Toast.LENGTH_SHORT).show();






    }


    private void settingsInput() {
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        userId=user.getUid();
        mDatabase=FirebaseDatabase.getInstance().getReference();

        mDatabase.child(userId).child("metricImperial").child("Type");

        String x = mDatabase.child(userId).child("metricImperial").toString();


        mDatabase.child("Users").child(userId).child("metricImperial").child("Type").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());

                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));

                    String test=String.valueOf(task.getResult().getValue());
                    String met="Metric";
                    String imp="Imperial";


                         if(test.equals(met)){


                             Toast.makeText(getActivity(), "Chosen Distance unit: Metric ", Toast.LENGTH_SHORT).show();
                    }
                         else if (test.equals(imp)){


                             Toast.makeText(getActivity(), "Chosen Distance unit: Imperial ", Toast.LENGTH_SHORT).show();
                    }
                         else{

                             Toast.makeText(getActivity(), "No Settings Detected! Please save", Toast.LENGTH_SHORT).show();
                         }

                }
            }
        });




        mDatabase.child("Users/").child(userId).child("LandmarkType").child("Landmark").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()){
                    Log.e("firebase", "Error getting data", task.getException());

                }
                else{
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    String test4=String.valueOf(task.getResult().getValue());

                    Toast.makeText(getActivity(), "Current Preferred Landmark type: "+test4, Toast.LENGTH_LONG).show();
                }
            }
        });



    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        gTypes = getContext().getResources().getStringArray(R.array.gtypes);
//        gType = gTypes[position];
//        String x=gType.toString();
//        mDatabase=FirebaseDatabase.getInstance().getReference();
//        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();;
//        userId=user.getUid();
//
//
//        mDatabase.child("Users/").child(userId).child("LandmarkType").child("Landmark").setValue(x);


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
