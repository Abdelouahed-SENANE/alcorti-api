package ma.youcode.api.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.youcode.api.annotations.AuthUser;
import ma.youcode.api.models.Post;
import ma.youcode.api.models.users.Customer;
import ma.youcode.api.models.users.UserSecurity;
import ma.youcode.api.payloads.requests.PostRequest;
import ma.youcode.api.payloads.responses.PostResponse;
import ma.youcode.api.services.PostService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.starter.utilities.controllers.CrudController;
import org.starter.utilities.controllers.DeleteController;
import org.starter.utilities.controllers.ReadAllController;
import org.starter.utilities.controllers.ReadController;
import org.starter.utilities.dtos.SimpleSuccessDTO;
import org.starter.utilities.markers.validation.OnCreate;
import org.starter.utilities.services.CrudService;

import java.util.UUID;

import static org.starter.utilities.response.Response.simpleSuccess;

@RestController
@RequestMapping("api/v1/posts")
@RequiredArgsConstructor
@Validated
public class PostController implements
        ReadController<PostResponse, PostRequest, Post,UUID>,
        ReadAllController<PostResponse, PostRequest, Post,UUID>,
        DeleteController<PostResponse, PostRequest, Post,UUID>

{

    private final PostService postService;


    @PostMapping("/customer")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<SimpleSuccessDTO> handleCreate(@ModelAttribute @Validated({OnCreate.class}) PostRequest request , @AuthUser UserSecurity authUser ) {
        PostResponse response = postService.create(authUser , request);
        return simpleSuccess(201, "Post created successfully.", response);
    }

    @Override
    public CrudService<PostResponse, PostRequest, Post, UUID> getService() {
        return postService;
    }

    @Override
    public Class<Post> getEntityClass() {
        return Post.class;
    }
}
