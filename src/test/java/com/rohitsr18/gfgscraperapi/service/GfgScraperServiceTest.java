package com.rohitsr18.gfgscraperapi.service;

import com.rohitsr18.gfgscraperapi.model.GfgProfile;
import com.rohitsr18.gfgscraperapi.utils.JsoupHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class GfgScraperServiceTest {

    @InjectMocks
    private GfgScraperService gfgScraperService;

    @Test
    @DisplayName("Should return correct profile when all data is present on the page")
    void scrapeProfile_shouldReturnCorrectProfile_whenDataIsPresent() throws IOException {
        // Arrange: Create a mock HTML document
        String mockHtml = "<html><body>" +
                "<div class='basic_details_data'><a href='https://www.geeksforgeeks.org/institute/test-institute/'>Test Institute</a></div>" +
                "<div class='rankNum'>123</div>" +
                "<div class='stats_card'><div class='stats_card_left--name'>Coding Score</div><div class='stats_card_right--count'>1500</div></div>" +
                "<div class='stats_card'><div class='stats_card_left--name'>Problem Solved</div><div class='stats_card_right--count'>450</div></div>" +
                "<div class='stats_card'><div class='stats_card_left--name'>Contest Rating</div><div class='stats_card_right--count'>2100</div></div>" +
                "</body></html>";
        Document doc = Jsoup.parse(mockHtml);

        // Mock the static JsoupHelper.fetchDocument method
        try (MockedStatic<JsoupHelper> mockedJsoup = Mockito.mockStatic(JsoupHelper.class)) {
            mockedJsoup.when(() -> JsoupHelper.fetchDocument(anyString())).thenReturn(doc);

            // Act
            GfgProfile profile = gfgScraperService.scrapeProfile("testuser");

            // Assert
            assertNotNull(profile);
            assertEquals("testuser", profile.getUsername());
            assertEquals("Test Institute", profile.getInstitute());
            assertEquals("123", profile.getGlobalRank());
            assertEquals("1500", profile.getCodingScore());
            assertEquals(450, profile.getProblemsSolved());
            assertEquals("2100", profile.getContestRating());
        }
    }

    @Test
    @DisplayName("Should return profile with default values when data is missing")
    void scrapeProfile_shouldReturnDefaultProfile_whenDataIsMissing() throws IOException {
        // Arrange: Create an empty HTML document
        String mockHtml = "<html><body></body></html>";
        Document doc = Jsoup.parse(mockHtml);

        try (MockedStatic<JsoupHelper> mockedJsoup = Mockito.mockStatic(JsoupHelper.class)) {
            mockedJsoup.when(() -> JsoupHelper.fetchDocument(anyString())).thenReturn(doc);

            // Act
            GfgProfile profile = gfgScraperService.scrapeProfile("nouser");

            // Assert
            assertNotNull(profile);
            assertEquals("nouser", profile.getUsername());
            assertEquals("N/A", profile.getInstitute());
            assertEquals("N/A", profile.getGlobalRank());
            assertEquals("N/A", profile.getCodingScore());
            assertEquals(0, profile.getProblemsSolved());
            assertEquals("N/A", profile.getContestRating());
        }
    }

    @Test
    @DisplayName("Should correctly parse integer from string with commas")
    void scrapeProfile_shouldHandleCommasInNumbers() throws IOException {
        // Arrange
        String mockHtml = "<html><body>" +
                "<div class='stats_card'><div class='stats_card_left--name'>Problem Solved</div><div class='stats_card_right--count'>1,234</div></div>" +
                "</body></html>";
        Document doc = Jsoup.parse(mockHtml);

        try (MockedStatic<JsoupHelper> mockedJsoup = Mockito.mockStatic(JsoupHelper.class)) {
            mockedJsoup.when(() -> JsoupHelper.fetchDocument(anyString())).thenReturn(doc);

            // Act
            GfgProfile profile = gfgScraperService.scrapeProfile("testuser");

            // Assert
            assertEquals(1234, profile.getProblemsSolved());
        }
    }

    @Test
    @DisplayName("Should throw IOException when the document fetcher fails")
    void scrapeProfile_shouldThrowIOException_whenFetcherFails() {
        // Arrange: Mock the static helper to throw an exception
        try (MockedStatic<JsoupHelper> mockedJsoup = Mockito.mockStatic(JsoupHelper.class)) {
            mockedJsoup.when(() -> JsoupHelper.fetchDocument(anyString())).thenThrow(new IOException("Network error"));

            // Act & Assert
            assertThrows(IOException.class, () -> {
                gfgScraperService.scrapeProfile("anyuser");
            }, "Expected scrapeProfile to throw IOException, but it didn't");
        }
    }
}