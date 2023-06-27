package com.vella.blog.service;


import com.vella.blog.model.Blog;
import com.vella.blog.model.requests.BlogCreateRequest;
import java.io.IOException;
import java.util.List;

public interface BlogService {

  Blog getBlogById(Long id);

  Blog getBlogByTitle(String Title);

  List<Blog> getAllBlogs();

  Blog saveBlog(Blog blog);

  Blog saveBlog(BlogCreateRequest request, String token);

  Blog updateBlog(Long id, Blog blog) throws IOException;

  Blog updateBlog(Long id, BlogCreateRequest request) throws IOException;

  void deleteBlog(Long id);
}
