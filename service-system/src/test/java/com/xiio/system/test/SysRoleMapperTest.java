package com.xiio.system.test;

import com.xiio.model.system.SysRole;
import com.xiio.system.mapper.SysRoleMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @description:
 * @author: xiio
 * @time: 2023/5/3 16:41
 */

@SpringBootTest
public class SysRoleMapperTest {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Test
    public void findAll(){
        List<SysRole> sysRoles = sysRoleMapper.selectList(null);
        for (SysRole sysRole : sysRoles) {
            System.out.println(sysRole);
        }
    }
}
