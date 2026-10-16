package com.example;

import org.junit.Test;
import static org.junit.Assert.*;

import com.example.data.models.CreationItem;
import com.example.data.models.GenerationRequest;
import com.example.data.models.User;
import java.util.Arrays;

public class AICreatorUnitTest {
    @Test
    public void testUserCreation() {
        User user = new User("u1", "Alex", "alex@example.com", false, 4, 5);
        assertEquals("Alex", user.getName());
        assertEquals("alex@example.com", user.getEmail());
        assertFalse(user.isPro());
        assertEquals(4, user.getCreditsRemaining());
        assertEquals(5, user.getMaxDailyCredits());
    }

    @Test
    public void testCreationItemWordCount() {
        CreationItem item = new CreationItem("item_1", "Test Title", "YouTube Script", "One two three four five", 0, "Today", false, "Test Subtitle");
        assertEquals(5, item.getWordCount());
        assertEquals("Test Title", item.getTitle());
        assertFalse(item.isFavorite());
        item.setFavorite(true);
        assertTrue(item.isFavorite());
    }

    @Test
    public void testGenerationRequest() {
        GenerationRequest request = new GenerationRequest("YouTube Script", "M4 Mac Mini", "Test Brief", "Professional", "English (US)", "Medium (~400w)", Arrays.asList("tech", "apple"));
        assertEquals("YouTube Script", request.getContentType());
        assertEquals("M4 Mac Mini", request.getTopic());
        assertEquals("Professional", request.getTone());
        assertEquals(2, request.getKeywords().size());
    }
}
