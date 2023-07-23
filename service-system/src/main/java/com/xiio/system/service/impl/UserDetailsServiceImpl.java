package com.xiio.system.service.impl;

import com.xiio.model.system.SysUser;
import com.xiio.security.custom.CustomUser;
import com.xiio.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * @description:
 * @author: xiio
 * @time: 2023/5/15 17:30
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserService sysUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        SysUser sysUser = sysUserService.findUserByUsername(username);

        if (sysUser==null){
            throw new UsernameNotFoundException("用户名不存在");
        }

        if (sysUser.getStatus().intValue()==0){
            throw new RuntimeException("账号已停用");
        }

        //将数据库查询用户结果返回给security进行处理  authorities为用户权限列表  暂时为空
        return new CustomUser(sysUser, Collections.emptyList());
    }
}
