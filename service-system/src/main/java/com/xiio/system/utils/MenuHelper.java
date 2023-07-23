package com.xiio.system.utils;

import com.xiio.model.system.SysMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: xiio
 * @time: 2023/5/9 9:53
 */
public class MenuHelper {
    //使用递归方式创建菜单
    public static List<SysMenu> buildTree(List<SysMenu> sysMenuList) {
         //创建list集合，用于最中数据
        List<SysMenu> trees = new ArrayList<>();
        //把所有菜单数据进行遍历
        for (SysMenu sysMenu: sysMenuList){
            //递归入口进入
            //parentId==0为入口
            if(sysMenu.getParentId().longValue()==0){
                trees.add(getChildren(sysMenu,sysMenuList));
            }
        }
        return trees;
    }

    public static SysMenu getChildren(SysMenu sysMenu,List<SysMenu> sysMenuList){
        sysMenu.setChildren(new ArrayList<SysMenu>());
        //遍历所有菜单数据
        for (SysMenu it:sysMenuList){
            if (Long.valueOf(sysMenu.getId())==it.getParentId().longValue()){
                sysMenu.getChildren().add(getChildren(it,sysMenuList));
            }
        }
        return sysMenu;
    }
}
