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

        // Find the "Coding Score"
        profile.setCodingScore(
                parseTextField(doc, "div.stats_card:nth-of-type(1) .stats_card_right--count")
        );

        // Find the "Problems Solved" count
        String problemsSolvedText = parseTextField(doc, "div.stats_card:nth-of-type(2) .stats_card_right--count");
        profile.setProblemsSolved(parseIntFromString(problemsSolvedText));

        // Find the "Contest Rating"
        profile.setContestRating(
                parseTextField(doc, "div.stats_card:nth-of-type(3) .stats_card_right--count")
        );

        // Find the Rank
        String rankText = parseTextField(doc, ".rankNum");
        profile.setGlobalRank(rankText);


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
