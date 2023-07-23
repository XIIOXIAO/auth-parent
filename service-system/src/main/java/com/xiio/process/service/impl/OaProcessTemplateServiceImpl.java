package com.xiio.process.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiio.model.process.ProcessTemplate;
import com.xiio.model.process.ProcessType;
import com.xiio.process.mapper.OaProcessTemplateMapper;
import com.xiio.process.mapper.OaProcessTypeMapper;
import com.xiio.process.service.OaProcessService;
import com.xiio.process.service.OaProcessTemplateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.xiio.process.service.OaProcessTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Wrapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 审批模板 服务实现类
 * </p>
 *
 * @author xiio
 * @since 2023-05-22
 */
@Service
public class OaProcessTemplateServiceImpl extends ServiceImpl<OaProcessTemplateMapper, ProcessTemplate> implements OaProcessTemplateService {

    @Autowired
    private OaProcessTypeService oaProcessTypeService;

    @Autowired
    private OaProcessService oaProcessService;

    @Override
    public IPage<ProcessTemplate> queryTemplate(Page<ProcessTemplate> page) {
        //1.根据mapper方法实现分页查询
        Page<ProcessTemplate> templatePage = baseMapper.selectPage(page,null);

        //2.分页数据转换Wie列表list集合
        List<ProcessTemplate> records = templatePage.getRecords();

        //3.获取分页列表数据中出现的id集合 stream流操作
        List<Long> idsCollect = records.stream().map(ProcessTemplate::getProcessTypeId).distinct().collect(Collectors.toList());

        //4.创建<id,typeName>map
        Map<Long,String> resultMap = new HashMap<>();

        //4.遍历ids列表 查询processType表中 对应type名称
        for (Long id : idsCollect) {
            LambdaQueryWrapper<ProcessType> wrapper = new LambdaQueryWrapper();
            wrapper.eq(ProcessType::getId,id);
            ProcessType processType = oaProcessTypeService.getOne(wrapper);

            if (processType!= null){
                String typeName = processType.getName();
                resultMap.put(id,typeName);
            }else{
                continue;
            }

        }
        System.out.println(resultMap);
        //5.遍历分页数据 添加类型名称
        for (ProcessTemplate record : records) {
            record.setProcessTypeName(resultMap.get(record.getProcessTypeId()));
        }
        System.out.println(records);
        //6.返回page对象

        return templatePage;
    }

    @Override
    public void publish(Long id) {
        //1.设置流程模板定义状态值(1已发布 0未发布)
        ProcessTemplate processTemplate = baseMapper.selectById(id);
        processTemplate.setStatus(1);
        baseMapper.updateById(processTemplate);

        //TODO 2.根据上传的zip文件 创建流程模板定义
        if (!StringUtils.isEmpty(processTemplate.getProcessDefinitionPath())){
            oaProcessService.deployByZip(processTemplate.getProcessDefinitionPath());
        }

    }
}
