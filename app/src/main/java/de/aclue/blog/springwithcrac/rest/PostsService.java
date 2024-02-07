package de.aclue.blog.springwithcrac.rest;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;

@HttpExchange(url = "/posts", accept = "application/json", contentType = "application/json")
public interface PostsService {
    @GetExchange("/")
    List<Post> getPosts();

    @GetExchange("/{id}")
    Post getPost(@PathVariable("id") long id);

    @GetExchange("/{id}/comments")
    List<Comment> getPostComments(@PathVariable("id") long id);

    @PostExchange("/")
    Post savePost(@RequestBody Post post);

    @PutExchange("/{id}")
    Post savePut(@PathVariable("id") long id, @RequestBody Post post);

    @DeleteExchange("/{id}")
    ResponseEntity<Void> deletePost(@PathVariable("id") long id);
}
