package framework.api;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ApiTestContext {

    private static final Logger LOGGER = LogManager.getLogger(ApiTestContext.class);

    private final Thread ownerThread;
    private Playwright playwright;
    private APIRequestContext apiRequestContext;
    private boolean closed;

    private ApiTestContext() {
        ownerThread = Thread.currentThread();
    }

    public static ApiTestContext create(String apiBaseUrl) {
        String validatedApiBaseUrl = validateApiBaseUrl(apiBaseUrl);
        ApiTestContext context = new ApiTestContext();

        try {
            context.playwright = Playwright.create();
            context.apiRequestContext = context.playwright.request().newContext(
                    new APIRequest.NewContextOptions().setBaseURL(validatedApiBaseUrl));
            return context;
        } catch (RuntimeException | Error setupFailure) {
            context.close(setupFailure);
            throw setupFailure;
        }
    }

    public APIRequestContext apiRequestContext() {
        ensureOpen();
        return apiRequestContext;
    }

    public void close(Throwable primaryFailure) {
        assertOwnerThread();
        if (closed) {
            return;
        }
        closed = true;

        Throwable cleanupFailure = null;
        if (apiRequestContext != null) {
            cleanupFailure = closeResource(
                    "APIRequestContext", apiRequestContext::dispose, cleanupFailure);
            apiRequestContext = null;
        }
        if (playwright != null) {
            cleanupFailure = closeResource("Playwright", playwright::close, cleanupFailure);
            playwright = null;
        }

        if (cleanupFailure == null) {
            return;
        }
        if (primaryFailure != null) {
            if (primaryFailure != cleanupFailure) {
                primaryFailure.addSuppressed(cleanupFailure);
            }
            LOGGER.error("API resource cleanup failed; attached to the primary failure.", cleanupFailure);
            return;
        }
        rethrow(cleanupFailure);
    }

    private static String validateApiBaseUrl(String apiBaseUrl) {
        if (apiBaseUrl == null || apiBaseUrl.isBlank()) {
            throw new IllegalArgumentException("API base URL must not be blank.");
        }

        String normalizedApiBaseUrl = apiBaseUrl.trim();
        try {
            URI uri = new URI(normalizedApiBaseUrl);
            String scheme = uri.getScheme();
            if (!uri.isAbsolute()
                    || uri.getHost() == null
                    || !(scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https"))) {
                throw new IllegalArgumentException(
                        "Invalid API base URL '" + apiBaseUrl
                                + "'. Configure an absolute HTTP or HTTPS URL with a host.");
            }
            int port = uri.getPort();
            if (port != -1 && (port < 1 || port > 65535)) {
                throw new IllegalArgumentException(
                        "Invalid port in API base URL '" + apiBaseUrl
                                + "'. Port must be between 1 and 65535.");
            }
        } catch (URISyntaxException exception) {
            throw new IllegalArgumentException(
                    "Invalid API base URL '" + apiBaseUrl
                            + "'. Configure a valid absolute HTTP or HTTPS URL.",
                    exception);
        }
        return normalizedApiBaseUrl;
    }

    private Throwable closeResource(
            String resourceName, Runnable closeOperation, Throwable previousFailure) {
        try {
            closeOperation.run();
        } catch (RuntimeException | Error failure) {
            LOGGER.error("Failed to close {}.", resourceName, failure);
            if (previousFailure == null) {
                return failure;
            }
            if (previousFailure != failure) {
                previousFailure.addSuppressed(failure);
            }
        }
        return previousFailure;
    }

    private void ensureOpen() {
        assertOwnerThread();
        if (closed) {
            throw new IllegalStateException("API test context has already been closed.");
        }
    }

    private void assertOwnerThread() {
        Thread currentThread = Thread.currentThread();
        if (currentThread != ownerThread) {
            throw new IllegalStateException(
                    "ApiTestContext is owned by thread '" + ownerThread.getName()
                            + "' (id=" + ownerThread.getId() + "), but was accessed from thread '"
                            + currentThread.getName() + "' (id=" + currentThread.getId() + ").");
        }
    }

    private void rethrow(Throwable failure) {
        if (failure instanceof RuntimeException runtimeException) {
            throw runtimeException;
        }
        throw (Error) failure;
    }
}
