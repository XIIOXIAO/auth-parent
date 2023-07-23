package com.xiio.system.controller;


import com.xiio.common.result.Result;
import com.xiio.model.system.SysMenu;
import com.xiio.system.service.SysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.xiio.model.vo.AssignMenuVo;

import java.util.List;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author xiio
 * @since 2023-05-09
 */
@Api(tags = "菜单管理接口")
@RestController
@RequestMapping("/admin/system/sysMenu")
public class SysMenuController {
    @Autowired
    private SysMenuService sysMenuService;

    @ApiOperation("新增菜单")
    @PostMapping("save")
    public Result save(@RequestBody SysMenu sysMenu){
        sysMenuService.save(sysMenu);
        return Result.ok();
    }

    @ApiOperation("修改菜单")
    @PutMapping("update")
    public Result updateById(@RequestBody SysMenu sysMenu){
        sysMenuService.updateById(sysMenu);
        return Result.ok();
    }

    @ApiOperation("删除菜单")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id){
        sysMenuService.removeMenuById(id);
        return Result.ok();
    }

    //---------------菜单列表接口-----------------------
    @ApiOperation("菜单列表")
    @GetMapping("findNodes")
    public Result findNodes(){
        List<SysMenu> Nodelist = sysMenuService.findNodes();
        return Result.ok(Nodelist);
    }

    @ApiOperation(value = "根据角色获取菜单")
    @GetMapping("toAssign/{roleId}")
    public Result toAssign(@PathVariable Long roleId) {
        List<SysMenu> list = sysMenuService.findSysMenuByRoleId(roleId);
        return Result.ok(list);
    }

    @ApiOperation(value = "给角色分配权限")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssignMenuVo assignMenuVo) {
        sysMenuService.doAssign(assignMenuVo);
        return Result.ok();
    }
}

