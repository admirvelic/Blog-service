package com.vella.blog.repository;

import com.vella.blog.model.Blog;
import java.util.Optional;
import io.micrometer.common.lang.NonNullApi;
import org.springframework.data.jpa.repository.JpaRepository;

@NonNullApi
public interface BlogRepo extends JpaRepository<Blog, Long> {

  @Override
  Optional<Blog> findById(Long id);

  Optional<Blog> findByTitle(String title);
}
