package com.xiio.process.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiio.common.result.Result;
import com.xiio.model.process.Process;
import com.xiio.model.vo.process.ProcessQueryVo;
import com.xiio.model.vo.process.ProcessVo;
import com.xiio.process.service.OaProcessService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 审批类型 前端控制器
 * </p>
 *
 * @author xiio
 * @since 2023-05-22
 */
@Api(tags = "审批管理接口")
@RestController
@RequestMapping("admin/process/oa-process")
public class ProcessController {

    @Autowired
    private OaProcessService processService;

    //1.审批列表方法
    @ApiOperation(value = "获取分页列表")
    @GetMapping("{page}/{limit}")
    public Result index(@PathVariable Long page,
                        @PathVariable Long limit,
                        ProcessQueryVo processQueryVo){
        Page<ProcessVo> pageParam = new Page<>(page, limit);
        IPage<ProcessVo> pagemodel = processService.selectPage(pageParam,processQueryVo);

        return Result.ok(pagemodel);
    }
}

