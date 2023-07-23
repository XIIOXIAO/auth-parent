package com.xiio.system.service;

import com.xiio.model.system.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author xiio
 * @since 2023-05-08
 */
public interface SysUserService extends IService<SysUser> {

    void updateStatus(Long id, Integer status);

    SysUser findUserByUsername(String username);
}
