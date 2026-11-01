package framework.api;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import java.util.Map;

public final class JsonPlaceholderApiClient {

    private final APIRequestContext request;

    public JsonPlaceholderApiClient(APIRequestContext request) {
        this.request = request;
    }

    public APIResponse getPost(int postId) {
        return request.get("/posts/" + postId);
    }

    public APIResponse createPost(Map<String, Object> post) {
        return request.post("/posts", RequestOptions.create().setData(post));
    }

    public APIResponse updatePost(int postId, Map<String, Object> post) {
        return request.put("/posts/" + postId, RequestOptions.create().setData(post));
    }

    public APIResponse patchPost(int postId, Map<String, Object> changes) {
        return request.patch("/posts/" + postId, RequestOptions.create().setData(changes));
    }

    public APIResponse deletePost(int postId) {
        return request.delete("/posts/" + postId);
    }
}
