package in.dete.oops;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MenuActivity extends AppCompatActivity implements RecyclerViewClickInterface {

    private Double total = 0.0;
    private HashMap<String, Cart> odr;
    private String restaurantName, uid;
    private Location selectedLocation;
    private RecyclerView recyclerView;
    private String lat = "", lon = "";
    private MenuAdapter myAdapter;
    private ArrayList<MenuModel> menuModels;
    private TextView itemCount, totalPrice, viewCart, address, change;
    private String a = "";
    private int selection = 1;
    private static int AUTOCOMPLETE_PLACES = 502;
    private ArrayList<Cart> carts;
    private ImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        carts = new ArrayList<>();

        change = findViewById(R.id.change);
        address = findViewById(R.id.address);
        image = findViewById(R.id.image);
        restaurantName = getIntent().getStringExtra("restaurants");
        uid = getIntent().getStringExtra("uid");

        if (restaurantName.equals("Alankrita")) {
            image.setImageResource(R.drawable.restaurant1);
        } else if (restaurantName.equals("Tandoor")) {
            image.setImageResource(R.drawable.restaurant2);
        } else if (restaurantName.equals("Saffron Mantra")) {
            image.setImageResource(R.drawable.restaurant3);
        } else if (restaurantName.equals("Evergreen 9")) {
            image.setImageResource(R.drawable.restaurant4);
        } else if (restaurantName.equals("Viceroy")) {
            image.setImageResource(R.drawable.restaurant5);
        }

        menuModels = new ArrayList<>();
        itemCount = findViewById(R.id.itemCount);
        totalPrice = findViewById(R.id.totalPrice);
        viewCart = findViewById(R.id.viewCart);
        odr = new HashMap<>();

        Places.initialize(MenuActivity.this, "AIzaSyBXpHTrurGbpDlb6RXy5AisDcQkKxWiXyU");
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

        change.setOnClickListener(v -> {
            selection = 2;
            startActivityForResult(new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.OVERLAY, fields)
                    .setCountry("IN")
                    .build(MenuActivity.this), AUTOCOMPLETE_PLACES);
        });

        FirebaseFirestore.getInstance().collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult().size() > 0) {
                    for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                      if (doc.get("uid").equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                          if (task.isSuccessful()) {
                              address.setText(doc.get("a").toString());
                              a = doc.get("a").toString();
                              lat = doc.get("lt").toString();
                              lon = doc.get("lo").toString();
                              break;
                          }

                      }
                    }
                }
            }
        });


        menuModels.add(new MenuModel("Dosa", "150", "veg", null));
        menuModels.add(new MenuModel("Burger", "100", "veg", null));
        menuModels.add(new MenuModel("Pizza", "125", "non-veg", null));
        menuModels.add(new MenuModel("Chilli Paneer", "200", "veg", null));
        menuModels.add(new MenuModel("Choco Brownie", "250", "non-veg", null));

        viewCart.setOnClickListener(v -> {

            HashMap<String, Object> objectHashMap = new HashMap<>();
            objectHashMap.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
            objectHashMap.put("restaurantName", restaurantName);
            objectHashMap.put("restaurant-uid", uid);
            objectHashMap.put("items", Integer.toString(carts.size()));
            if (carts.size() > 0) {
                objectHashMap.put("total-price", Double.toString(carts.get(carts.size() - 1).getTp()));
            }
            objectHashMap.put("status", "Order Un-Placed");
            FirebaseFirestore.getInstance().collection("Cart").add(objectHashMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if (task.isSuccessful()) {
                    }
                }
            });

            Intent intent = new Intent(MenuActivity.this, CartActivity.class);
            intent.putExtra("order", carts);
            intent.putExtra("menus", menuModels);
            intent.putExtra("restaurantName", restaurantName);
            intent.putExtra("uid", uid);

            final String[] add = {""};
            final String[] lati = {""};
            final String[] longi = {""};

            FirebaseFirestore.getInstance().collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful() && task.getResult().size() > 0) {
                        for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                            if (doc.get("uid").equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                if (task.isSuccessful()) {
                                    add[0] = doc.get("a").toString();
                                    lati[0] = doc.get("lt").toString();
                                    longi[0] = doc.get("lo").toString();

                                    Log.d("adddress", add[0]);
                                    Log.d("lati", lati[0]);
                                    Log.d("i", longi[0]);

                                    if (selection == 1) {
                                        Log.d("adddress", add[0]);
                                        Log.d("lati", lati[0]);
                                        Log.d("i", longi[0]);
                                        intent.putExtra("latitude", lati[0]);
                                        intent.putExtra("longitude", longi[0]);
                                        intent.putExtra("address", add[0]);
                                    } else if (selection == 2) {
                                        intent.putExtra("latitude", selectedLocation.getLatitude());
                                        intent.putExtra("longitude", selectedLocation.getLongitude());
                                        intent.putExtra("address", add[0]);
                                    }

                                }
                            }
                        }
                    }
                }
            });
            startActivity(intent);
        });
        myAdapter = new MenuAdapter(MenuActivity.this, menuModels, this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(MenuActivity.this));
        recyclerView.setAdapter(myAdapter);
    }

    @Override
    public void onItemClick(int position) {
        total = total + Integer.parseInt(menuModels.get(position).getP());

        int dish = 0;
        carts.add(new Cart(menuModels.get(position).getN(), menuModels.get(position).getP(), total, FirebaseAuth.getInstance().getCurrentUser().getUid(), menuModels.get(position).getV()));
//        odr.put(menus.get(position).getN()+dish , new Cart(menus.get(position).getN(), menus.get(position).getP(),menus.get(position).getV(), FirebaseAuth.getInstance().getCurrentUser().getUid()));
//        Toast.makeText(MenuActivity.this, "Hi",Toast.LENGTH_LONG).show();
      //  total = total + Integer.parseInt(menuModels.get(position).getP());
        totalPrice.setText(Double.toString(total));
        if (carts.size() != 0) {
            itemCount.setText(Integer.toString(carts.size()));
            ++dish;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if ((requestCode == AUTOCOMPLETE_PLACES)) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.d("BidRyd", "Place: " + place.getName() + ", " + place.getId());
                address.setText(place.getName());
                a = place.getName();
                selectedLocation = new Location("");
                selectedLocation.setLatitude(place.getLatLng().latitude);
                selectedLocation.setLongitude(place.getLatLng().longitude);
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.d("BidRyd", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}