@RestController
@RequestMapping("/api/gfg")
public class GfgController {

    private final GfgScraperService gfgScraperService;

    // Using Constructor Injection
    public GfgController(GfgScraperService gfgScraperService) {
        this.gfgScraperService = gfgScraperService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getProfile(@PathVariable String username) { // Use wildcard for multiple return types
        try {
            GfgProfile profile = gfgScraperService.scrapeProfile(username);
            return ResponseEntity.ok(profile);
        } catch (IOException e) {
            // Log the error for debugging
            // e.printStackTrace();

            // Return a more appropriate status code and a helpful message
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to scrape the profile for user: " + username);
        }
    }
}