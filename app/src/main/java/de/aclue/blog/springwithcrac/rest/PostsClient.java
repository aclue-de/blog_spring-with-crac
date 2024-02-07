package de.aclue.blog.springwithcrac.rest;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Component
public class PostsClient {
    private final PostsService postsService;

    public PostsClient() {
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(RestClient.create("https://jsonplaceholder.typicode.com")))
                .build();
        postsService = httpServiceProxyFactory.createClient(PostsService.class);
    }

    public PostsService getPostsService() {
        return postsService;
    }
}
