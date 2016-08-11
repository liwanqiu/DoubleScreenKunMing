package com.changfeng.tcpdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.changfeng.tcpdemo.bean.Suggestion;
import com.changfeng.tcpdemo.listener.OnItemClickListener;
import com.changfeng.tcpdemo.listener.OnItemLongClickListener;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chang on 2016/8/10.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.SimpleViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private Context context;
    List<Suggestion.SuggestionBean> suggestions;

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public static class SimpleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {


        @BindView(R.id.text_view_weather)
        TextView weatherTextView;
        @BindView(R.id.text_view_suggestion)
        TextView suggestionTextView;

        private OnItemClickListener onItemClickListener;
        private OnItemLongClickListener onItemLongClickListener;
        public SimpleViewHolder(final View itemView, OnItemClickListener onItemClickListener, OnItemLongClickListener onItemLongClickListener) {
            super(itemView);
            Log.i(TAG, "SimpleViewHolder: ");
            ButterKnife.bind(this, itemView);
            this.onItemClickListener = onItemClickListener;
            this.onItemLongClickListener = onItemLongClickListener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.i(TAG, "onClick: ");
            if (onItemClickListener != null) {
                onItemClickListener.onItemClickListener(getLayoutPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            Log.i(TAG, "onLongClick: ");
            if (onItemLongClickListener != null) {
                onItemLongClickListener.onItemLongClickListener(getLayoutPosition());
            }
            return true;
        }
    }

    public RecyclerViewAdapter(Context context, List<Suggestion.SuggestionBean> suggestions, OnItemClickListener onItemClickListener, OnItemLongClickListener onItemLongClickListener) {
        Log.i(TAG, "RecyclerViewAdapter: ");
        this.context = context;
        this.suggestions = suggestions;
        this.onItemClickListener = onItemClickListener;
        this.onItemLongClickListener = onItemLongClickListener;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        return new SimpleViewHolder(view, onItemClickListener, onItemLongClickListener);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder: ");
        final Suggestion.SuggestionBean suggestion = suggestions.get(position);
        holder.weatherTextView.setText(suggestion.getWeather());
        holder.suggestionTextView.setText(suggestion.getSuggestion());

    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "getItemCount: ");
        return suggestions.size();
    }

    public void updateDatas(List<Suggestion.SuggestionBean> suggestions) {
        this.suggestions = suggestions;
        notifyDataSetChanged();
    }


}
