package com.exmample.android.inventoryapp;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.RecyclerViewHolder> {

    private List<ProductItem> items;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public ProductAdapter(List<ProductItem> items, Context context) {
        this.items = items;
        this.context = context;
        this.onItemClickListener = (OnItemClickListener) context;
    }

    public void setItems(List<ProductItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        ProductItem item = items.get(position);
        holder.productTitle.setText(item.getName());
        holder.productPrice.setText(String.valueOf(item.getPrice()) + context.getString(R.string.currency));
        holder.productQuantity.setText(String.valueOf(item.getQuantity()) + " " + context.getString(R.string.items_left));
        Picasso.with(context).load(item.getIcon()).placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher).into(holder.productImage);
    }

    private ProductItem getItem(int position) {
        return items.get(position);
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView productImage;
        TextView productTitle;
        TextView productPrice;
        TextView productQuantity;
        LinearLayout linearLayout;
        ImageButton saleButton;

        public RecyclerViewHolder(View view) {
            super(view);
            linearLayout = view.findViewById(R.id.list_item_linear_layout);
            linearLayout.setOnClickListener(this);
            productImage = view.findViewById(R.id.product_image);
            productTitle = view.findViewById(R.id.product_name);
            productPrice = view.findViewById(R.id.product_price);
            productQuantity = view.findViewById(R.id.product_quantity);
            saleButton = view.findViewById(R.id.sale_button);
            saleButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.list_item_linear_layout:
                    onItemClickListener.onItemClick(getItem(getAdapterPosition()));
                    break;
                case R.id.sale_button:
                    onItemClickListener.onSaleButtonClicked(getItem(getAdapterPosition()));
                    break;
            }
        }
    }
}
