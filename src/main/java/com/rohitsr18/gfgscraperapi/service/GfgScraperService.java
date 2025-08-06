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
        // 1. UPDATED URL: Use the current public profile URL structure.
        String url = "https://www.geeksforgeeks.org/user/" + username + "/";
        Document doc = JsoupHelper.fetchDocument(url);

        GfgProfile profile = new GfgProfile();
        profile.setUsername(username);

        // 2. UPDATED SELECTORS: Target elements based on their unique links or stable parent classes.

        // The overall score card is now a single component. Let's select it once.
        Element scoreCard = doc.selectFirst(".contributor_card_right_upper_div");

        if (scoreCard != null) {
            // Find the coding score within the score card.
            Element codingScoreElement = scoreCard.selectFirst("a[href*='coding-scores'] .stats_card_right--count");
            profile.setCodingScore(codingScoreElement != null ? codingScoreElement.text() : "N/A");

            // Find the problems solved count within the score card.
            Element problemsSolvedElement = scoreCard.selectFirst("a[href*='problems'] .stats_card_right--count");
            profile.setProblemsSolved(parseIntFromElement(problemsSolvedElement));
        } else {
            // If the main score card isn't found, default all to N/A or 0.
            profile.setCodingScore("N/A");
            profile.setProblemsSolved(0);
        }

        // Global Rank and Contest Rating are not displayed on the main profile page anymore.
        profile.setGlobalRank("N/A");
        profile.setContestRating("N/A");

        return profile;
    }

    /**
     * Parses an integer from a Jsoup Element.
     * Extracts digits from the text and converts to an int.
     * Returns 0 if element is null or on parsing error.
     */
    private int parseIntFromElement(Element element) {
        if (element != null) {
            // Remove any non-digit characters (like commas) before parsing
            String text = element.text().replaceAll("\\D+", "");
            if (!text.isEmpty()) {
                try {
                    return Integer.parseInt(text);
                } catch (NumberFormatException e) {
                    // In a real application, you might want to log this error
                    return 0;
                }
            }
        }
        return 0;
    }
}