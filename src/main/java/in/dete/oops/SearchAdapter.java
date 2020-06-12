package in.dete.oops;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> implements Filterable {

    private Context context;
    private ArrayList<Restaurant> restaurants;
    private ArrayList<Restaurant> restaurantArrayList;

    public SearchAdapter(Context context, ArrayList<Restaurant> restaurants) {
        this.context = context;
        this.restaurants = restaurants;
        restaurantArrayList = new ArrayList<>(restaurants);
    }

        @NonNull
        @Override
        public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_row_layout, parent, false);
            return new SearchAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.restaurantName.setText(restaurants.get(position).getN());
            if (restaurants.get(position).getVn().equals("veg")) {
                holder.nonVeg.setVisibility(View.GONE);
            }

            if (restaurants.get(position).getV().equals("non-veg")) {
                holder.veg.setVisibility(View.GONE);
            }
            holder.rating.setText(restaurants.get(position).getR());

            holder.bg.setOnClickListener(v -> {
                Intent intent = new Intent(context, MenuActivity.class);
                intent.putExtra("restaurants", restaurants.get(position).getN());
                intent.putExtra("uid", restaurants.get(position).getUid());
                context.startActivity(intent);
            });
            switch (position) {
                case 0:
                    holder.image.setImageResource(R.drawable.restaurant1);
                    break;
                case 1:
                    holder.image.setImageResource(R.drawable.restaurant2);
                    break;
                case 2:
                    holder.image.setImageResource(R.drawable.restaurant3);
                    break;
                case 3:
                    holder.image.setImageResource(R.drawable.restaurant4);
                    break;
                case 4:
                    holder.image.setImageResource(R.drawable.restaurant5);
                    break;
            }
//        if (restaurants.get(position).getImg() != null && !restaurants.get(position).getImg().isEmpty())
//            GlideApp.with(context)
//                    .load(restaurants.get(position).getImg().get(0))
//                    .centerInside()
//                    .into(holder.image);
//        else holder.image.setImageURI(null);

        }

        @Override
        public int getItemCount() {
            return restaurants.size();
        }

//    @Override
//    public Filter getFilter() {
//        return exampleFilter;
//    }
//
//    private Filter exampleFilter = new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            ArrayList<Restaurant> filteredList = new ArrayList<>();
//
//            if(constraint == null || constraint.length() == 0){
//                filteredList.addAll(restaurantArrayList);
//            }else{
//                String filterPattern = constraint.toString().toLowerCase().trim();
//
//                for(Restaurant item : restaurantArrayList){
//                    if(item.getN().toLowerCase().contains(filterPattern)){
//                        filteredList.add(item);
//                    }
//                }
//            }
//            FilterResults results = new FilterResults();
//            results.values = filteredList;
//
//            return  results;
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            restaurantArrayList.clear();
//            restaurantArrayList.addAll((ArrayList) results.values);
//            notifyDataSetChanged();
//        }
//    };




    public class ViewHolder extends RecyclerView.ViewHolder {

            private ImageView image;
            private CardView bg;
            private TextView restaurantName, rating, veg, nonVeg;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                bg = itemView.findViewById(R.id.bg);
                restaurantName = itemView.findViewById(R.id.restaurantName);
                image = itemView.findViewById(R.id.image);
                rating = itemView.findViewById(R.id.rating);
                veg = itemView.findViewById(R.id.veg);
                nonVeg = itemView.findViewById(R.id.items);
            }
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Restaurant> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(restaurantArrayList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Restaurant item : restaurantArrayList) {
                    if (item.getN().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            restaurantArrayList.clear();
            restaurantArrayList.addAll((ArrayList<Restaurant>) results.values);
            notifyDataSetChanged();
        }
    };
}

