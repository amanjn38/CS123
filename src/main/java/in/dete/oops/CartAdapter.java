package in.dete.oops;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>{
    private Context context;
    private RecyclerViewClickInterface recyclerViewClickInterface;
    private ArrayList<Cart> cartArrayList;
    private int c = 0;

    public CartAdapter(Context context, ArrayList<Cart> cartArrayList, RecyclerViewClickInterface recyclerViewClickInterface) {
        this.context = context;
        this.cartArrayList = cartArrayList;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_dish_layout, parent, false);
        return new CartAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.quantity.setText();
        holder.dishName.setText(cartArrayList.get(position).getN());

        holder.price.setText(cartArrayList.get(position).getP());
        holder.totalPrice.setText(cartArrayList.get(position).getP());

        if(cartArrayList.get(position).getV().equalsIgnoreCase("veg")){
            holder.vnv.setImageResource(R.drawable.veg);
        }
        else {
            holder.vnv.setImageResource(R.drawable.nonveg);
        }

    }

    @Override
    public int getItemCount() {
        return cartArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView vnv, dishadd, dishminus;
        private TextView dishName,price,veg, quantity, totalPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            vnv = itemView.findViewById(R.id.vnv);
            dishName = itemView.findViewById(R.id.dishName);
            price = itemView.findViewById(R.id.price);
            veg = itemView.findViewById(R.id.veg);
            quantity = itemView.findViewById(R.id.totalquantity);
            totalPrice = itemView.findViewById(R.id.totalPrice);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewClickInterface.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
