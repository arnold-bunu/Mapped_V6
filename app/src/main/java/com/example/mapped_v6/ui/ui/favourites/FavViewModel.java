package ui.favourites;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FavViewModel extends ViewModel {

    private MutableLiveData<String> distanceText;

    public FavViewModel() {
        distanceText = new MutableLiveData<>();
        distanceText.setValue("Favourite Fragment");
    }

    public LiveData<String> getText(){
        return distanceText;
    }

}
