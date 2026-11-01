package tests.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIResponse;
import com.networknt.schema.Error;
import com.networknt.schema.Schema;
import com.networknt.schema.SchemaRegistry;
import com.networknt.schema.SpecificationVersion;
import framework.api.JsonPlaceholderApiClient;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.base.ApiBaseTest;

public final class JsonPlaceholderApiTest extends ApiBaseTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String POST_SCHEMA_RESOURCE = "schemas/post.schema.json";

    @Test
    public void getPostReturnsExpectedPost() throws Exception {
        JsonPlaceholderApiClient client =
                new JsonPlaceholderApiClient(getApiTestContext().apiRequestContext());
        APIResponse response = client.getPost(1);

        try {
            Assert.assertEquals(response.status(), 200, "GET /posts/1 should return HTTP 200.");
            JsonNode post = parseAndValidatePost(response.text());

            Assert.assertEquals(post.path("id").asInt(), 1, "Post ID should be 1.");
            Assert.assertEquals(post.path("userId").asInt(), 1, "User ID should be 1.");
        } finally {
            response.dispose();
        }
    }

    @Test
    public void createPostReturnsSubmittedFieldsAndIdentifier() throws Exception {
        Map<String, Object> submittedPost = Map.of(
                "title", "M6 API foundation",
                "body", "A simulated JSONPlaceholder post.",
                "userId", 1);
        JsonPlaceholderApiClient client =
                new JsonPlaceholderApiClient(getApiTestContext().apiRequestContext());
        APIResponse response = client.createPost(submittedPost);

        try {
            Assert.assertEquals(response.status(), 201, "POST /posts should return HTTP 201.");
            JsonNode createdPost = parseAndValidatePost(response.text());

            Assert.assertEquals(createdPost.path("title").asText(), submittedPost.get("title"));
            Assert.assertEquals(createdPost.path("body").asText(), submittedPost.get("body"));
            Assert.assertEquals(createdPost.path("userId").asInt(), submittedPost.get("userId"));
            Assert.assertTrue(
                    createdPost.path("id").isIntegralNumber(),
                    "Created post ID should be an integer.");
            Assert.assertTrue(
                    createdPost.path("id").asLong() > 0,
                    "Created post ID should be positive.");
        } finally {
            response.dispose();
        }
    }

    private JsonNode parseAndValidatePost(String responseBody) throws Exception {
        JsonNode post = OBJECT_MAPPER.readTree(responseBody);
        Schema schema;
        try (InputStream schemaStream = getClass().getClassLoader()
                .getResourceAsStream(POST_SCHEMA_RESOURCE)) {
            if (schemaStream == null) {
                throw new IllegalStateException(
                        "Post JSON schema resource not found: " + POST_SCHEMA_RESOURCE);
            }
            schema = SchemaRegistry.withDefaultDialect(SpecificationVersion.DRAFT_2020_12)
                    .getSchema(schemaStream);
        }

        List<Error> validationMessages = schema.validate(post);
        Assert.assertTrue(
                validationMessages.isEmpty(),
                "Post response does not match schema: " + validationMessages);
        return post;
    }
}
