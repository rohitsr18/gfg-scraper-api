@RestController
@RequestMapping("/api/gfg")
public class GfgController {

    @Autowired
    private GfgScraperService gfgScraperService;

    @GetMapping("/{username}")
    public ResponseEntity<GfgProfile> getProfile(@PathVariable String username) {
        try {
            GfgProfile profile = gfgScraperService.scrapeProfile(username);
            return ResponseEntity.ok(profile);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}