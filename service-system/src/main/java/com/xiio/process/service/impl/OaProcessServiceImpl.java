package com.xiio.process.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiio.model.process.Process;
import com.xiio.model.vo.process.ProcessQueryVo;
import com.xiio.model.vo.process.ProcessVo;
import com.xiio.process.mapper.OaProcessMapper;
import com.xiio.process.service.OaProcessService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.zip.ZipInputStream;

/**
 * <p>
 * 审批类型 服务实现类
 * </p>
 *
 * @author xiio
 * @since 2023-05-22
 */
@Service
public class OaProcessServiceImpl extends ServiceImpl<OaProcessMapper, Process> implements OaProcessService {

    @Autowired
    RepositoryService repositoryService;

    //审批管理列表
    @Override
    public IPage<ProcessVo> selectPage(Page<ProcessVo> pageParam, ProcessQueryVo processQueryVo) {
        IPage<ProcessVo> pageModel = baseMapper.selectPage(pageParam,processQueryVo);
        return pageModel;
    }

    @Override
    public void deployByZip(String deployPath) {
        //1.通过流的方式读取到zip输入流
        InputStream resourceStream = this
                .getClass()
                .getClassLoader()
                .getResourceAsStream(deployPath);
        ZipInputStream zipInputStream = new ZipInputStream(resourceStream);


        //2.流程部署
        Deployment deploy = repositoryService.createDeployment()
                .addZipInputStream(zipInputStream)
                .deploy();
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }
}
