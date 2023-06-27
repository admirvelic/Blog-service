package com.vella.blog.controller;

import com.vella.blog.exception.CustomErrorException;
import com.vella.blog.model.Blog;
import com.vella.blog.model.requests.BlogCreateRequest;
import com.vella.blog.service.BlogService;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/blogs")
@RequiredArgsConstructor
@Slf4j
public class BlogController {
  private final BlogService blogService;

  @GetMapping("/{id}")
  public Blog getBlogById(@PathVariable Long id) throws CustomErrorException {
    return blogService.getBlogById(id);
  }

  @PutMapping("/{id}/update")
  public Blog updateBlog(@PathVariable Long id, @RequestBody BlogCreateRequest request)
      throws CustomErrorException, IOException {
    return blogService.updateBlog(id, request);
  }

  @PostMapping("/save")
  public Blog saveBlog(@RequestBody BlogCreateRequest request, @RequestHeader (name="Authorization") String token)
      throws CustomErrorException {
    return blogService.saveBlog(request, token);
  }

  @GetMapping("/all")
  public List<Blog> getAllBlog() throws CustomErrorException {
    return blogService.getAllBlogs();
  }

  @DeleteMapping("/{id}/delete")
  public String deleteBlog(@PathVariable Long id) throws CustomErrorException {
    blogService.deleteBlog(id);
    return "Successfully deleted app user with id " + id;
  }
}
