package de.aclue.blog.springwithcrac.rest;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("posts")
@RequiredArgsConstructor
public class PostsController {

    private final PostsClient postsClient;

    @GetMapping
    public List<Post> getPosts() {
        return postsClient.getPostsService().getPosts();
    }

    @GetMapping("{id}")
    public Post getPost(@PathVariable long id) {
        return postsClient.getPostsService().getPost(id);
    }

    @GetMapping("{id}/comments")
    public List<Comment> getPostComments(@PathVariable long id) {
        return postsClient.getPostsService().getPostComments(id);
    }

    @PostMapping
    public Post savePost(Post post) {
        return postsClient.getPostsService().savePost(post);
    }

    @PutMapping("/{id}")
    public Post savePut(@PathVariable long id, Post post) {
        return postsClient.getPostsService().savePut(id, post);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletePost(@PathVariable long id) {
        return postsClient.getPostsService().deletePost(id);
    }
}
