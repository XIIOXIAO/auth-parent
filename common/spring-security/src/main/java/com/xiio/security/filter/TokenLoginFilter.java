package com.xiio.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.corba.se.spi.ior.ObjectKey;
import com.xiio.common.jwt.JwtHelper;
import com.xiio.common.result.Result;
import com.xiio.common.result.ResultCodeEnum;
import com.xiio.common.utils.ResponseUtil;
import com.xiio.model.vo.LoginVo;
import com.xiio.security.custom.CustomUser;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * @description:
 * @author: xiio
 * @time: 2023/5/15 17:47
 */
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {
    //1.构造方法
    public TokenLoginFilter(AuthenticationManager authenticationManager){
        this.setAuthenticationManager(authenticationManager);
        this.setPostOnly(false);
        //指定登陆的接口及提交方式，可以指定任意路径
        //即该方法进行本地化登陆接口的填写并指定该接口的请求方式
        this.setRequiresAuthenticationRequestMatcher(
                new AntPathRequestMatcher(
                        "/admin/system/index/login"
                        ,"POST")
        );
    }

    //2.登陆认证
        //2.1获取请求中传入的用户命名和密码  此处没有springboot管理只能使用原生的request方法进行获取
        //获取request用户信息的方法为UsernamePasswordAuthenticationFilter中的attemptAuthentication

    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException {
            //此处使用Jackson的objectMapper方法 将POST请求体中的数据通过request.getInputStream
            //流的方式获得,只用POST请求体使用getInputStream获得 GET方法通过getParameter即可直接获取
        try {
            //获取用户信息
            LoginVo loginVo = new ObjectMapper().readValue(
                                                request.getInputStream(),
                                                LoginVo.class);

            //封装为Authentication对象
            Authentication authenToken = new UsernamePasswordAuthenticationToken(
                                                                    loginVo.getUsername(),
                                                                    loginVo.getPassword());

            //通过AuthenticationManager调用authenticate进行认证授权
            return this.getAuthenticationManager().authenticate(authenToken);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    //3.认证成功方法
    public void successfulAuthentication(HttpServletRequest request,
                                         HttpServletResponse response,
                                         FilterChain chain,
                                         Authentication authResult){
        //1.获取当前用户
        CustomUser customUser = (CustomUser) authResult.getPrincipal();
        //2.生成token
        String token = JwtHelper.createToken(customUser.getSysUser().getId(),
                customUser.getSysUser().getUsername());
        //3.TODO返回  这里使用原生的response进行返回
        HashMap<String, Object> map = new HashMap<>();
        map.put("token",token);
        ResponseUtil.out(response, Result.ok(map));
    }



    //4.失败方法
    public void unsuccessfulAuthentication(HttpServletRequest request,
                                           HttpServletResponse response,
                                           AuthenticationException failed){
        ResponseUtil.out(response,
                            Result.build(null,
                                        ResultCodeEnum.LOGIN_ERROR));
    }


}
