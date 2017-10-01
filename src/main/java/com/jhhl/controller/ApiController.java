package com.jhhl.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * test
 * Created by ALIENWARE on 2017/9/30.
 */

@RestController
public class ApiController {

    @RequestMapping({"", "/", "index"})
    public String index() throws Exception {
        return "index";
    }
}
