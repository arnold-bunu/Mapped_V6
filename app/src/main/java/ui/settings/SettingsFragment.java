package ui.settings;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment implements AdapterView.OnItemClickListener {

    private String distMeasureSystem;
    private RadioGroup distanceRg;
    private RadioButton imperialRb, metricRb, measurementRb;
    private Button btnSave;
    private Spinner spLandMarkType;
    private String gType;
    private String[] gTypes;
    private FragmentSettingsBinding binding;
    private SettingsViewModel settingsViewModel;


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
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
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

        //landmark type


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
        int distanceId = distanceRg.getCheckedRadioButtonId();
        measurementRb = (RadioButton) getView().findViewById(distanceId);
        //firebase
        Toast.makeText(getContext(), "Your settings have been updated", Toast.LENGTH_SHORT).show();
    }


    private void settingsInput() {
        //firebase
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}