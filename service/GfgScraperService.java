@Service
public class GfgScraperService {

    public GfgProfile scrapeProfile(String username) throws IOException {
        String url = "https://auth.geeksforgeeks.org/user/" + username;
        Document doc = JsoupHelper.fetchDocument(url);

        GfgProfile profile = new GfgProfile();
        profile.setUsername(username);

        profile.setGlobalRank(parseField(doc, "global-rank"));
        profile.setProblemsSolved(parseProblemsSolved(doc));
        profile.setCodingScore(parseField(doc, "score-card-value"));
        profile.setContestRating(parseField(doc, "contest-rating"));

        profile.setInstitute(doc.select("div.institute").text());
        profile.setLanguage(doc.select("div.language").text());

        return profile;
    }

    private String parseField(Document doc, String className) {
        Elements elements = doc.getElementsByClass(className);
        return elements.isEmpty() ? "N/A" : elements.first().text();
    }

    private int parseProblemsSolved(Document doc) {
        Elements stats = doc.select("div.score_cards div.score-card-value");
        int total = 0;
        for (Element stat : stats) {
            String text = stat.text().replaceAll("\\D+", "");
            if (!text.isEmpty()) {
                total += Integer.parseInt(text);
            }
        }
        return total;
    }
}