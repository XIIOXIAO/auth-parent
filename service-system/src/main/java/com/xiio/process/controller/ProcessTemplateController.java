package com.xiio.process.controller;


import cn.hutool.core.io.resource.ResourceUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiio.common.result.Result;
import com.xiio.model.process.ProcessTemplate;
import com.xiio.process.service.OaProcessTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 审批模板 前端控制器
 * </p>
 *
 * @author xiio
 * @since 2023-05-22
 */
@Api(tags = "审批模板接口")
@RestController
@RequestMapping("/admin/process/oa-processTemplate")
public class ProcessTemplateController {
    @Autowired
    private OaProcessTemplateService processTemplateService;

    /*
     * 发布流程模板定义
     */
    // @PreAuthorize("hasAuthority('bnt.processTemplate.publish')")
    @ApiOperation(value = "发布")
    @GetMapping("/publish/{id}")
    public Result publish(@PathVariable Long id) {
        processTemplateService.publish(id);

        return Result.ok();
    }


    /*
     * 上传流程定义
     */
    @ApiOperation("上传流程定义")
    @PostMapping("/uploadProcessDefinition")
    public Result uploadProcessDefinition(MultipartFile file) throws FileNotFoundException {
        //1.获取程序编译后的class目录位置
        String classPath = new File(ResourceUtils.getURL("classpath:")
                                                    .getPath())
                                                    .getAbsolutePath();
        //2.设置上传文件夹
        File processFile = new File(classPath + "/process/");
        if (!processFile.exists()){
            processFile.mkdirs();
        }

        //3.设置上传文件  使用空文件
        String fileName = file.getOriginalFilename();
        File zipFile = new File(classPath + "/process/" + fileName);

        //4.将上传文件写入空文件
        try {
            file.transferTo(zipFile);
        } catch (IOException e) {
            return Result.fail();
        }

        Map<String, Object> map = new HashMap<>();
        //根据上传地址后续部署流程定义，文件名称为流程定义的默认key
        map.put("processDefinitionPath", "processes/" + fileName);
        map.put("processDefinitionKey", fileName.substring(0, fileName.lastIndexOf(".")));

        return Result.ok(map);
    }

    @ApiOperation("分页查询审批模板")
    @GetMapping("pageTemplate/{page}/{limit}")
    public Result getPageTemplate(@PathVariable Long page,
                                @PathVariable Long limit){
        Page<ProcessTemplate> templatePag = new Page<>(page,limit);

        IPage<ProcessTemplate> templateIPag = processTemplateService.queryTemplate(templatePag);

        return Result.ok(templateIPag);
    }

    //@PreAuthorize("hasAuthority('bnt.processTemplate.list')")
    @ApiOperation(value = "获取")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        ProcessTemplate processTemplate = processTemplateService.getById(id);
        return Result.ok(processTemplate);
    }

    //@PreAuthorize("hasAuthority('bnt.processTemplate.templateSet')")
    @ApiOperation(value = "新增")
    @PostMapping("save")
    public Result save(@RequestBody ProcessTemplate processTemplate) {
        processTemplateService.save(processTemplate);
        return Result.ok();
    }

    //@PreAuthorize("hasAuthority('bnt.processTemplate.templateSet')")
    @ApiOperation(value = "修改")
    @PutMapping("update")
    public Result updateById(@RequestBody ProcessTemplate processTemplate) {
        processTemplateService.updateById(processTemplate);
        return Result.ok();
    }

    //@PreAuthorize("hasAuthority('bnt.processTemplate.remove')")
    @ApiOperation(value = "删除")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        processTemplateService.removeById(id);
        return Result.ok();
    }

    public static void main(String[] args) {
        String absolutePath = null;
        try {
            absolutePath = new File(ResourceUtils.getURL("classpath:")
                    .getPath())
                    .getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println(absolutePath);
    }
}

