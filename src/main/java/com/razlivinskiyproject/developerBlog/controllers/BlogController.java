package com.razlivinskiyproject.developerBlog.controllers;

import com.razlivinskiyproject.developerBlog.models.Post;
import com.razlivinskiyproject.developerBlog.models.User;
import com.razlivinskiyproject.developerBlog.repository.WritePostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class BlogController {
    @Autowired
    private WritePostRepository repository;

    @GetMapping("/blog")
    public String blogMain(Model model) {
        Iterable<Post> posts = repository.findAll();
        model.addAttribute("posts", posts);
        return "blog-main";
    }

    @GetMapping("/blog/add")
    public String blogAdd(Model model) {
        return "blog-add";
    }

    @PostMapping("/blog/add")
    public String blogPostAdd(
            @AuthenticationPrincipal User author,
            @RequestParam String title,
            @RequestParam String anons,
            @RequestParam String text,
            Model model) {
        Post post = new Post(title, anons, text, author);
        repository.save(post);
        return "redirect:/blog";
    }

    @GetMapping("/blog/{id}")
    public String blogDetails(
            @PathVariable(value = "id") long id,
            Model model) {
        if(!repository.existsById(id)) {
            return "redirect:/blog";
        }
        Optional<Post> post = repository.findById(id);
        ArrayList<Post> lsitPost = new ArrayList<>();
        post.ifPresent(lsitPost::add);
        model.addAttribute("post", lsitPost);
        return "blog-details";
    }

    @GetMapping("/blog/{id}/edit")
    public String blogEdit(
            @PathVariable(value = "id") long id,
            Model model) {
        if(!repository.existsById(id)) {
            return "redirect:/blog";
        }
        Optional<Post> post = repository.findById(id);
        ArrayList<Post> lsitPost = new ArrayList<>();
        post.ifPresent(lsitPost::add);
        model.addAttribute("post", lsitPost);
        return "blog-edit";
    }

    @PostMapping("/blog/{id}/edit")
    public String blogPostUpdate(
            @PathVariable(value = "id") long id,
            @RequestParam String title,
            @RequestParam String anons,
            @RequestParam String text,
            Model model) {
        Post post = repository.findById(id).orElseThrow(() -> new RuntimeException());
        post.setTitle(title);
        post.setAnons(anons);
        post.setText(text);
        repository.save(post);
        return "redirect:/blog";
    }

    @PostMapping("/blog/{id}/remove")
    public String blogPostDelete(
            @PathVariable(value = "id") long id,
            Model model) {
        Post post = repository.findById(id).orElseThrow(() -> new RuntimeException());
        repository.delete(post);
        return "redirect:/blog";
    }
}
