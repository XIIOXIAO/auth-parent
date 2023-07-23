package com.xiio.system.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiio.model.system.SysMenu;
import com.xiio.model.system.SysRole;
import com.xiio.model.system.SysRoleMenu;
import com.xiio.model.vo.AssignMenuVo;
import com.xiio.model.vo.MetaVo;
import com.xiio.model.vo.RouterVo;
import com.xiio.system.exception.GlobalExceptionHandler;
import com.xiio.system.exception.MyselfException;
import com.xiio.system.mapper.SysMenuMapper;
import com.xiio.system.service.SysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiio.system.service.SysRoleMenuService;
import com.xiio.system.utils.MenuHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListResourceBundle;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author xiio
 * @since 2023-05-09
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {
    @Autowired
    private SysRoleMenuService sysRoleMenuService;
    //查询菜单列表结构
    @Override
    public List<SysMenu> findNodes() {
        //1.查询所有菜单数据
        List<SysMenu> sysMenuList = baseMapper.selectList(null);
        //2.构建属性结构
        List<SysMenu> resultList = MenuHelper.buildTree(sysMenuList);

        return resultList;
    }

    //删除菜单
    @Override
    public void removeMenuById(Long id) {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getParentId,id);

        Integer count = baseMapper.selectCount(wrapper);
        if (count>0){
            throw new MyselfException(201,"菜单不能删除");
        }
        baseMapper.deleteById(id);
    }

    @Override
    public List<SysMenu> findSysMenuByRoleId(Long roleId) {
        //1.查询所有菜单--status=1
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getStatus,1);
        List<SysMenu> allSysMenuList = baseMapper.selectList(wrapper);

        //2.根据角色id，roleid查询
        LambdaQueryWrapper<SysRoleMenu> wrapperSysRoleMenu = new LambdaQueryWrapper<>();
        wrapperSysRoleMenu.eq(SysRoleMenu::getRoleId,roleId);
        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuService.list(wrapperSysRoleMenu);

        //3.根据获取菜单id 获取对应菜单对象
        List<String> menuIdList = sysRoleMenuList.stream().map(c -> c.getMenuId()).collect(Collectors.toList());

        allSysMenuList.stream().forEach(item ->{
            if (menuIdList.contains(item.getId())){
                item.setSelect(true);
            }else {
                item.setSelect(false);
            }
        });

        //4.返回树形结构显示菜单列表
        List<SysMenu> sysMenus = MenuHelper.buildTree(allSysMenuList);

        return sysMenus;
    }

    @Override
    public void doAssign(AssignMenuVo assignMenuVo) {
        //1.根据角色id 删除菜单角色表 分配数据
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleMenu::getRoleId,assignMenuVo.getRoleId());
        sysRoleMenuService.remove(wrapper);
        //2.从参数里面获取角色新分配菜单id列表  每个id数据添加菜单角色表
        List<String> menuIdList = assignMenuVo.getMenuIdList();
        for (String menuId : menuIdList) {
            if (StringUtils.isEmpty(menuId)){
                continue;
            }
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setMenuId(menuId);
            sysRoleMenu.setRoleId(assignMenuVo.getRoleId());

            sysRoleMenuService.save(sysRoleMenu);
        }
    }

    @Override
    public List<RouterVo> findUserMenuByUserId(Long userId) {
        List<SysMenu> sysMenuList = null;
        //1.判断当前用户是否是管理员 USERID=1是管理员
        if(userId.longValue()==1){
            //1.1是，查询所有菜单列表
            LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysMenu::getStatus,1);
            wrapper.orderByAsc(SysMenu::getSortValue);
            sysMenuList = baseMapper.selectList(wrapper);
        }else {
            //1.2不是，根据userid查询可以操作的菜单列表
            //多表联查 用户角色关系表、角色菜单表、菜单表
            sysMenuList = baseMapper.findMenuListByUserId(userId);
        }
        //2.把查询出来的数据列表构建成--框架要求的路由数据结构
        List<SysMenu> sysMenuTreeList = MenuHelper.buildTree(sysMenuList);
        //构建成框架要求的路由结构
        List<RouterVo> routerList = this.buildRouter(sysMenuTreeList);

        return routerList;
    }

    @Override
    public List<String> findUserPermsByUserId(Long userId) {
        List<SysMenu> sysMenuList = null;
        if (userId.longValue()==1){
            //1.1是，查询所有菜单列表
            LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysMenu::getStatus,1);
            sysMenuList = baseMapper.selectList(wrapper);
        }else{
           sysMenuList = baseMapper.findMenuListByUserId(userId);
        }
        List<String> permsList = sysMenuList.stream()
                .filter(item -> item.getType() == 2)
                .map(item -> item.getPerms())
                .collect(Collectors.toList());
        return permsList;
    }
    //前端路由框架文件构建
    private List<RouterVo> buildRouter(List<SysMenu> menus) {
        List<RouterVo> routers = new LinkedList<RouterVo>();
        for (SysMenu menu : menus) {
            RouterVo router = new RouterVo();
            router.setHidden(false);
            router.setAlwaysShow(false);
            router.setPath(menu.getPath());
            router.setComponent(menu.getComponent());
            router.setMeta(new MetaVo(menu.getName(), menu.getIcon()));
            List<SysMenu> children = menu.getChildren();
            //如果当前是菜单，需将按钮对应的路由加载出来，如：“角色授权”按钮对应的路由在“系统管理”下面
            if(menu.getType().intValue() == 1) {
                List<SysMenu> hiddenMenuList = children.stream().filter(item -> !StringUtils.isEmpty(item.getComponent())).collect(Collectors.toList());
                for (SysMenu hiddenMenu : hiddenMenuList) {
                    RouterVo hiddenRouter = new RouterVo();
                    hiddenRouter.setHidden(true);
                    hiddenRouter.setAlwaysShow(false);
                    hiddenRouter.setPath(hiddenMenu.getPath());
                    hiddenRouter.setComponent(hiddenMenu.getComponent());
                    hiddenRouter.setMeta(new MetaVo(hiddenMenu.getName(), hiddenMenu.getIcon()));
                    routers.add(hiddenRouter);
                }
            } else {
                if (!CollectionUtils.isEmpty(children)) {
                    if(children.size() > 0) {
                        router.setAlwaysShow(true);
                    }
                    router.setChildren(buildRouter(children));
                }
            }
            routers.add(router);
        }
        return routers;
    }
}
