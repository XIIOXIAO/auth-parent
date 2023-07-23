package com.xiio.process.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiio.model.process.ProcessTemplate;
import com.xiio.model.process.ProcessType;

import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 审批类型 服务类
 * </p>
 *
 * @author xiio
 * @since 2023-05-22
 */
public interface OaProcessTypeService extends IService<ProcessType> {

    IPage<ProcessTemplate> queryTemplate();

    List<ProcessType> findProcessType();
}
