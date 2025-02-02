package ma.youcode.api.services;

import ma.youcode.api.annotations.AuthUser;
import ma.youcode.api.models.Post;
import ma.youcode.api.models.users.UserSecurity;
import ma.youcode.api.payloads.requests.PostRequest;
import ma.youcode.api.payloads.responses.PostResponse;
import org.starter.utilities.services.CrudService;

import java.util.UUID;

public interface PostService extends CrudService<PostResponse, PostRequest, Post, UUID> {
    PostResponse create(@AuthUser UserSecurity authUser, PostRequest request);
}
