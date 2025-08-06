package com.rohitsr18.gfgscraperapi.controller;

import com.rohitsr18.gfgscraperapi.model.GfgProfile;
import com.rohitsr18.gfgscraperapi.service.GfgScraperService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/gfg")
public class GfgController {

    private final GfgScraperService gfgScraperService;

    public GfgController(GfgScraperService gfgScraperService) {
        this.gfgScraperService = gfgScraperService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getProfile(@PathVariable String username) {
        try {
            GfgProfile profile = gfgScraperService.scrapeProfile(username);
            return ResponseEntity.ok(profile);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to scrape the profile for user: " + username);
        }
    }

    @GetMapping("/test")
    public String test() {
        return "GFG API is working!";
    }
}