package com.example.features.history;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.AICreatorApp;
import com.example.R;
import com.example.data.models.CreationItem;
import java.util.List;

public class CreationsAdapter extends RecyclerView.Adapter<CreationsAdapter.ViewHolder> {
    private final List<CreationItem> items;
    private final OnCreationActionListener listener;

    public interface OnCreationActionListener {
        void onItemClick(CreationItem item);
        void onFavoriteToggle(CreationItem item);
        void onItemDeleted(CreationItem item);
    }

    public CreationsAdapter(List<CreationItem> items, OnCreationActionListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_creation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CreationItem item = items.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvMeta.setText(item.getContentType() + " • " + item.getWordCount() + " words • " + item.getDateFormatted());
        holder.tvTagBadge.setText(item.getSubtitle() != null && !item.getSubtitle().isEmpty() ? item.getSubtitle() : "Studio Draft");

        // Star state
        if (item.isFavorite()) {
            holder.btnStar.setImageResource(R.drawable.ic_star_filled);
            holder.btnStar.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), R.color.secondary));
        } else {
            holder.btnStar.setImageResource(R.drawable.ic_star_outline);
            holder.btnStar.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), R.color.outline));
        }

        // Icon per content type
        int iconRes = R.drawable.ic_smart_display;
        int colorRes = R.color.primary;
        if (item.getContentType().contains("Social")) {
            iconRes = R.drawable.ic_photo_camera;
            colorRes = R.color.primary;
        } else if (item.getContentType().contains("Email")) {
            iconRes = R.drawable.ic_mail;
            colorRes = R.color.tertiary;
        } else if (item.getContentType().contains("Blog")) {
            iconRes = R.drawable.ic_article;
            colorRes = R.color.text_white;
        } else if (item.getContentType().contains("Ad") || item.getContentType().contains("Marketing")) {
            iconRes = R.drawable.ic_campaign;
            colorRes = R.color.secondary;
        }
        holder.ivIcon.setImageResource(iconRes);
        holder.ivIcon.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), colorRes));

        // Click on item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(item);
        });

        // Toggle favorite
        holder.btnStar.setOnClickListener(v -> {
            AICreatorApp.getInstance().getCreationsRepository().toggleFavorite(item.getId());
            item.setFavorite(!item.isFavorite());
            notifyItemChanged(position);
            String msg = item.isFavorite() ? "Saved to favorites!" : "Removed from favorites";
            Toast.makeText(holder.itemView.getContext(), msg, Toast.LENGTH_SHORT).show();
            if (listener != null) listener.onFavoriteToggle(item);
        });

        // Copy button
        holder.btnCopy.setOnClickListener(v -> {
            ClipboardManager cm = (ClipboardManager) holder.itemView.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            if (cm != null) {
                cm.setPrimaryClip(ClipData.newPlainText("Creation Content", item.getContent()));
                Toast.makeText(holder.itemView.getContext(), "Copied to clipboard!", Toast.LENGTH_SHORT).show();
            }
        });

        // View button
        holder.btnView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(item);
        });

        // Share button
        holder.btnShare.setOnClickListener(v -> {
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, item.getTitle() + "\n\n" + item.getContent());
            sendIntent.setType("text/plain");
            holder.itemView.getContext().startActivity(Intent.createChooser(sendIntent, "Share Creation"));
        });

        // 3-dots popup menu
        holder.btnMore.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(holder.itemView.getContext(), holder.btnMore);
            popup.getMenu().add(item.isFavorite() ? "Remove from Favorites" : "Add to Favorites");
            popup.getMenu().add("Delete Creation");
            popup.setOnMenuItemClickListener(menuItem -> {
                if (menuItem.getTitle().toString().contains("Favorites")) {
                    holder.btnStar.performClick();
                    return true;
                } else if (menuItem.getTitle().toString().contains("Delete")) {
                    AICreatorApp.getInstance().getCreationsRepository().deleteCreation(item.getId());
                    items.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(holder.itemView.getContext(), "Creation deleted", Toast.LENGTH_SHORT).show();
                    if (listener != null) listener.onItemDeleted(item);
                    return true;
                }
                return false;
            });
            popup.show();
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvTitle;
        TextView tvMeta;
        ImageView btnStar;
        ImageView btnMore;
        TextView tvTagBadge;
        Button btnCopy;
        Button btnView;
        Button btnShare;

        ViewHolder(View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_history_icon);
            tvTitle = itemView.findViewById(R.id.tv_history_title);
            tvMeta = itemView.findViewById(R.id.tv_history_meta);
            btnStar = itemView.findViewById(R.id.btn_history_star);
            btnMore = itemView.findViewById(R.id.btn_history_more);
            tvTagBadge = itemView.findViewById(R.id.tv_history_tag_badge);
            btnCopy = itemView.findViewById(R.id.btn_card_copy);
            btnView = itemView.findViewById(R.id.btn_card_view);
            btnShare = itemView.findViewById(R.id.btn_card_share);
        }
    }
}
