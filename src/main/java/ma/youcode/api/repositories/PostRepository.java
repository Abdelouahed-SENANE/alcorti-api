package ma.youcode.api.repositories;

import ma.youcode.api.models.Post;
import org.starter.utilities.repositories.GenericRepository;

import java.util.UUID;

public interface PostRepository extends GenericRepository<Post, UUID> {
}
