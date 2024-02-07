package de.aclue.blog.springwithcrac.rest;

public record Comment(Integer postId, Integer id, String name, String email, String body) {

}
