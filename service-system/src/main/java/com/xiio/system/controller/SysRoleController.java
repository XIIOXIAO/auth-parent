package com.xiio.system.controller;

import com.xiio.model.system.SysRole;
import com.xiio.model.vo.AssignRoleVo;
import com.xiio.model.vo.SysRoleQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiio.common.result.Result;
import com.xiio.system.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: xiio
 * @time: 2023/5/3 17:29
 */
@Api(tags = "角色管理接口")
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;

    //1.查询所有记录
    @ApiOperation(value = "查询所有角色")
    @GetMapping("findAll")
    public Result findAllRole(){
        //调用service方法
        List<SysRole> sysRoleList = sysRoleService.list();

        return Result.ok(sysRoleList);
    }

    //2.逻辑删除记录
    @ApiOperation(value = "逻辑删除id角色")
    @DeleteMapping("remove/{id}")
    public Result removeRole(@PathVariable Long id){
        boolean isSuccess = sysRoleService.removeById(id);
        if (isSuccess) return Result.ok();
        return Result.fail();
    }

    //3.条件分页查询
    //page 当前页数  limit 每页限制数
    @ApiOperation("条件分页查询")
    @GetMapping("{page}/{limit}")
    public Result findPageQueryRole(@PathVariable Long page,
                                    @PathVariable Long limit,
                                    SysRoleQueryVo sysRoleQueryVo){
        //创建page对象 传递分页参数
        Page<SysRole> pageParam = new Page<>(page,limit);

        //创建wrapper查询对象
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        //获取vo视图类的姓名
        String roleName = sysRoleQueryVo.getRoleName();

        if(!StringUtils.isEmpty(roleName)){
            //wrapper具体查询条件
            wrapper.like(SysRole::getRoleName,roleName);
        }
        //使用page查询
        Page<SysRole> pageModel = sysRoleService.page(pageParam, wrapper);
        return Result.ok(pageModel);
    }

    //4.添加角色
    @ApiOperation("添加角色")
    @PostMapping("save")
    public Result saveRole(@RequestBody SysRole sysRole){
        boolean isSuccess = sysRoleService.save(sysRole);

        return isSuccess?Result.ok():Result.fail();
    }

    //5.根据id查询角色
    @ApiOperation("根据id查询角色")
    @GetMapping("findRoleById/{id}")
    public Result findRoleById(@PathVariable Long id){

        SysRole sysRole = sysRoleService.getById(id);
        return Result.ok(sysRole);
    }

    //6.修改角色
    @ApiOperation("根据id修改角色")
    @PutMapping("update")
    public Result updateRole(@RequestBody SysRole sysRole){

        boolean isSuccess = sysRoleService.updateById(sysRole);
        return isSuccess?Result.ok():Result.fail();
    }

    //7.批量删除(逻辑删除)
    @ApiOperation("批量删除")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> ids){
        boolean isSuccess = sysRoleService.removeByIds(ids);

        return isSuccess?Result.ok():Result.fail();
    }

    //8.查询所有角色 和 当前用户所属角色
    @ApiOperation("根据id获取当前用户角色")
    @GetMapping("toAssign/{userId}")
    public Result toAssign(@PathVariable Long userId){
        Map<String,Object> map = sysRoleService.findRoleDataByUserId(userId);

        return Result.ok(map);
    }

    //9.为用户分配角色
    @ApiOperation("根据id用户角色分配")
    @PostMapping("doAssign")
    public Result doAssign(@RequestBody AssignRoleVo assginRoleVo){
        sysRoleService.doAssign(assginRoleVo);
        return Result.ok();
    }

}
