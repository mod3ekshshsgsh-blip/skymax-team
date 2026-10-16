package com.example.data.repositories;

import com.example.R;
import com.example.data.models.CuratedTemplate;
import java.util.ArrayList;
import java.util.List;

public class TemplatesRepository {
    private final List<CuratedTemplate> templates = new ArrayList<>();

    public TemplatesRepository() {
        templates.add(new CuratedTemplate(
                "tmpl_1",
                "YouTube",
                "Sponsor Pitch",
                "High engagement",
                true,
                "Brand Integration: Premium Smart Watch",
                "Hook viewers within 3 seconds, naturally bridge from video topic to sponsor product, highlight 2 major features, include exclusive coupon code.",
                "Professional",
                R.drawable.img_template_yt
        ));

        templates.add(new CuratedTemplate(
                "tmpl_2",
                "Social",
                "Viral Carousel",
                "10-slide layout",
                false,
                "10 Micro-Habits of High-Performing Creators",
                "Slide 1: High-contrast curiosity hook.\nSlides 2-9: One punchy actionable habit per slide with visual cue.\nSlide 10: Call to action to save and share.",
                "Friendly",
                R.drawable.img_template_yt
        ));

        templates.add(new CuratedTemplate(
                "tmpl_3",
                "Marketing",
                "Product Launch",
                "Conversion engine",
                true,
                "Next-Gen Wireless Noise-Cancelling Headphones",
                "Announce preorder window, contrast against legacy bulky models, emphasize 40h battery and spatial acoustics, include countdown timer copy.",
                "Persuasive",
                R.drawable.img_template_yt
        ));

        templates.add(new CuratedTemplate(
                "tmpl_4",
                "Business",
                "Client Proposal",
                "Formal tone",
                false,
                "Creative Content Retainer Proposal Q4",
                "Executive summary of goals, scope of deliverables (12 shorts, 4 longform), timeline milestones, ROI metrics, clear signing deadline.",
                "Professional",
                R.drawable.img_template_yt
        ));

        templates.add(new CuratedTemplate(
                "tmpl_5",
                "Email",
                "Re-engagement",
                "Warm outreach",
                true,
                "We noticed you have been quiet...",
                "Warm personalized check-in, summarize 3 major features shipped since last visit, offer 20% reactivation bonus credit.",
                "Casual",
                R.drawable.img_template_yt
        ));
    }

    public List<CuratedTemplate> getAllTemplates() {
        return new ArrayList<>(templates);
    }

    public List<CuratedTemplate> getTemplatesByCategory(String category) {
        if (category == null || category.equalsIgnoreCase("All")) {
            return getAllTemplates();
        }
        List<CuratedTemplate> filtered = new ArrayList<>();
        for (CuratedTemplate t : templates) {
            if (t.getCategory().equalsIgnoreCase(category)) {
                filtered.add(t);
            }
        }
        return filtered;
    }
}
