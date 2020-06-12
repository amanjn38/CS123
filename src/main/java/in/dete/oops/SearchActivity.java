package in.dete.oops;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private ImageView home, cart, profile, order;
    private RecyclerView recyclerView;
    private android.widget.SearchView searchView;
    private SearchAdapter myAdapter;
    private ArrayList<Restaurant> restaurants;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        home = findViewById(R.id.home);
        home.setOnClickListener(v -> {
            startActivity(new Intent(SearchActivity.this, HomeActivity.class));
        });
        cart = findViewById(R.id.cart);
        findViewById(R.id.cart).setOnClickListener(v -> {
            startActivity(new Intent(SearchActivity.this, ShowCartActivity.class));
        });
        order = findViewById(R.id.order);

        profile = findViewById(R.id.profile);
        order.setOnClickListener(v -> {
            startActivity(new Intent(SearchActivity.this, OrderActivity.class));
        });

        profile.setOnClickListener(v -> {
            startActivity(new Intent(SearchActivity.this, ProfileActivity.class));
        });
        restaurants = new ArrayList<>();

        restaurants.add(new Restaurant("Alankrita", "", "veg", "4.5", null, "K50DqL4wnZZ9L3UiSptkbABxbb13"));
        restaurants.add(new Restaurant("Tandoor", "", "veg", "3.5", null, "yyWfojsakrbnk60FChJHDTk2jP42"));
        restaurants.add(new Restaurant("Saffron Mantra", "non-veg", "", "5.0", null, "y483SzakrxgSLBtEgVPJ7NZdD6c2"));
        restaurants.add(new Restaurant("Evergreen 9", "non-veg", "", "4.5", null, "aNgBGYpptIbi8MO8dNNFQyCjhE13"));
        restaurants.add(new Restaurant("Viceroy", "", "veg", "2.5", null, "pkwGWo0y7EUALJ8gzH3BRwOxOGv1"));

        myAdapter = new SearchAdapter(SearchActivity.this, restaurants);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        recyclerView.setAdapter(myAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.actionSearch);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                myAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
}
