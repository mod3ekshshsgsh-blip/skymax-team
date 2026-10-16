package com.example.features.templates;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.R;
import com.example.data.models.CuratedTemplate;
import java.util.List;

public class CuratedTemplatesAdapter extends RecyclerView.Adapter<CuratedTemplatesAdapter.ViewHolder> {
    private final List<CuratedTemplate> items;
    private final OnTemplateClickListener listener;

    public interface OnTemplateClickListener {
        void onTemplateClick(CuratedTemplate template);
    }

    public CuratedTemplatesAdapter(List<CuratedTemplate> items, OnTemplateClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_curated_template, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CuratedTemplate item = items.get(position);
        holder.tvCategory.setText(item.getCategory().toUpperCase());
        holder.tvTitle.setText(item.getTitle());
        holder.tvSubtitle.setText(item.getSubtitle());
        holder.tvProBadge.setVisibility(item.isPro() ? View.VISIBLE : View.GONE);
        holder.ivImg.setImageResource(item.getImageRes());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTemplateClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImg;
        TextView tvProBadge;
        TextView tvCategory;
        TextView tvTitle;
        TextView tvSubtitle;

        ViewHolder(View itemView) {
            super(itemView);
            ivImg = itemView.findViewById(R.id.iv_template_img);
            tvProBadge = itemView.findViewById(R.id.tv_template_pro_badge);
            tvCategory = itemView.findViewById(R.id.tv_template_category);
            tvTitle = itemView.findViewById(R.id.tv_template_title);
            tvSubtitle = itemView.findViewById(R.id.tv_template_subtitle);
        }
    }
}
