package ma.youcode.api.utilities.mappers;

import ma.youcode.api.models.Post;
import ma.youcode.api.payloads.requests.PostRequest;
import ma.youcode.api.payloads.responses.PostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.starter.utilities.mappers.GenericMapper;

@Mapper(componentModel = "spring" , uses = ArticleMapper.class)
public interface PostMapper extends GenericMapper<Post , PostResponse , PostRequest> {

    @Override
    @Mapping(target = "customer", ignore = true)
    Post fromRequestDTO(PostRequest dto);
}
