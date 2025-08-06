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
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to scrape the profile for user: " + username);
        }
    }

    // ✅ Add this method below your existing ones
    @GetMapping("/test")
    public String test() {
        return "GFG API is working!";
    }
}