package com.rohitsr18.gfgscraperapi.service;

import com.rohitsr18.gfgscraperapi.model.GfgProfile;
import com.rohitsr18.gfgscraperapi.utils.JsoupHelper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GfgScraperService {

    public GfgProfile scrapeProfile(String username) throws IOException {
        // The profile URL remains the same.
        String url = "https://www.geeksforgeeks.org/user/" + username + "/";
        Document doc = JsoupHelper.fetchDocument(url);

        GfgProfile profile = new GfgProfile();
        profile.setUsername(username);

        // --- UPDATED SELECTOR STRATEGY ---
        // This new strategy is more robust. It finds the text label for a stat
        // (e.g., "Coding Score") and then gets the value from the element associated with it.

        // Scrape the user's institute
        profile.setInstitute(parseTextField(doc, ".basic_details_data > a[href*='institute']"));

        // Find the "Coding Score" by its label
        profile.setCodingScore(findStatByLabel(doc, "Coding Score"));

        // Find the "Problem Solved" count by its label
        String problemsSolvedText = findStatByLabel(doc, "Problem Solved");
        profile.setProblemsSolved(parseIntFromString(problemsSolvedText));

        // Find the "Contest Rating" by its label
        profile.setContestRating(findStatByLabel(doc, "Contest Rating"));

        // Find the Rank by its specific class
        profile.setGlobalRank(parseTextField(doc, ".rankNum"));

        return profile;
    }

    /**
     * A general-purpose parser for a text field from the document using a CSS selector.
     * Returns "N/A" if the element is not found.
     */
    private String parseTextField(Document doc, String cssSelector) {
        Element element = doc.selectFirst(cssSelector);
        return (element != null) ? element.text() : "N/A";
    }

    /**
     * Finds a stat card by its text label (e.g., "Problem Solved") and returns the
     * corresponding value. This is more resilient to page layout changes.
     * @param doc The Jsoup document.
     * @param label The text of the label to search for.
     * @return The text of the corresponding value, or "N/A" if not found.
     */
    private String findStatByLabel(Document doc, String label) {
        // This selector finds a card that has a div containing the label text,
        // and then finds the count value within that same card.
        String selector = String.format("div.stats_card:has(div.stats_card_left--name:contains(%s)) div.stats_card_right--count", label);
        Element element = doc.selectFirst(selector);
        return (element != null) ? element.text() : "N/A";
    }

    /**
     * Helper method to parse an integer from a String.
     * @param text The string containing the number.
     * @return The parsed integer, or 0 if parsing fails.
     */
    private int parseIntFromString(String text) {
        if (text != null && !text.equals("N/A")) {
            // Remove any non-digit characters (like commas or dashes) before parsing
            String digitsOnly = text.replaceAll("[^0-9]", "");
            if (!digitsOnly.isEmpty()) {
                try {
                    return Integer.parseInt(digitsOnly);
                } catch (NumberFormatException e) {
                    // In a real application, you would log this error.
                    return 0;
                }
            }
        }
        return 0;
    }
}
