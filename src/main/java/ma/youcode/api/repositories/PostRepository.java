package ma.youcode.api.repositories;

import ma.youcode.api.models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.starter.utilities.repositories.GenericRepository;

import java.util.UUID;

public interface PostRepository extends GenericRepository<Post, UUID> {

    Page<Post> findAllByCustomerId(Pageable pageable , UUID customerId);
}
