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
        // 1. Correct URL: The profile page is on the "www" subdomain.
        String url = "https://www.geeksforgeeks.org/user/" + username + "/";
        Document doc = JsoupHelper.fetchDocument(url);

        GfgProfile profile = new GfgProfile();
        profile.setUsername(username);

        // 2. Correct Selectors: The old class names are gone. We now target elements
        // by finding the link to their specific pages (e.g., the link to "coding-scores").

        // Find the main container for all the stats cards.
        Element scoreCardContainer = doc.selectFirst(".contributor_card_right_upper_div");

        if (scoreCardContainer != null) {
            // Inside the container, find the coding score by its unique link.
            Element codingScoreElement = scoreCardContainer.selectFirst("a[href*='coding-scores'] .stats_card_right--count");
            profile.setCodingScore(codingScoreElement != null ? codingScoreElement.text() : "N/A");

            // Find the problems solved count by its unique link.
            Element problemsSolvedElement = scoreCardContainer.selectFirst("a[href*='problems'] .stats_card_right--count");
            profile.setProblemsSolved(parseIntFromElement(problemsSolvedElement));
        } else {
            // If the main stats container isn't found, default all values.
            profile.setCodingScore("N/A");
            profile.setProblemsSolved(0);
        }

        // These stats are no longer on the main profile page.
        profile.setGlobalRank("N/A");
        profile.setContestRating("N/A");

        return profile;
    }

    /**
     * Helper method to parse an integer from a Jsoup Element.
     * This avoids code duplication and makes the main method cleaner.
     * Returns 0 if the element is null or if parsing fails.
     */
    private int parseIntFromElement(Element element) {
        if (element != null) {
            String text = element.text().replaceAll("\\D+", ""); // Remove non-digit characters
            if (!text.isEmpty()) {
                try {
                    return Integer.parseInt(text);
                } catch (NumberFormatException e) {
                    // In a real application, you would log this error.
                    return 0;
                }
            }
        }
        return 0;
    }
}