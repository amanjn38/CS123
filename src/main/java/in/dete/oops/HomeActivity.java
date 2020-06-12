package in.dete.oops;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    private NavController navController;
    private User user;
    private LinearLayout linearLayout;
    private ImageView home, cart, profile, search;
    CardView welcome;

    private RecyclerView recyclerView;
    private RestaurantAdapter myAdapter;
    private ArrayList<Restaurant> restaurants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        pref.edit().putBoolean("isIntroOpened", true).apply();

        welcome = findViewById(R.id.card);
        home = findViewById(R.id.home);
        search = findViewById(R.id.search);
        cart = findViewById(R.id.cart);
        profile = findViewById(R.id.profile);

        search.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, SearchActivity.class));

        });
        findViewById(R.id.order).setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, IdkActivity.class));
        });
        profile.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
        });
        cart.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, ShowCartActivity.class));
        });

        user = (User) getIntent().getSerializableExtra("user");
        if (user == null) {
            String userInfo = getSharedPreferences("MyPrefs", MODE_PRIVATE).getString("loggedInUser", "");
            if (!userInfo.equals("")) user = new Gson().fromJson(userInfo, User.class);
        }

        restaurants = new ArrayList<>();

        restaurants.add(new Restaurant("Alankrita", "", "veg", "4.5", null, "K50DqL4wnZZ9L3UiSptkbABxbb13"));
        restaurants.add(new Restaurant("Tandoor", "", "veg", "3.5", null, "yyWfojsakrbnk60FChJHDTk2jP42"));
        restaurants.add(new Restaurant("Saffron Mantra", "non-veg", "", "5.0", null, "y483SzakrxgSLBtEgVPJ7NZdD6c2"));
        restaurants.add(new Restaurant("Evergreen 9", "non-veg", "", "4.5", null, "aNgBGYpptIbi8MO8dNNFQyCjhE13"));
        restaurants.add(new Restaurant("Viceroy", "", "veg", "2.5", null, "pkwGWo0y7EUALJ8gzH3BRwOxOGv1"));

        myAdapter = new RestaurantAdapter(HomeActivity.this, restaurants);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
        recyclerView.setAdapter(myAdapter);

//        btnLogout = findViewById(R.id.btnLogout);
//
//        btnLogout.setOnClickListener(view -> {
//
//        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    public boolean checkPermission(String permission) {
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

}
