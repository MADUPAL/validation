package com.validation.domain.post.post.controller;

import com.validation.domain.post.post.entity.Post;
import com.validation.domain.post.post.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;



    record ModifyForm (
        @NotBlank(message = "01-title-제목을 입력해주세요.")
        @Size(min = 2, max = 20, message = "02-title-제목은 2자 이상, 20자 이하로 입력가능합니다.")
        String title,
        @NotBlank(message = "03-content-내용을 입력해주세요.")
        @Size(min = 2, max = 20, message = "04-content-내용은 2자 이상, 20자 이하로 입력가능합니다.")
        String content
    ){}

    @GetMapping("/posts/{id}/modify")
    public String showModify(
            @PathVariable int id,
            @ModelAttribute("form") ModifyForm form,
            Model model
    ) {
        Post post = postService.findById(id).get();

        model.addAttribute("post", new ModifyForm(post.getTitle(), post.getContent()));

        return "post/post/modify";
    }

    @PutMapping("/posts/{id}/modify")
    @Transactional
    public String modify(
            @PathVariable int id,
            @Valid @ModelAttribute("form") ModifyForm form,
            BindingResult bindingResult,
            Model model
    ) {
        Post post = postService.findById(id).get();
        model.addAttribute("post", post);

        if (bindingResult.hasErrors()) {
            return "post/post/modify";
        }

        postService.modify(post, form.title, form.content);

        return "redirect:/posts/" + post.getId();
    }

    @AllArgsConstructor
    @Getter
    public static class WriteForm {
        @NotBlank(message = "1-title-제목을 입력해주세요.")
        @Size(min = 2, max = 20, message = "2-title-제목은 2자 이상, 20자 이하로 입력가능합니다.")
        private String title;
        @NotBlank(message = "3-content-내용을 입력해주세요.")
        @Size(min = 2, max = 20, message = "4-content-내용은 2자 이상, 20자 이하로 입력가능합니다.")
        private String content;
    }

    @GetMapping("/posts/write")
    public String showWrite(@ModelAttribute("form") WriteForm form) {
        // write.html은 form에 의존하고있기 때문에
        // 비어있는 form이어도 오류가 나지 않게 하기 위해 넘겨줘야 한다.
        return "post/post/write";
    }

    @PostMapping("/posts/write")
    @Transactional
    public String write(
            @ModelAttribute("form") @Valid WriteForm form,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {

            return "post/post/write";
        }

        Post post = postService.write(form.getTitle(), form.getContent());

        model.addAttribute("post", post);

        return "redirect:/posts/" + post.getId();
    }


    @GetMapping("/posts/{id}")
    @Transactional(readOnly = true)
    public String showDetail(
            @PathVariable int id,
            Model model
    ) {
        Post post = postService.findById(id).get();

        model.addAttribute("post", post);

        return "post/post/detail";
    }

    @GetMapping("/posts")
    @Transactional(readOnly = true)
    public String showList(Model model) {
        List<Post> posts = postService.findAll();
        model.addAttribute("posts", posts);

        return "post/post/list";
    }

    @GetMapping("/posts/")
    public String redirectToList() {
        return "redirect:/posts";
    }
}