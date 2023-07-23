package com.xiio.process.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiio.model.process.Process;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiio.model.vo.process.ProcessQueryVo;
import com.xiio.model.vo.process.ProcessVo;

/**
 * <p>
 * 审批类型 服务类
 * </p>
 *
 * @author xiio
 * @since 2023-05-22
 */
public interface OaProcessService extends IService<Process> {
    //审批管理列表
    IPage<ProcessVo> selectPage(Page<ProcessVo> pageParam, ProcessQueryVo processQueryVo);

    //部署流程定义
    void deployByZip(String deployPath);
}
