package ma.youcode.api.services.implementations;

import lombok.RequiredArgsConstructor;
import ma.youcode.api.enums.PostStatus;
import ma.youcode.api.exceptions.PostAccessDeniedException;
import ma.youcode.api.models.Post;
import ma.youcode.api.models.users.UserSecurity;
import ma.youcode.api.models.users.Customer;
import ma.youcode.api.models.users.User;
import ma.youcode.api.payloads.requests.PostRequest;
import ma.youcode.api.payloads.responses.PostResponse;
import ma.youcode.api.repositories.ArticleRepository;
import ma.youcode.api.repositories.PostRepository;
import ma.youcode.api.services.PostService;
import ma.youcode.api.services.UserService;
import ma.youcode.api.utilities.mappers.ArticleMapper;
import ma.youcode.api.utilities.mappers.PostMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.starter.utilities.mappers.GenericMapper;
import org.starter.utilities.repositories.GenericRepository;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private static final Logger log = LogManager.getLogger(PostServiceImpl.class);
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final UserService userService;
    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    @Override
    public GenericRepository<Post, UUID> getRepository() {
        return postRepository;
    }

    @Override
    public GenericMapper<Post, PostResponse, PostRequest> getMapper() {
        return postMapper;
    }

    @Override
    public PostResponse create(PostRequest request) {

        UUID customerId = getAuthUser().getId();

        User customer = userService.findById(customerId);
        Post post = postMapper.fromRequestDTO(request);

        post.setCustomer((Customer) customer);
        post.setPostStatus(PostStatus.PENDING);

        post.getArticles().forEach(article -> {
            article.setPost(post);
            article.calculateVolume();
        });

        return postMapper.toResponseDTO(postRepository.save(post));
    }

    @Override
    public PostResponse update(UUID postId, PostRequest request) {

        return findAndExecute(postId, post -> {

            verifyAccess(post, "update");
            postMapper.updateEntity(request, post);
            return postMapper.toResponseDTO(postRepository.save(post));
        });

    }

    @Override
    public void delete(UUID postId) {
        findAndExecute(postId, post -> {
            verifyAccess(post, "delete");
            postRepository.delete(post);
        });
    }

    @Override
    public Page<PostResponse> loadCustomerPosts(Pageable pageable) {
        UUID customerId = getAuthUser().getId();
        Page<Post> posts = postRepository.findAllByCustomerId(pageable, customerId);
        return posts.map(postMapper::toResponseDTO);
    }

    private UserSecurity getAuthUser() {
        return (UserSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private boolean isPostOwner(Post post) {
        return Objects.equals(post.getCustomer().getId(), getAuthUser().getId());
    }

    private void verifyAccess(Post post, String action) {
        if (!isPostOwner(post)) {
            throw new PostAccessDeniedException(String.format("You don't have permission to %s this post", action));
        }
    }
}
