package framework.config;

import java.util.Locale;
import java.util.Optional;

public final class ConfigReader {

    public String environmentName() {
        String value = value("env", "ENV").orElse("local");
        String environmentName = value.toLowerCase(Locale.ROOT);
        if (!environmentName.matches("[a-z][a-z0-9_-]*")) {
            throw new IllegalArgumentException(
                    "Invalid environment name '" + value
                            + "'. Use a label containing letters, digits, '_' or '-'.");
        }
        return environmentName;
    }

    public String browserName() {
        String browser = value("browser", "BROWSER").orElse("chromium")
                .toLowerCase(Locale.ROOT);
        if (!browser.equals("chromium")
                && !browser.equals("firefox")
                && !browser.equals("webkit")) {
            throw new IllegalArgumentException(
                    "Invalid browser '" + browser
                            + "'. Allowed values: chromium, firefox, webkit.");
        }
        return browser;
    }

    public boolean headless() {
        String value = value("headless", "HEADLESS").orElse("true");
        if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
            throw new IllegalArgumentException(
                    "Invalid headless value '" + value + "'. Allowed values: true, false.");
        }
        return Boolean.parseBoolean(value);
    }

    public Optional<String> baseUrl(String environmentName) {
        String environmentVariable = "BASE_URL_" + environmentName.toUpperCase(Locale.ROOT);
        return property("baseUrl." + environmentName)
                .or(() -> property("baseUrl"))
                .or(() -> environmentVariable(environmentVariable))
                .or(() -> environmentVariable("BASE_URL"));
    }

    private Optional<String> value(String propertyName, String environmentVariable) {
        return property(propertyName).or(() -> environmentVariable(environmentVariable));
    }

    private Optional<String> property(String name) {
        String value = System.getProperty(name);
        return normalize(value, "system property '" + name + "'");
    }

    private Optional<String> environmentVariable(String name) {
        String value = System.getenv(name);
        return normalize(value, "environment variable '" + name + "'");
    }

    private Optional<String> normalize(String value, String source) {
        if (value == null) {
            return Optional.empty();
        }

        String normalized = value.trim();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException(source + " must not be blank.");
        }
        return Optional.of(normalized);
    }
}
