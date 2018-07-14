package com.ice.bunchbead.android.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ice.bunchbead.android.R;
import com.ice.bunchbead.android.data.Rank;
import com.ice.bunchbead.android.data.RankIngredient;
import com.ice.bunchbead.android.helpers.UtilHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rizky Kharisma on 14/07/18.
 */
public class RankAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_TIME = 100;
    private final int TYPE_INGREDIENT_RANK = 101;

    private Context mContext;
    private List<Object> data;
    private List<Rank> realData;
    private OnIngredientClickListener mClickListener;

    public RankAdapter(Context context) {
        this.mContext = context;
        data = new ArrayList<>();
        realData = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_TIME:
                return new TimeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time, parent, false));
            case TYPE_INGREDIENT_RANK:
                return new IngredientRankViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient_rank, parent, false));
            default:
                throw new RuntimeException("View Type can't be handled");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (data.get(position) instanceof String) {
            String time = String.valueOf(data.get(position));
            ((TimeViewHolder) holder).time.setText(time);
        } else {
            RankIngredient ingredient = (RankIngredient) data.get(position);
            ((IngredientRankViewHolder) holder).name.setText(ingredient.getNama());
            if (ingredient.isProcessed()) {
                ((IngredientRankViewHolder) holder).root.setBackgroundColor(UtilHelper.getColor(mContext, R.color.ingredient_no_need_attention));
            } else {
                ((IngredientRankViewHolder) holder).root.setBackgroundColor(UtilHelper.getColor(mContext, R.color.ingredient_normal));
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (data.get(position) instanceof String) return TYPE_TIME;
        else return TYPE_INGREDIENT_RANK;
    }

    public void setClickListener(OnIngredientClickListener clickListener) {
        mClickListener = clickListener;
    }

    public void add(Rank rank) {
        realData.add(0, rank);
        changeRealData();
    }

    public void change(Rank rank) {
        int position = findPosition(rank);
        if (position > -1) {
            realData.set(position, rank);
            changeRealData();
        }
    }

    public void remove(Rank rank) {
        int position = findPosition(rank);
        if (position > -1) {
            realData.remove(position);
            changeRealData();
        }
    }

    private int findPosition(Rank rank) {
        int position = -1;
        for (int i = 0; i < getItemCount(); i++) {
            if (rank.getId().equals(realData.get(i).getId())) {
                position = i;
                break;
            }
        }
        return position;
    }


    public void clear() {
        realData.clear();
        clearShowedData();
    }

    private void clearShowedData() {
        int length = getItemCount();
        data.clear();
        notifyItemRangeRemoved(0, length);
    }

    private void changeRealData() {
        clearShowedData();
        for (Rank rank : realData) {
            data.add(rank.getTimeStamp());
            data.addAll(rank.getIngredients());
        }
        notifyItemRangeInserted(0, getItemCount());
    }

    class TimeViewHolder extends RecyclerView.ViewHolder {
        TextView time;

        TimeViewHolder(View view) {
            super(view);
            time = view.findViewById(R.id.textTime);
        }
    }

    class IngredientRankViewHolder extends RecyclerView.ViewHolder {
        CardView root;
        TextView name;

        IngredientRankViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.textProductName);
            root = view.findViewById(R.id.rootProduct);
            root.setOnClickListener(v -> {
                Object dataView = data.get(getAdapterPosition());
                if (dataView instanceof RankIngredient && mClickListener != null) {
                    RankIngredient rankIngredient = (RankIngredient) dataView;
                    if (!rankIngredient.isProcessed()) {
                        mClickListener.onClick(rankIngredient.getId(), rankIngredient.getRankId(), rankIngredient.getKey());
                    }
                }
            });
        }
    }

    public interface OnIngredientClickListener {
        void onClick(String id, String rankId, String key);
    }
}
