package com.xiio.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiio.model.system.SysMenu;
import com.xiio.model.vo.AssignMenuVo;
import com.xiio.model.vo.RouterVo;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author xiio
 * @since 2023-05-09
 */
public interface SysMenuService extends IService<SysMenu> {

    List<SysMenu> findNodes();

    void removeMenuById(Long id);

    List<SysMenu> findSysMenuByRoleId(Long roleId);

    void doAssign(AssignMenuVo assignMenuVo);

    List<RouterVo> findUserMenuByUserId(Long userId);

    List<String> findUserPermsByUserId(Long userId);
}
