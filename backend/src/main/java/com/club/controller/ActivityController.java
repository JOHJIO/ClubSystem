package com.club.controller;

import com.club.common.Result;
import com.club.entity.Activity;
import com.club.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {
    @Autowired
    private ActivityService service;

    @GetMapping
    public Result page(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(required = false) String keyword) {
        return Result.success(service.page(page, size, keyword));
    }

    @GetMapping("/all")
    public Result list(@RequestParam(required = false) String keyword) {
        return Result.success(service.list(keyword));
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Integer id) {
        return Result.success(service.getById(id));
    }

    @PostMapping
    public Result create(@RequestBody Activity body) {
        service.create(body);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable Integer id, @RequestBody Activity body) {
        service.update(id, body);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        service.delete(id);
        return Result.success();
    }
}