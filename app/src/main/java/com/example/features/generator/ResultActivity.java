package com.example.features.generator;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.AICreatorApp;
import com.example.R;
import com.example.data.models.CreationItem;

public class ResultActivity extends AppCompatActivity {
    public static final String EXTRA_CREATION_ITEM = "extra_creation_item";

    private CreationItem creationItem;
    private TextView tvHeaderType;
    private TextView tvHeaderTitle;
    private TextView tvWords;
    private EditText etBody;
    private ImageView btnFavorite;
    private ImageView btnShareTop;
    private Button btnCopy;
    private Button btnShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        creationItem = (CreationItem) getIntent().getSerializableExtra(EXTRA_CREATION_ITEM);
        if (creationItem == null) {
            finish();
            return;
        }

        initViews();
        bindData();
        setupListeners();
    }

    private void initViews() {
        tvHeaderType = findViewById(R.id.tv_result_header_type);
        tvHeaderTitle = findViewById(R.id.tv_result_header_title);
        tvWords = findViewById(R.id.tv_result_metrics_words);
        etBody = findViewById(R.id.et_result_body);
        btnFavorite = findViewById(R.id.btn_result_favorite);
        btnShareTop = findViewById(R.id.btn_result_share_top);
        btnCopy = findViewById(R.id.btn_result_copy);
        btnShare = findViewById(R.id.btn_result_share);

        findViewById(R.id.btn_result_back).setOnClickListener(v -> finish());
    }

    private void bindData() {
        tvHeaderType.setText(creationItem.getContentType().toUpperCase());
        tvHeaderTitle.setText(creationItem.getTitle());
        tvWords.setText(creationItem.getWordCount() + " words");
        etBody.setText(creationItem.getContent());
        updateFavoriteIcon();
    }

    private void updateFavoriteIcon() {
        btnFavorite.setImageResource(creationItem.isFavorite() ? R.drawable.ic_star_filled : R.drawable.ic_star_outline);
    }

    private void setupListeners() {
        btnFavorite.setOnClickListener(v -> {
            creationItem.setFavorite(!creationItem.isFavorite());
            AICreatorApp.getInstance().getCreationsRepository().toggleFavorite(creationItem.getId());
            updateFavoriteIcon();
            String msg = creationItem.isFavorite() ? "Saved to favorites!" : "Removed from favorites";
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        });

        btnCopy.setOnClickListener(v -> copyContent());

        btnShare.setOnClickListener(v -> shareContent());
        btnShareTop.setOnClickListener(v -> shareContent());
    }

    private void copyContent() {
        String content = etBody.getText().toString();
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null) {
            cm.setPrimaryClip(ClipData.newPlainText("AI Content", content));
            Toast.makeText(this, "Copied content to clipboard!", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareContent() {
        String content = etBody.getText().toString();
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, creationItem.getTitle() + "\n\n" + content);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share Draft"));
    }
}
