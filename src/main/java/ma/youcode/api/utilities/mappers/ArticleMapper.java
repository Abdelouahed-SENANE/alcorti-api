package ma.youcode.api.utilities.mappers;

import ma.youcode.api.models.Article;
import ma.youcode.api.payloads.requests.ArticleRequest;
import ma.youcode.api.payloads.responses.ArticleResponse;
import ma.youcode.api.utilities.FileServiceStorage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.web.multipart.MultipartFile;
import org.starter.utilities.mappers.GenericMapper;

@Mapper(componentModel = "spring")
public interface ArticleMapper extends GenericMapper<Article, ArticleResponse, ArticleRequest> {

    @Override
    @Mapping(target = "imageURL", source = "image" , qualifiedByName = "uploadImageIsExists")
    Article fromRequestDTO(ArticleRequest dto);

    @Named("uploadImageIsExists")
    default String uploadImageIsExists(MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) {
            return null;
        }
        return FileServiceStorage.store(imageFile);
    }
}
