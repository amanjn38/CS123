package in.dete.oops.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Arrays;
import java.util.List;

import in.dete.oops.R;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private EditText aptAddress;
    public static int AUTOCOMPLETE_ACTIVITY = 180;
    private GeoPoint aptLoc;


    public ProfileFragment() {
        // Required empty public constructor
    }
        public View onCreateView(@NonNull LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_profile, container, false);

                aptAddress = view.findViewById(R.id.aptAddress);

//        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
//        Intent autocompleteIntent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(getContext());
//        try {
//            Places.initialize(getContext(), "AIzaSyCsTJ7aqC8aL0vdjFal4dob2NRp3BAxzj0");
//        }
//        catch (Exception e){}
            aptAddress.setOnClickListener(v ->{
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
                Intent autocompleteIntent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(getContext());

            });
//                startActivityForResult(new Autocomplete.IntentBuilder(
//                        AutocompleteActivityMode.OVERLAY, fields)
//                        .setCountry("IN")
//                        .build(getContext()), AUTOCOMPLETE_ACTIVITY));
            return view;
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
            if (requestCode == AUTOCOMPLETE_ACTIVITY) {
                if (resultCode == RESULT_OK) {
                    Place place = Autocomplete.getPlaceFromIntent(data);
                    aptAddress.setText(place.getName());
                    aptLoc = new GeoPoint(place.getLatLng().latitude, place.getLatLng().longitude);
                    // findViewById(R.id.addressExtraDetails).setVisibility(View.VISIBLE);
                } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                    Status status = Autocomplete.getStatusFromIntent(data);
                    Log.d("Address", status.getStatusMessage());
                }
            }
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
