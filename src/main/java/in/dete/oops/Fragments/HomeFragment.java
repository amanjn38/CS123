package in.dete.oops.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import in.dete.oops.R;
import in.dete.oops.Restaurant;
import in.dete.oops.RestaurantAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private RestaurantAdapter myAdapter;
    private ArrayList<Restaurant> restaurants;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        restaurants = new ArrayList<>();
//
//        restaurants.add(new Restaurant("Aman Jain", "","veg", "4.5", null ));
//        restaurants.add(new Restaurant("Aman Jain", "","veg", "4.5", null ));
//        restaurants.add(new Restaurant("Aman Jain", "","veg", "4.5", null ));
//        restaurants.add(new Restaurant("Aman Jain", "","veg", "4.5", null ));

        myAdapter = new RestaurantAdapter(getContext(), restaurants);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(myAdapter);

        return view;
    }

}
