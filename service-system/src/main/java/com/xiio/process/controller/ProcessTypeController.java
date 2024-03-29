package com.xiio.process.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.org.apache.regexp.internal.RE;
import com.xiio.common.result.Result;
import com.xiio.model.process.ProcessType;
import com.xiio.process.service.OaProcessTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 审批类型 前端控制器
 * </p>
 *
 * @author xiio
 * @since 2023-05-22
 */
@Api(value = "审批类型", tags = "审批类型")
@RestController
@RequestMapping("/admin/process/processType")
public class ProcessTypeController {
    @Autowired
    private OaProcessTypeService oaProcessTypeService;


    @ApiOperation("获取全部流程类型")
    @GetMapping("findAll")
    public Result findAll(){
        List<ProcessType> list = oaProcessTypeService.list();

        return Result.ok(list);
    }


    @ApiOperation("获取分页列表")
    @GetMapping("{page}/{limit}")
    public Result index(@PathVariable Long page,
                        @PathVariable Long limit){
        Page<ProcessType> pageParam = new Page<>(page, limit);
        IPage<ProcessType> pageModel = oaProcessTypeService.page(pageParam);

        return Result.ok(pageModel);
    }

    // @PreAuthorize("hasAuthority('bnt.processType.list')")
    @ApiOperation(value = "获取")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        ProcessType processType = oaProcessTypeService.getById(id);
        return Result.ok(processType);
    }
    //
    // @PreAuthorize("hasAuthority('bnt.processType.add')")
    @ApiOperation(value = "新增")
    @PostMapping("save")
    public Result save(@RequestBody ProcessType processType) {
        oaProcessTypeService.save(processType);
        return Result.ok();
    }

    // @PreAuthorize("hasAuthority('bnt.processType.update')")
    @ApiOperation(value = "修改")
    @PutMapping("update")
    public Result updateById(@RequestBody ProcessType processType) {
        oaProcessTypeService.updateById(processType);
        return Result.ok();
    }

    // @PreAuthorize("hasAuthority('bnt.processType.remove')")
    @ApiOperation(value = "删除")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        oaProcessTypeService.removeById(id);
        return Result.ok();
    }
}

