package in.dete.oops;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class IdkAdapter extends RecyclerView.Adapter<IdkAdapter.ViewHolder> {

    private Context context;
    private ArrayList<IDK> cartArrayList;
    private int c = 0;

    public IdkAdapter(Context context, ArrayList<IDK> cartArrayList) {
        this.context = context;
        this.cartArrayList = cartArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.idk_activity_layout, parent, false);
        return new IdkAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.restaurantName.setText(cartArrayList.get(position).getRn());
        holder.totalPrice.setText(cartArrayList.get(position).getTp());
        holder.totalItems.setText(cartArrayList.get(position).getI());
        if(cartArrayList.get(position).getS().equals("In-Progress")){
            holder.status.setVisibility(View.VISIBLE);
            holder.status.setText("Status : In-Progress");
            holder.otp.setVisibility(View.VISIBLE);
            holder.otp.setText(cartArrayList.get(position).getOtp());
        }
        if(cartArrayList.get(position).getS().equals("Order Completed")){
            holder.status.setVisibility(View.VISIBLE);
            holder.status.setText("Order Completed");
        }
    }

    @Override
    public int getItemCount() {
        return cartArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView restaurantName, totalItems, totalPrice, status, otp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            restaurantName = itemView.findViewById(R.id.dishName);
            totalItems = itemView.findViewById(R.id.totalquantity1);
            totalPrice = itemView.findViewById(R.id.price1);
            status = itemView.findViewById(R.id.status);
            otp = itemView.findViewById(R.id.otp);
        }
    }
}
