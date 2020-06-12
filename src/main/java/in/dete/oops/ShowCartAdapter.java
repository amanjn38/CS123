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

public class ShowCartAdapter extends RecyclerView.Adapter<ShowCartAdapter.ViewHolder>  {

private Context context;
private RecyclerViewClickInterface recyclerViewClickInterface;
private ArrayList<ShowCart> cartArrayList;
private int c = 0;

public ShowCartAdapter(Context context, ArrayList<ShowCart> cartArrayList, RecyclerViewClickInterface recyclerViewClickInterface) {
        this.context = context;
        this.cartArrayList = cartArrayList;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
        }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_cart_activity_layout, parent, false);
        return new ShowCartAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       holder.restaurantName.setText(cartArrayList.get(position).getRn());
       holder.totalPrice.setText(cartArrayList.get(position).getTp());
        holder.totalItems.setText(cartArrayList.get(position).getI());
    }



    @Override
    public int getItemCount() {
        return cartArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView restaurantName, totalItems, totalPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            restaurantName = itemView.findViewById(R.id.restaurantName);
            totalItems = itemView.findViewById(R.id.items);
            totalPrice = itemView.findViewById(R.id.price);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewClickInterface.onItemClick(getAdapterPosition());
                }
            });
        }
    }

}
