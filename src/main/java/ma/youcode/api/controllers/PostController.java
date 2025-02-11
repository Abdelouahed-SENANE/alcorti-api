package ma.youcode.api.controllers;

import lombok.RequiredArgsConstructor;
import ma.youcode.api.payloads.requests.PostRequest;
import ma.youcode.api.payloads.responses.PostResponse;
import ma.youcode.api.services.PostService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.starter.utilities.dtos.SimpleSuccessDTO;
import org.starter.utilities.markers.validation.OnCreate;
import org.starter.utilities.markers.validation.OnUpdate;

import java.util.UUID;

import static org.starter.utilities.response.Response.simpleSuccess;

@RestController
@RequestMapping("api/v1/posts")
@RequiredArgsConstructor
@Validated
public class PostController {

    private final PostService postService;
    private static  final String DEFAULT_PAGE = "0";
    private static  final String DEFAULT_SIZE = "10";


    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<SimpleSuccessDTO> handleAllPosts(@RequestParam(value = "page", defaultValue = DEFAULT_PAGE) int page, @RequestParam(value = "size", defaultValue = DEFAULT_SIZE) int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        return simpleSuccess(200, "Posts fetched successfully.", postService.readAll(pageable));
    }

    @GetMapping("/all/customer")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<SimpleSuccessDTO> getAllPostsForCustomer(@RequestParam(value = "page", defaultValue = DEFAULT_PAGE) int page, @RequestParam(value = "size", defaultValue = DEFAULT_SIZE) int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        return simpleSuccess(200, "Posts fetched successfully.", postService.loadCustomerPosts(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SimpleSuccessDTO> handleRead(@PathVariable UUID id) {
        return simpleSuccess(200, "Post fetched successfully.", postService.readById(id));
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<SimpleSuccessDTO> handleCreate(@ModelAttribute @Validated({OnCreate.class}) PostRequest request) {
        PostResponse response = postService.create(request);
        return simpleSuccess(201, "Post created successfully.", response);
    }

    @PutMapping("/update/{postId}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<SimpleSuccessDTO> handleUpdate(@ModelAttribute @Validated({OnUpdate.class}) PostRequest request, @PathVariable UUID postId) {
        PostResponse response = postService.update(postId, request);
        return simpleSuccess(201, "Post updated successfully.", response);
    }

    @DeleteMapping("/delete/{postId}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<SimpleSuccessDTO> handleDelete(@PathVariable UUID postId) {
        postService.delete(postId);
        return simpleSuccess(201, "Post deleted successfully.");
    }


}
