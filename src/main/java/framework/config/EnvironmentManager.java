package framework.config;

import java.net.URI;
import java.net.URISyntaxException;

public final class EnvironmentManager {

    private final ConfigReader configReader;

    public EnvironmentManager() {
        this(new ConfigReader());
    }

    EnvironmentManager(ConfigReader configReader) {
        this.configReader = configReader;
    }

    public Configuration resolve() {
        String environmentName = configReader.environmentName();
        String browserName = configReader.browserName();
        boolean headless = configReader.headless();
        String baseUrl = configReader.baseUrl(environmentName)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No base URL configured for environment '" + environmentName
                                + "'. Set -DbaseUrl or BASE_URL (or an environment-specific override)."));

        validateBaseUrl(baseUrl);
        return new Configuration(environmentName, baseUrl, browserName, headless);
    }

    private void validateBaseUrl(String baseUrl) {
        try {
            URI uri = new URI(baseUrl);
            String scheme = uri.getScheme();
            if (!uri.isAbsolute()
                    || uri.getHost() == null
                    || !(scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https"))) {
                throw new IllegalArgumentException(
                        "Invalid base URL '" + baseUrl
                                + "'. Configure an absolute HTTP or HTTPS URL with a host.");
            }
            int port = uri.getPort();
            if (port != -1 && (port < 1 || port > 65535)) {
                throw new IllegalArgumentException(
                        "Invalid port in base URL '" + baseUrl
                                + "'. Port must be between 1 and 65535.");
            }
        } catch (URISyntaxException exception) {
            throw new IllegalArgumentException(
                    "Invalid base URL '" + baseUrl + "'. Configure a valid absolute HTTP or HTTPS URL.",
                    exception);
        }
    }

    public record Configuration(
            String environmentName,
            String baseUrl,
            String browserName,
            boolean headless) {
    }
}
