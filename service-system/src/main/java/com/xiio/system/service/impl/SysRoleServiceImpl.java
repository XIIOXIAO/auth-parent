package com.xiio.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiio.model.system.SysRole;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiio.model.system.SysUserRole;
import com.xiio.model.vo.AssignRoleVo;
import com.xiio.system.mapper.SysRoleMapper;
import com.xiio.system.service.SysRoleService;
import com.xiio.system.service.SysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: xiio
 * @time: 2023/5/3 17:14
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Override
    public Map<String, Object> findRoleDataByUserId(Long userId) {
        List<SysRole> allRoleList = baseMapper.selectList(null);

        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId,userId);

        List<SysUserRole> existUserRoleList = sysUserRoleService.list(wrapper);

        List<String> existUserRoleIdList = existUserRoleList.stream().map(c -> c.getRoleId()).collect(Collectors.toList());

        List<SysRole> assignRoleList = new ArrayList<>();
        for (SysRole sysRole:allRoleList){
            if (existUserRoleIdList.contains(sysRole.getId())){
                assignRoleList.add(sysRole);
            }
        }
        HashMap<String, Object> rolemap = new HashMap<>();
        rolemap.put("assginRoleList",assignRoleList);
        rolemap.put("allRolesList",allRoleList);
        return rolemap;
    }

    @Override
    public void doAssign(AssignRoleVo assginRoleVo) {
        //删除用户之前分配的角色数据
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId,assginRoleVo.getUserId());
        sysUserRoleService.remove(wrapper);
        //重新分配
        List<String> roleIdList = assginRoleVo.getRoleIdList();
        for (String roleId : roleIdList) {
            if (StringUtils.isEmpty(roleId)){
                continue;
            }
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(assginRoleVo.getUserId());
            sysUserRole.setRoleId(roleId);

            sysUserRoleService.save(sysUserRole);
        }
    }
}
