package com.rohitsr18.gfgscraperapi.service;

import com.rohitsr18.gfgscraperapi.model.GfgProfile;
import com.rohitsr18.gfgscraperapi.utils.JsoupHelper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GfgScraperService {

    public GfgProfile scrapeProfile(String username) throws IOException {
        String url = "https://www.geeksforgeeks.org/user/" + username + "/";
        Document doc = JsoupHelper.fetchDocument(url);

        GfgProfile profile = new GfgProfile();
        profile.setUsername(username);
        
        // Scrape the user's institute and rank first, as they have unique selectors.
        profile.setInstitute(parseTextField(doc, "div.basic_details_data > a[href*='institute']"));
        profile.setGlobalRank(parseTextField(doc, ".rankNum"));

        // Default values in case stats are not found
        profile.setCodingScore("N/A");
        profile.setProblemsSolved(0);
        profile.setContestRating("N/A");

        // Select all stat cards present on the page
        Elements statCards = doc.select("div.stats_card");
        for (Element card : statCards) {
            Element nameElement = card.selectFirst(".stats_card_left--name");
            Element valueElement = card.selectFirst(".stats_card_right--count");

            if (nameElement != null && valueElement != null) {
                String label = nameElement.text();
                String value = valueElement.text();

                // Assign values based on the label text
                switch (label) {
                    case "Coding Score":
                        profile.setCodingScore(value);
                        break;
                    case "Problem Solved":
                        profile.setProblemsSolved(parseIntFromString(value));
                        break;
                    case "Contest Rating":
                        profile.setContestRating(value);
                        break;
                }
            }
        }

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