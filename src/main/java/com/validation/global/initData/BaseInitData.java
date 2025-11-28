package com.validation.global.initData;

import com.validation.domain.post.post.entity.Post;
import com.validation.domain.post.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BaseInitData {
    private final PostService postService;

    @Bean
    ApplicationRunner baseInitDataApplicationRunner() {
        return arg-> {
          if (postService.count() > 0) return;

          Post post1 = postService.write("제목 1","내용 1");
          Post post2 = postService.write("제목 2","내용 2");
          Post post3 = postService.write("제목 3","내용 3");
        };
    }
}
