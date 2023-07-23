package com.xiio.process.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiio.model.process.Process;
import com.xiio.model.vo.process.ProcessQueryVo;
import com.xiio.model.vo.process.ProcessVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 审批类型 Mapper 接口
 * </p>
 *
 * @author xiio
 * @since 2023-05-22
 */
public interface OaProcessMapper extends BaseMapper<Process> {
   IPage<ProcessVo> selectPage(Page<ProcessVo> pageParam,@Param("vo") ProcessQueryVo processQueryVo);
}
