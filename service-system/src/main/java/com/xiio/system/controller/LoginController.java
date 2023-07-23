package com.xiio.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiio.common.jwt.JwtHelper;
import com.xiio.common.result.Result;
import com.xiio.common.utils.MD5;
import com.xiio.model.system.SysMenu;
import com.xiio.model.system.SysRole;
import com.xiio.model.system.SysUser;
import com.xiio.model.vo.LoginVo;
import com.xiio.model.vo.RouterVo;
import com.xiio.system.exception.MyselfException;
import com.xiio.system.service.SysMenuService;
import com.xiio.system.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Wrapper;
import java.util.HashMap;
import java.util.List;

/**
 * @description:
 * @author: xiio
 * @time: 2023/5/4 16:32
 */

@Api(tags = "用户登录接口")
@RestController
@RequestMapping("/admin/system/index")
public class LoginController {
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuService sysMenuService;

    @ApiOperation("用户登录token")
    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo){
        //1.获取输入用户名和密码
        String username = loginVo.getUsername();
        String password = loginVo.getPassword();

        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername,username);

        SysUser sysUser = sysUserService.getOne(wrapper);
        //2.查询数据库是否存在是否被禁用且密码正确

        //2.1用户是否存在
        if (sysUser==null){
            throw new MyselfException(201,"用户不存在");
        }

        //2.2用户是否被禁用
        if(sysUser.getStatus().intValue()==0){
            throw new MyselfException(201,"用户已经被禁用");
        }

        //2.3用户密码是否正确
        String password_db = sysUser.getPassword();
        String password_MD5 = MD5.encrypt(password);

        if (!password_db.equals(password_MD5)){
            throw new MyselfException(201,"密码错误");
        }

        //3.使用JWT进行用户id和用户名称生成token字符串
        String token = JwtHelper.createToken(Long.valueOf(sysUser.getId()), sysUser.getUsername());

        //4.返回
        HashMap<String, Object> map = new HashMap<>();
        map.put("token",token);

        return Result.ok(map);
    }

    @ApiOperation("用户基本信息")
    @GetMapping("info")
    public Result info(HttpServletRequest request){
        //1.从请求头获取用户信息(获取请求头token字符串)
        String token = request.getHeader("token");
        //2.从token中获取用户id和用户名称
        Long userId = JwtHelper.getUserId(token);
        //3.根据用户id查询数据库
        SysUser sysUser = sysUserService.getById(userId);

        //4.根据用户id获取用户可以操作菜单列表【查询数据库动态构建路由结构】
        List<RouterVo> routerList = sysMenuService.findUserMenuByUserId(userId);

        //5.根据用户id获取用户可以操作的菜单列表
        List<String> permsList = sysMenuService.findUserPermsByUserId(userId);

        //6.返回数据

        HashMap<String, Object> infoMap = new HashMap<>();
        infoMap.put("roles","[admin]");
        infoMap.put("introduction","i am new xiio");
        infoMap.put("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        infoMap.put("name","Super Admin XIIO");

        //TODO 返回用户可以操作菜单
        infoMap.put("routers",routerList);

        //TODO 返回用户可以操作按钮
        infoMap.put("buttons",permsList);

        return Result.ok(infoMap);
    }

    @ApiOperation("HUTOOL")
    @PostMapping("hutool")
    public Result MyHuTool(@RequestBody LoginVo loginVo){
        System.out.println(loginVo);
        return Result.ok();
    }

}
