package com.xiio.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiio.model.system.SysUser;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiio.system.mapper.SysUserMapper;

import com.xiio.system.service.SysUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author xiio
 * @since 2023-05-08
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Override
    public void updateStatus(Long id, Integer status) {
        //根据userid查询用户对象
        SysUser sysUser = baseMapper.selectById(id);
        //设置修改状态
        sysUser.setStatus(status);
        //调用方法进行修改
        baseMapper.updateById(sysUser);
    }

    //根据用户名进行查询
    @Override
    public SysUser findUserByUsername(String username) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername,username);

        SysUser sysUser = baseMapper.selectOne(wrapper);

        return sysUser;
    }
}
