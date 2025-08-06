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
        // The GFG profile URL structure
        String url = "https://auth.geeksforgeeks.org/user/" + username;
        Document doc = JsoupHelper.fetchDocument(url);

        GfgProfile profile = new GfgProfile();
        profile.setUsername(username);

//        // Scrape basic details using more specific CSS selectors
//        profile.setInstitute(parseTextField(doc, ".basic_details_data > a"));
//        // The primary language is not displayed on the profile page anymore.
//        profile.setLanguage("N/A");

        // Scrape stats
        // Note: Selectors are based on the GFG profile page structure as of late 2023/early 2024.
        // These can change and may need updates if GFG redesigns their page.
        profile.setGlobalRank(parseTextField(doc, ".rank_div .score_card_value"));
        profile.setProblemsSolved(parseIntField(doc, ".problems_solved_div .score_card_value"));
        profile.setCodingScore(parseTextField(doc, ".coding_score_div .score_card_value"));
        // Contest rating is not on the main profile page.
        profile.setContestRating("N/A");

        return profile;
    }

    /**
     * Parses a text field from the document using a CSS selector.
     * Returns "N/A" if the element is not found.
     */
    private String parseTextField(Document doc, String cssSelector) {
        Element element = doc.selectFirst(cssSelector);
        return (element != null) ? element.text() : "N/A";
    }

    /**
     * Parses an integer field from the document using a CSS selector.
     * Extracts digits from the text and converts to an int.
     * Returns 0 if not found or on parsing error.
     */
    private int parseIntField(Document doc, String cssSelector) {
        Element element = doc.selectFirst(cssSelector);
        if (element != null) {
            // Remove any non-digit characters (like commas) before parsing
            String text = element.text().replaceAll("\\D+", "");
            if (!text.isEmpty()) {
                try {
                    return Integer.parseInt(text);
                } catch (NumberFormatException e) {
                    // Log this error in a real application
                    return 0;
                }
            }
        }
        return 0;
    }
}