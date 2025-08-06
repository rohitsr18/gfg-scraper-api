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
        // This strategy finds a text label for a stat (e.g., "Coding Score")
        // and then gets the value from the element immediately following it.

        // Find the "Coding Score"
        profile.setCodingScore(
                parseTextFieldByLabel(doc, "Coding Score")
        );

        // Find the "Problems Solved" count
        String problemsSolvedText = parseTextFieldByLabel(doc, "Problems Solved");
        profile.setProblemsSolved(parseIntFromString(problemsSolvedText));

        // Scrape the user's institute, which is available.
       // profile.setInstitute(parseTextField(doc, ".basic_details_data > a"));

        // Find the "Contest Rating"
        profile.setContestRating(
                parseTextFieldByLabel(doc, "Contest Rating")
        );

        // Global Rank is no longer displayed on the main profile page.
        profile.setGlobalRank("N/A");

        return profile;
    }

    /**
     * Finds a text label (e.g., "Problems Solved") and returns the text
     * from the element immediately following it.
     * @param doc The Jsoup document.
     * @param label The text of the label to search for.
     * @return The text of the corresponding value, or "N/A" if not found.
     */
    private String parseTextFieldByLabel(Document doc, String label) {
        // Selector finds a div with the specific label text, then gets its next sibling div.
        Element element = doc.selectFirst("div.score_card_name:contains(" + label + ") + div.score_card_value");
        return (element != null) ? element.text() : "N/A";
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
     * Helper method to parse an integer from a String.
     * This is useful when the element has already been found.
     * @param text The string containing the number.
     * @return The parsed integer, or 0 if parsing fails.
     */
    private int parseIntFromString(String text) {
        if (text != null && !text.equals("N/A")) {
            String digitsOnly = text.replaceAll("\\D+", ""); // Remove non-digit characters
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
