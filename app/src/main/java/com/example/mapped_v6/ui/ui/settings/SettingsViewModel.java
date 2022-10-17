package ui.settings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SettingsViewModel extends ViewModel {

    private MutableLiveData<String> distanceText;

    public SettingsViewModel()
    {
        distanceText = new MutableLiveData<>();
        distanceText.setValue("Settings fragment");
    }

    public LiveData<String> getText()
    {
        return distanceText;
    }
}
