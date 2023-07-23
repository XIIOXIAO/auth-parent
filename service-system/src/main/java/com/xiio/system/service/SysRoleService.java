package com.xiio.system.service;

import com.xiio.model.system.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiio.model.vo.AssignRoleVo;

import java.util.Map;

public interface SysRoleService extends IService<SysRole> {
    //查询当前用户所有角色
    Map<String, Object> findRoleDataByUserId(Long userId);
    //修改当前用户所有角色
    void doAssign(AssignRoleVo assginRoleVo);
}
