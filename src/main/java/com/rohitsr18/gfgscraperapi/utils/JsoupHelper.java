package com.rohitsr18.gfgscraperapi.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class JsoupHelper {

    /**
     * Fetches and parses an HTML document from a URL.
     * @param url The URL to fetch.
     * @return The parsed Jsoup Document.
     * @throws IOException if an I/O error occurs.
     */
    public static Document fetchDocument(String url) throws IOException {
        // Add a User-Agent header to mimic a real browser visit.
        // This is crucial for avoiding being blocked by websites like GeeksforGeeks.
        return Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
                .get();
    }
}