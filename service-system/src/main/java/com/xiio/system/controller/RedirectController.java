package com.xiio.system.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @description:
 * @author: xiio
 * @time: 2023/5/13 17:05
 */
@Api(tags = "redirectTest")
@Controller
@RequestMapping("/jumpTest")
public class RedirectController {
    @ApiOperation("跳转测试")
    @GetMapping("myJump")
    public String test(){
        return "redirect:/myself";
    }

    @GetMapping("myself")
    public String test2(){
        return "myself";
    }
}
