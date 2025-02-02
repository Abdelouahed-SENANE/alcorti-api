package ma.youcode.api.repositories;

import ma.youcode.api.models.Article;
import org.starter.utilities.repositories.GenericRepository;

import java.util.UUID;

public interface ArticleRepository extends GenericRepository<Article , UUID> {
}
