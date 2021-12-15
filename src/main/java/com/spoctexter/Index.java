package com.spoctexter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/")
public class Index {

    @PostMapping
    public String helloSpocWTF() {
        return "Hello and welcome to the SpocTexter app -- post mapping";
    }

    @GetMapping
    public String helloSpoc() {
        return "Hello and welcome to the SpocTexter app";
    }

}
