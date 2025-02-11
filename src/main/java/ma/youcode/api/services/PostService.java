package ma.youcode.api.services;

import ma.youcode.api.models.Post;
import ma.youcode.api.payloads.requests.PostRequest;
import ma.youcode.api.payloads.responses.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.starter.utilities.services.crud.ReadAllService;
import org.starter.utilities.services.crud.ReadByIdService;
import org.starter.utilities.services.support.FindAndExecuteService;

import java.util.UUID;

public interface PostService extends
        ReadAllService<PostResponse, PostRequest, Post, UUID>,
        ReadByIdService<PostResponse, PostRequest, Post, UUID>,
        FindAndExecuteService<PostResponse, PostRequest, Post, UUID> {

    Page<PostResponse> loadCustomerPosts(Pageable pageable);
    PostResponse create(PostRequest request);

    PostResponse update( UUID postId, PostRequest request);

    void delete(UUID postId);
}
