package com.ice.bunchbead.android.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ice.bunchbead.android.R;
import com.ice.bunchbead.android.data.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {

    private List<Ingredient> data;
    private List<Ingredient> searchData;
    private Context mContext;

    public IngredientsAdapter(Context context) {
        mContext = context;
        data = new ArrayList<>();
        searchData = null;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient ingredient;
        if (searchData == null) {
            ingredient = data.get(position);
        } else {
            ingredient = searchData.get(position);
        }
        if (Double.parseDouble(ingredient.getSisa()) <= Double.parseDouble(ingredient.getMin())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.root.setCardBackgroundColor(mContext.getColor(R.color.ingredient_need_attention));
            } else {
                holder.root.setCardBackgroundColor(mContext.getResources().getColor(R.color.ingredient_need_attention));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.root.setCardBackgroundColor(mContext.getColor(R.color.ingredient_normal));
            } else {
                holder.root.setCardBackgroundColor(mContext.getResources().getColor(R.color.ingredient_normal));
            }
        }
        holder.productName.setText(ingredient.getNama());
        holder.minStock.setText(mContext.getString(R.string.ingredients_min_count, ingredient.getMin(), ingredient.getSatuan()));
        holder.nowStock.setText(mContext.getString(R.string.ingredients_now_count, ingredient.getSisa(), ingredient.getSatuan()));
    }

    public void add(Ingredient ingredient) {
        data.add(ingredient);
        if (searchData == null) notifyItemInserted(getItemCount() - 1);
    }

    public void change(Ingredient ingredient) {
        int position = -1;
        for (int i = 0; i < getItemCount(); i++) {
            if (ingredient.getId().equals(data.get(i).getId())) {
                position = i;
                break;
            }
        }
        if (position > -1) {
            data.set(position, ingredient);
            if (searchData == null) notifyItemChanged(position);
        }
    }

    public void remove(Ingredient ingredient) {
        int position = -1;
        for (int i = 0; i < getItemCount(); i++) {
            if (ingredient.getId().equals(data.get(i).getId())) {
                position = i;
                break;
            }
        }
        if (position > -1) {
            data.remove(position);
            if (searchData == null) notifyItemRemoved(position);
        }
    }

    public void clear() {
        int length = getItemCount();
        data.clear();
        notifyItemRangeRemoved(0, length);
    }

    public void search(String textSearch) {
        searchData = new ArrayList<>();
        for (Ingredient ingredient: data) {
            if (ingredient.getNama().contains(textSearch)) {
                searchData.add(ingredient);
            }
        }
        notifyDataSetChanged();
    }

    public void clearSearch() {
        searchData = null;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (searchData == null) {
            return data.size();
        } else {
            return searchData.size();
        }
    }

    public int getItemCountOriginal() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView root;
        TextView productName;
        TextView minStock;
        TextView nowStock;

        ViewHolder(View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.rootProduct);
            productName = itemView.findViewById(R.id.textProductName);
            minStock = itemView.findViewById(R.id.textProductMinStock);
            nowStock = itemView.findViewById(R.id.textProductNowStock);
        }
    }
}
