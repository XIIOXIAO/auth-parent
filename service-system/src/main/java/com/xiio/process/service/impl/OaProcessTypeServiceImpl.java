package com.xiio.process.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiio.model.process.ProcessTemplate;
import com.xiio.model.process.ProcessType;
import com.xiio.process.mapper.OaProcessTypeMapper;
import com.xiio.process.service.OaProcessTemplateService;
import com.xiio.process.service.OaProcessTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 审批类型 服务实现类
 * </p>
 *
 * @author xiio
 * @since 2023-05-22
 */
@Service
public class OaProcessTypeServiceImpl extends ServiceImpl<OaProcessTypeMapper, ProcessType> implements OaProcessTypeService {

    @Autowired
    private OaProcessTemplateService processTemplateService;

    @Override
    public IPage<ProcessTemplate> queryTemplate() {
        return null;
    }

    @Override
    public List<ProcessType> findProcessType() {
        //1.查询所有审批分配，返回list集合
        List<ProcessType> processTypes = baseMapper.selectList(null);

        //2.遍历所有审批分类的集合
        for (ProcessType processType : processTypes) {
            //3.得到每个审批分类 根据分类id 查询对应审批模板
                //3.1审批分类id
            Long id = processType.getId();
                //3.2根据审批分类id查询对应审批模板
            LambdaQueryWrapper<ProcessTemplate>  wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ProcessTemplate::getProcessTypeId,id);
            List<ProcessTemplate> processTemplateList = processTemplateService.list(wrapper);

            //4.根据审批分类id查询对应审批模板数据 封装到每个审批分类对象中
            processType.setProcessTemplateList(processTemplateList);
        }

        return processTypes;
    }
}
