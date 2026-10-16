package com.example.features.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.R;
import com.example.data.models.AiToolItem;
import java.util.List;

public class AiToolsAdapter extends RecyclerView.Adapter<AiToolsAdapter.ViewHolder> {
    private final List<AiToolItem> items;
    private final OnToolClickListener listener;

    public interface OnToolClickListener {
        void onToolClick(AiToolItem item);
    }

    public AiToolsAdapter(List<AiToolItem> items, OnToolClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ai_tool, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AiToolItem item = items.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvDesc.setText(item.getDescription());
        holder.tvAction.setText(item.getActionText());
        holder.ivIcon.setImageResource(item.getIconRes());
        holder.ivIcon.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), item.getAccentColorRes()));
        holder.tvAction.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), item.getAccentColorRes()));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onToolClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvTitle;
        TextView tvDesc;
        TextView tvAction;

        ViewHolder(View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_tool_icon);
            tvTitle = itemView.findViewById(R.id.tv_tool_title);
            tvDesc = itemView.findViewById(R.id.tv_tool_desc);
            tvAction = itemView.findViewById(R.id.tv_tool_action);
        }
    }
}
