package com.vella.blog.service;

import com.vella.blog.exception.CustomErrorException;
import com.vella.blog.model.Blog;
import com.vella.blog.model.requests.BlogCreateRequest;
import com.vella.blog.repository.BlogRepo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import com.vella.security.token.TokenRepository;
import com.vella.security.user.User;
import com.vella.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BlogServiceImpl implements BlogService{

  private final BlogRepo blogRepo;
  private final UserRepository appUserRepo;
  private final TokenRepository tokenRepo;
  @Override
  public Blog getBlogById(Long id) {
    try {
      log.info("Fetching blog by id {}", id);
      Optional<Blog> blog = blogRepo.findById(id);
      if (blog.isEmpty()) {
        throw new CustomErrorException("Blog not found");
      }
      return blog.get();
    } catch (Exception e) {
      log.error("Error fetching blog {}", e.getMessage());
      throw new CustomErrorException(HttpStatus.NOT_FOUND, e.getMessage());
    }
  }

  @Override
  public Blog getBlogByTitle(String title) {
    log.info("Fetching blog by title {}", title);
    Optional<Blog> blog = blogRepo.findByTitle(title);
    if (blog.isEmpty()) {
      throw new CustomErrorException("Blog is missing");
    }
    return blog.get();
  }

  @Override
  public List<Blog> getAllBlogs() {
    try {
      log.info("Fetching all blogs");
      return blogRepo.findAll();
    } catch (Exception e) {
      log.error("Error fetching all blogs");
      throw new CustomErrorException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }

  @Override
  public Blog saveBlog(Blog blog) {
    if(blog.getTitle().isEmpty()){
      log.error("Error saving new blog. Field 'Title' is empty.");
      throw new IllegalArgumentException("Field 'Title' is empty");
    }
    if(blog.getText().isEmpty()){
      log.error("Error saving new blog. Field 'Text' is empty");
      throw new IllegalArgumentException("Field 'Text' is empty");
    }
    if((appUserRepo.findById(blog.getUser().getId())).isEmpty()) {
      log.error("Error saving new blog. Field 'User' is empty");
      throw new IllegalArgumentException("Field 'User' is empty");
    }
    Blog dbBlog = new Blog();
    dbBlog.setTitle(blog.getTitle());
    dbBlog.setText(blog.getText());
    dbBlog.setUser(blog.getUser());
    dbBlog.setTimeEditedAt(LocalTime.now());
    dbBlog.setTimeCreatedAt(LocalTime.now());
    dbBlog.setDataCreatedAt(LocalDate.now());
    dbBlog.setDateEditedAt(LocalDate.now());
    return blogRepo.save(blog);
  }

  @Override
  public Blog saveBlog(BlogCreateRequest request, String token) {
    try {
      if (request.getTitle() == null) throw new IllegalArgumentException("Field 'Title' is missing");
      if(request.getTitle().isEmpty()){
        log.error("Error saving new blog. Field 'Title' is empty.");
        throw new IllegalArgumentException("Field 'Title' is empty");
      }
      if(request.getText().isEmpty()){
        log.error("Error saving new blog. Field 'Text' is empty");
        throw new IllegalArgumentException("Field 'Text' is empty");
      }
      Blog blog = new Blog();

      blog.setTitle(request.getTitle());
      blog.setText(request.getText());
      blog.setDataCreatedAt(LocalDate.now());
      blog.setTimeCreatedAt(LocalTime.now());
      blog.setDateEditedAt(LocalDate.now());
      blog.setTimeEditedAt(LocalTime.now());

      if(tokenRepo.findUserByToken(token).isEmpty()){
        throw new IllegalArgumentException("Field saving new blog. No AppUser found");
      }
      User user = tokenRepo.findUserByToken(token).get();

      blog.setUser(user);

      log.info("Saving new blog {} to the database", blog.getId());
      return blogRepo.save(blog);
    } catch (Exception e) {
      log.error("Error crating new blog {}", e.getMessage());
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }

  @Override
  public Blog updateBlog(Long id, Blog blog) {
    if(blog.getTitle().isEmpty()){
      log.error("Error saving new blog. Field 'Title' is empty.");
      throw new IllegalArgumentException("Field 'Title' is empty");
    }
    if(blog.getText().isEmpty()){
      log.error("Error saving new blog. Field 'Text' is empty");
      throw new IllegalArgumentException("Field 'Text' is empty");
    }
    if((appUserRepo.findById(blog.getUser().getId())).isEmpty()) {
      log.error("Error saving new blog. Field 'User' is empty");
      throw new IllegalArgumentException("Field 'User' is empty");
    }
    Blog blogDb = new Blog();
    if (blog.getId() != null) blogDb.setId(blog.getId());
    blog.setTitle(blog.getTitle());
    blog.setText(blog.getText());
    blog.setDateEditedAt(LocalDate.now());
    blog.setTimeEditedAt(LocalTime.now());

    return blogRepo.save(blogDb);
  }

  @Override
  public Blog updateBlog(Long id, BlogCreateRequest request) {
    try {
      if (request.getTitle() == null) throw new IllegalArgumentException("Field 'Title' is missing");
      if(request.getTitle().isEmpty()){
        log.error("Error saving new blog. Field 'Title' is empty.");
        throw new IllegalArgumentException("Field 'Title' is empty");
      }
      if(request.getText().isEmpty()){
        log.error("Error saving new blog. Field 'Text' is empty");
        throw new IllegalArgumentException("Field 'Text' is empty");
      }
      Blog dbBlog = new Blog();

      dbBlog.setTitle(request.getTitle());
      dbBlog.setText(request.getText());
      dbBlog.setDateEditedAt(LocalDate.now());
      dbBlog.setTimeEditedAt(LocalTime.now());
      return blogRepo.save(dbBlog);
    } catch (Exception e) {
      log.error("Error updating blog {}", e.getMessage());
      throw new CustomErrorException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }

  @Override
  public void deleteBlog(Long id) {
    try {
      log.info("Deleting blog with id {}", id);
      blogRepo.deleteById(id);
    } catch (Exception e) {
      throw new CustomErrorException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }
}
