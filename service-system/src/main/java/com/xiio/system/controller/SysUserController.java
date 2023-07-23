package com.xiio.system.controller;


import com.xiio.common.utils.MD5;
import com.xiio.model.system.SysUser;
import com.xiio.model.vo.SysUserQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiio.common.result.Result;
import com.xiio.system.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.PresentationDirection;
import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author xiio
 * @since 2023-05-08
 */

@Api(tags = "用户管理接口")
@RestController
@RequestMapping("/admin/system/sysUser")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;


    //用户条件分页查询
    @ApiOperation("用户分页查询")
    @GetMapping("{page}/{limit}")
    public Result index(@PathVariable Long page,
                        @PathVariable Long limit,
                        SysUserQueryVo sysUserQueryVo){
        //
        Page<SysUser> pageParam = new Page<>(page,limit);

        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        String keyword = sysUserQueryVo.getKeyword();
        String createTimeBegin = sysUserQueryVo.getCreateTimeBegin();
        String createTimeEnd = sysUserQueryVo.getCreateTimeEnd();

        if(!StringUtils.isEmpty(keyword)){
            wrapper.like(SysUser::getUsername,keyword);
        }
        if(!StringUtils.isEmpty(createTimeBegin)){
            wrapper.ge(SysUser::getCreateTime,createTimeBegin);
        }
        if(!StringUtils.isEmpty(createTimeEnd)){
            wrapper.le(SysUser::getCreateTime,createTimeEnd);
        }
        IPage<SysUser> pageModel = sysUserService.page(pageParam, wrapper);

        return Result.ok(pageModel);
    }
    //1.查询所有记录
    @ApiOperation(value = "查询所有角色")
    @GetMapping("findAll")
    public Result findAllRole(){
        //调用service方法
        List<SysUser> SysUserList = sysUserService.list();

        return Result.ok(SysUserList);
    }

    //2.逻辑删除记录
    @ApiOperation(value = "逻辑删除id角色")
    @DeleteMapping("remove/{id}")
    public Result removeRole(@PathVariable Long id){
        boolean isSuccess = sysUserService.removeById(id);
        if (isSuccess) return Result.ok();
        return Result.fail();
    }

    //4.添加角色
    @ApiOperation("添加角色")
    @PostMapping("save")
    public Result saveRole(@RequestBody SysUser sysUser){
        //MD5进行加密
        String password = sysUser.getPassword();
        String passwordMD5 = MD5.encrypt(password);
        sysUser.setPassword(passwordMD5);

        boolean isSuccess = sysUserService.save(sysUser);

        return isSuccess?Result.ok():Result.fail();
    }

    //5.根据id查询角色
    @ApiOperation("根据id查询角色")
    @GetMapping("get/{id}")
    public Result findRoleById(@PathVariable Long id){

        SysUser SysUser = sysUserService.getById(id);
        return Result.ok(SysUser);
    }

    //6.修改角色
    @ApiOperation("根据id修改角色")
    @PutMapping("update")
    public Result updateRole(@RequestBody SysUser sysUser){

        boolean isSuccess = sysUserService.updateById(sysUser);
        return isSuccess?Result.ok():Result.fail();
    }

    //7.批量删除(逻辑删除)
    @ApiOperation("批量删除")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> ids){
        boolean isSuccess = sysUserService.removeByIds(ids);

        return isSuccess?Result.ok():Result.fail();
    }

    //根据用户id更改用户状态
    @ApiOperation("更新状态")
    @GetMapping("updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable Long id,@PathVariable Integer status){
        sysUserService.updateStatus(id,status);
        return Result.ok();
    }

}

