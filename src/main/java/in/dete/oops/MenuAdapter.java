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

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    private Context context;
    private RecyclerViewClickInterface recyclerViewClickInterface;
    private ArrayList<MenuModel> menuModels;
    private int c = 0;

//    public interface OnItemClickListener{
//        void onItemClick(int position);
//    }

    public MenuAdapter(Context context, ArrayList<MenuModel> menuModels, RecyclerViewClickInterface recyclerViewClickInterface) {
        this.context = context;
        this.menuModels = menuModels;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dish_row_layout, parent, false);
        return new MenuAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      //  holder.dishquantity.setText("0");
        holder.dishName.setText(menuModels.get(position).getN());

        holder.price.setText(menuModels.get(position).getP());

//        if (menu.get(position).getUrl()!=null && !menu.get(position).getUrl().isEmpty())
//            GlideApp.with(context)
//                    .load(menu.get(position).getUrl())
//                    .centerInside()
//                    .into(holder.image);
//        else holder.image.setImageURI(null);

        if(menuModels.get(position).getV().equalsIgnoreCase("veg")){
            holder.vnv.setImageResource(R.drawable.veg);
        }
        else {
            holder.vnv.setImageResource(R.drawable.nonveg);
        }

        switch (position){
            case 0:
                holder.image.setImageResource(R.drawable.dosa);
                break;
            case 1:
                holder.image.setImageResource(R.drawable.burger);
                break;
            case 2:
                holder.image.setImageResource(R.drawable.pizza);
                break;
            case 3:
                holder.image.setImageResource(R.drawable.chillipaneer);
                break;
            case 4:
                holder.image.setImageResource(R.drawable.chocobrownie);
                break;
        }

//        holder.dishadd.setOnClickListener(v -> {
//            ++c;
//            StringBuffer sr = new StringBuffer(c);
//            holder.dishquantity.setText(sr);
//        });
//
//        holder.dishminus.setOnClickListener(v -> {
//            if(c==0){
//                Toast.makeText(context, "Please add some dish first", Toast.LENGTH_SHORT).show();
//            }else{
//                --c;
//                StringBuffer sr = new StringBuffer(c);
//                holder.dishquantity.setText(sr);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return menuModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView image, vnv;
        private TextView dishName,price,veg, dishquantity;
        private TextView etquantity, totalquantity;
        private String quan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            vnv = itemView.findViewById(R.id.vnv);
            dishName = itemView.findViewById(R.id.dishName);
            image = itemView.findViewById(R.id.image);
            price = itemView.findViewById(R.id.price);
            veg = itemView.findViewById(R.id.veg);
            etquantity = itemView.findViewById(R.id.quantity);
            totalquantity = itemView.findViewById(R.id.totalquantity);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewClickInterface.onItemClick(getAdapterPosition());
//                    etquantity.setVisibility(View.VISIBLE);
//                    quan = etquantity.getText().toString();
                }
            });
        }
    }
}
