package com.xiio.security.filter;

import com.xiio.common.jwt.JwtHelper;
import com.xiio.common.result.Result;
import com.xiio.common.result.ResultCodeEnum;
import com.xiio.common.utils.ResponseUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * @description:
 * @author: xiio
 * @time: 2023/5/15 19:48
 */
//登陆状态验证
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        if("/admin/system/index/login".equals(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        if(null != authentication) {
            //传入上下文对象当中
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } else {
            ResponseUtil.out(response, Result.build(null, ResultCodeEnum.PERMISSION));
        }
    }

    //进行token认证
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        //请求头中是否存在token
        String token = request.getHeader("token");

        if (!StringUtils.isEmpty(token)){
            String username = JwtHelper.getUsername(token);
            if (!StringUtils.isEmpty(username)){
                //返回一个UPT对象 只包含了用户的用户名
                return new UsernamePasswordAuthenticationToken(username,
                                                    null,
                                                              Collections.emptyList());
            }
            //TODO 没有获得用户名
            return null;
        }

        return null;
    }

}

