import java.util.HashMap;
import java.util.Map;

public class URLShortenerService {
    private Map<String, String> urlMap;
    private Map<String, String> reverseUrlMap;
    private static final String BASE_URL = "http://localhost:8000/";
    private static final String CHAR_SET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int SHORT_URL_LENGTH = 6;

    public URLShortenerService() {
        urlMap = new HashMap<>();
        reverseUrlMap = new HashMap<>();
    }

    public String shortenURL(String longUrl) {
        if (reverseUrlMap.containsKey(longUrl)) {
            return reverseUrlMap.get(longUrl);
        }

        String shortUrl;
        do {
            shortUrl = generateShortURL();
        } while (urlMap.containsKey(shortUrl));

        urlMap.put(shortUrl, longUrl);
        reverseUrlMap.put(longUrl, shortUrl);
        return BASE_URL + shortUrl;
    }

    public String expandURL(String shortUrl) {
        String key = shortUrl.replace(BASE_URL, "");
        return urlMap.getOrDefault(key, "Invalid short URL");
    }

    private String generateShortURL() {
        StringBuilder shortUrl = new StringBuilder(SHORT_URL_LENGTH);
        for (int i = 0; i < SHORT_URL_LENGTH; i++) {
            int index = (int) (CHAR_SET.length() * Math.random());
            shortUrl.append(CHAR_SET.charAt(index));
        }
        return shortUrl.toString();
    }
}

