package ma.youcode.api.services.implementations;

import lombok.RequiredArgsConstructor;
import ma.youcode.api.annotations.AuthUser;
import ma.youcode.api.enums.PostStatus;
import ma.youcode.api.models.Post;
import ma.youcode.api.models.users.Customer;
import ma.youcode.api.models.users.User;
import ma.youcode.api.models.users.UserSecurity;
import ma.youcode.api.payloads.requests.PostRequest;
import ma.youcode.api.payloads.responses.PostResponse;
import ma.youcode.api.repositories.PostRepository;
import ma.youcode.api.services.PostService;
import ma.youcode.api.services.UserService;
import ma.youcode.api.utilities.mappers.PostMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.starter.utilities.mappers.GenericMapper;
import org.starter.utilities.repositories.GenericRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private static final Logger log = LogManager.getLogger(PostServiceImpl.class);
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final UserService userService;

    @Override
    public GenericRepository<Post, UUID> getRepository() {
        return postRepository;
    }

    @Override
    public GenericMapper<Post, PostResponse, PostRequest> getMapper() {
        return postMapper;
    }

    @Override
    public PostResponse create(@AuthUser UserSecurity authUser, PostRequest request) {

        User customer =  userService.findById(authUser.getId());
        Post post = postMapper.fromRequestDTO(request);
        post.setCustomer((Customer) customer);

        post.setPostStatus(PostStatus.PENDING);

        post.getArticles().forEach(article -> {
            article.setPost(post);
            article.calculateVolume();
        });

        Post savedPost = postRepository.save(post);

        return postMapper.toResponseDTO(savedPost);
    }


}
