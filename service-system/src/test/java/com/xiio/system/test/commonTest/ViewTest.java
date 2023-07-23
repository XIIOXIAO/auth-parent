package com.xiio.system.test.commonTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @description:
 * @author: xiio
 * @time: 2023/6/28 16:50
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Controller
public class ViewTest {
    // @RequestMapping("testView")4
    @Test
    @RequestMapping("/testView")
    public void testForward(){
        // return "forward:www.baidu.com";
        System.out.println("666");
    }
}
