package in.dete.oops;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ShowCartActivity extends AppCompatActivity implements RecyclerViewClickInterface {

    private RecyclerView recyclerView;
    private ImageView image;
    private TextView text, carrt;
    private ShowCartAdapter myAdapter;
    private ArrayList<ShowCart> carts;
    private ImageView home, cart, profile, search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_cart);

        carts = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        image = findViewById(R.id.image);
        text = findViewById(R.id.text);
        home = findViewById(R.id.home);
        search = findViewById(R.id.search);
        cart = findViewById(R.id.cart);
        profile = findViewById(R.id.profile);

        carrt = findViewById(R.id.carrt);
        search.setOnClickListener(v -> {
            startActivity(new Intent(ShowCartActivity.this, SearchActivity.class));

        });
        findViewById(R.id.order).setOnClickListener(v -> {
            startActivity(new Intent(ShowCartActivity.this, IdkActivity.class));
        });
        profile.setOnClickListener(v -> {
            startActivity(new Intent(ShowCartActivity.this, ProfileActivity.class));
        });
        home.setOnClickListener(v -> {
            startActivity(new Intent(ShowCartActivity.this, HomeActivity.class));
        });


        FirebaseFirestore.getInstance().collection("Cart").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                if (doc.get("uid").toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                    carts.add(new ShowCart(doc.get("restaurantName").toString(), doc.get("total-price").toString(), doc.get("items").toString()));
                                    Log.d("carts_size", doc.get("restaurantName").toString());
                                }
                            }
                        }
                        if (carts.size() > 0) {
                            carrt.setVisibility(View.INVISIBLE);
                            image.setVisibility(View.INVISIBLE);
                            text.setVisibility(View.INVISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                            myAdapter = new ShowCartAdapter(ShowCartActivity.this, carts, ShowCartActivity.this);
                            recyclerView = findViewById(R.id.recyclerView);
                            recyclerView.setLayoutManager(new LinearLayoutManager(ShowCartActivity.this));
                            recyclerView.setAdapter(myAdapter);

                        }
                    }
                });

    }

    @Override
    public void onItemClick(int position) {

    }
}