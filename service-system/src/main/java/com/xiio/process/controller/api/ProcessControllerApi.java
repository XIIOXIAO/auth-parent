package com.xiio.process.controller.api;

import com.xiio.common.result.Result;
import com.xiio.model.process.ProcessTemplate;
import com.xiio.model.process.ProcessType;
import com.xiio.process.service.OaProcessTemplateService;
import com.xiio.process.service.OaProcessTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description:
 * @author: xiio
 * @time: 2023/5/28 11:07
 */

@Api(tags = "外部Api接口")
@RestController
@RequestMapping(value = "/admin/process")
@CrossOrigin //跨域
public class ProcessControllerApi {
    @Autowired
    private OaProcessTypeService processTypeService;
    @Autowired
    private OaProcessTemplateService processTemplateService;

    @ApiOperation("查询所有审批类型及其模板")
    @GetMapping("findProcessType")
    public Result findProcessType(){
        List<ProcessType> listType = processTypeService.findProcessType();
        return Result.ok(listType);
    }

    @ApiOperation("获取模板信息")
    @GetMapping("getProcessTemplate/{processTemplateId}")
    public Result get(@PathVariable Long processTemplateId){
        ProcessTemplate processTemplate = processTemplateService.getById(processTemplateId);
        return Result.ok(processTemplate);
    }

}
