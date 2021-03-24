package com.lrm.web;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
public class Test {
    @PostMapping("/aa")
    void aa(@RequestParam(required = false) String username)
    {
        System.out.println(username);
    }
}
