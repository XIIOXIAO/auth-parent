package com.xiio.system.test.activti;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: xiio
 * @time: 2023/5/15 9:52
 */
@Component
public class UserBean {
    public String getUsername(int id){
        if (id==1){
            return "XIIO";
        }else {
            return "UIIO";
        }
    }
}
