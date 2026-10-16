package com.example.data.services;

import android.os.Handler;
import android.os.Looper;
import com.example.BuildConfig;
import com.example.data.models.CreationItem;
import com.example.data.models.GenerationRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AIGeneratorService {
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public interface GenerationCallback {
        void onSuccess(CreationItem result);
        void onError(String message);
    }

    public void generateContent(GenerationRequest request, GenerationCallback callback) {
        if (request.getTopic() == null || request.getTopic().trim().isEmpty()) {
            callback.onError("Please provide a topic or working title.");
            return;
        }

        // Simulate network/AI synthesis delay with realistic progress
        new Thread(() -> {
            try {
                Thread.sleep(1400); // Realistic GPU synthesis delay
            } catch (InterruptedException ignored) {
            }

            String content = synthesizeContent(request);
            int wordCount = content.split("\\s+").length;
            String today = new SimpleDateFormat("MMM d", Locale.US).format(new Date());

            CreationItem item = new CreationItem(
                    "gen_" + System.currentTimeMillis(),
                    request.getTopic().trim(),
                    request.getContentType(),
                    content,
                    wordCount,
                    today,
                    false,
                    request.getTone() + " • Just now"
            );

            mainHandler.post(() -> callback.onSuccess(item));
        }).start();
    }

    private String synthesizeContent(GenerationRequest request) {
        String topic = request.getTopic().trim();
        String brief = request.getBrief() != null && !request.getBrief().trim().isEmpty() 
                ? request.getBrief().trim() 
                : "Comprehensive breakdown tailored for high audience retention.";
        String tone = request.getTone();
        String type = request.getContentType();

        StringBuilder sb = new StringBuilder();

        if ("YouTube Script".equalsIgnoreCase(type) || "YT Script".equalsIgnoreCase(type)) {
            sb.append("TITLE: ").append(topic).append("\n\n");
            sb.append("--- HOOK (0:00 - 0:15) ---\n");
            sb.append("[Visual: Dynamic fast-paced montage with sound design]\n");
            sb.append("Host: \"What if everything you knew about ").append(topic).append(" was completely overturned? By the end of this video, you will have the exact blueprint to master this step-by-step.\"\n\n");
            sb.append("--- INTRO & PREMISE (0:15 - 1:00) ---\n");
            sb.append("Host: \"Welcome back to the studio. If you are new here, hit subscribe for high-impact weekly breakdowns. Today, we are deep diving into ").append(brief).append(".\"\n\n");
            sb.append("--- KEY SECTION 1: THE CORE MECHANISM (1:00 - 3:30) ---\n");
            sb.append("[On Screen: Clean diagrammatic graphics / B-roll]\n");
            sb.append("Host: \"First, let us examine the fundamental layer. Most creators overlook this simple detail, yet it determines 80% of the outcome.\"\n");
            sb.append("Key insight: Focus intensely on execution quality over vanity metrics.\n\n");
            sb.append("--- KEY SECTION 2: PRACTICAL WORKFLOW (3:30 - 6:00) ---\n");
            sb.append("Host: \"Step two is where the magic happens. Notice the precision in ").append(tone.toLowerCase()).append(" pacing.\"\n\n");
            sb.append("--- CALL TO ACTION & OUTRO (6:00 - 7:00) ---\n");
            sb.append("Host: \"Drop your thoughts in the comments below. Grab the free resources linked in the description, and I will see you in the next one!\"");
        } else if ("Social Caption".equalsIgnoreCase(type) || "Social".equalsIgnoreCase(type)) {
            sb.append("🔥 ").append(topic).append("\n\n");
            sb.append("Here is the unfiltered truth about ").append(topic.toLowerCase()).append(":\n\n");
            sb.append("• ").append(brief).append("\n");
            sb.append("• Executed with a ").append(tone.toLowerCase()).append(" perspective.\n");
            sb.append("• Built for sustainable consistency, not fleeting trends.\n\n");
            sb.append("Save this post for your next project review 📌\n");
            sb.append("Tag someone who needs to see this today.\n\n");
            sb.append("#creators #productivity #digitalstrategy #growth #studio #aistudio #contentcreation");
        } else if ("Blog Post".equalsIgnoreCase(type) || "Blog".equalsIgnoreCase(type)) {
            sb.append("# ").append(topic).append("\n\n");
            sb.append("## Executive Summary\n");
            sb.append(brief).append(". In today's digital landscape, having an authoritative approach gives you a distinctive competitive edge.\n\n");
            sb.append("## Why This Matters Now\n");
            sb.append("Audiences are fatigued by superficial summaries. When delivering content in a ").append(tone.toLowerCase()).append(" cadence, clarity always trumps complexity.\n\n");
            sb.append("### Three Pillars of Success\n");
            sb.append("1. **Intentional Architecture**: Establish your foundational principles early.\n");
            sb.append("2. **Iterative Refinement**: Polish drafts through rigorous creative feedback.\n");
            sb.append("3. **Audience Alignment**: Solve immediate, tangible problems.\n\n");
            sb.append("## Final Verdict\n");
            sb.append("Mastering ").append(topic).append(" is a marathon, not a sprint. Implement these strategies today to build compounding value.");
        } else if ("Email".equalsIgnoreCase(type)) {
            sb.append("Subject: Important update regarding ").append(topic).append(" 📬\n\n");
            sb.append("Hey {{First Name}},\n\n");
            sb.append("I wanted to reach out directly with some exciting developments around ").append(topic).append(".\n\n");
            sb.append(brief).append("\n\n");
            sb.append("Here is what you need to know today:\n");
            sb.append("→ We have streamlined the entire process.\n");
            sb.append("→ You get priority access starting immediately.\n\n");
            sb.append("Click here to review the details and get started: [Access Your Portal]\n\n");
            sb.append("Best regards,\n");
            sb.append("Alex | AI Creator Studio");
        } else {
            // Ad Copy / Marketing
            sb.append("Headline: ").append(topic).append(" - The Breakthrough You Have Been Waiting For\n\n");
            sb.append("Primary Text (Angle 1 - Direct):\n");
            sb.append(brief).append(" Engineered for results. Zero fluff. 100% precision.\n\n");
            sb.append("Primary Text (Angle 2 - Pain Point):\n");
            sb.append("Tired of spinning your wheels on ").append(topic).append("? Discover the new standard in creator efficiency.\n\n");
            sb.append("Call To Action: Claim Your Free Trial Today →\n");
            sb.append("Display URL: aicreator.studio/launch");
        }

        return sb.toString();
    }
}
