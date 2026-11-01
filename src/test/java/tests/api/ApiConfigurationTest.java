package tests.api;

import framework.config.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.Test;

public final class ApiConfigurationTest {

    private static final String ENVIRONMENT_SPECIFIC_PROPERTY = "apiBaseUrl.qa";
    private static final String GENERIC_PROPERTY = "apiBaseUrl";
    private static final Object SYSTEM_PROPERTY_LOCK = new Object();

    @Test
    public void environmentSpecificApiBaseUrlTakesPrecedence() {
        synchronized (SYSTEM_PROPERTY_LOCK) {
            String originalEnvironmentSpecific = System.getProperty(ENVIRONMENT_SPECIFIC_PROPERTY);
            String originalGeneric = System.getProperty(GENERIC_PROPERTY);

            try {
                System.setProperty(ENVIRONMENT_SPECIFIC_PROPERTY, "https://qa-api.example.test");
                System.setProperty(GENERIC_PROPERTY, "https://generic-api.example.test");

                Assert.assertEquals(
                        new ConfigReader().apiBaseUrl("qa").orElseThrow(),
                        "https://qa-api.example.test");
            } finally {
                restoreProperty(ENVIRONMENT_SPECIFIC_PROPERTY, originalEnvironmentSpecific);
                restoreProperty(GENERIC_PROPERTY, originalGeneric);
            }
        }
    }

    @Test
    public void genericApiBaseUrlIsUsedWithoutEnvironmentSpecificProperty() {
        synchronized (SYSTEM_PROPERTY_LOCK) {
            String originalEnvironmentSpecific = System.getProperty(ENVIRONMENT_SPECIFIC_PROPERTY);
            String originalGeneric = System.getProperty(GENERIC_PROPERTY);

            try {
                System.clearProperty(ENVIRONMENT_SPECIFIC_PROPERTY);
                System.setProperty(GENERIC_PROPERTY, "https://generic-api.example.test");

                Assert.assertEquals(
                        new ConfigReader().apiBaseUrl("qa").orElseThrow(),
                        "https://generic-api.example.test");
            } finally {
                restoreProperty(ENVIRONMENT_SPECIFIC_PROPERTY, originalEnvironmentSpecific);
                restoreProperty(GENERIC_PROPERTY, originalGeneric);
            }
        }
    }

    private void restoreProperty(String propertyName, String originalValue) {
        if (originalValue == null) {
            System.clearProperty(propertyName);
        } else {
            System.setProperty(propertyName, originalValue);
        }
    }
}
