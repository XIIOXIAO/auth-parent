package com.xiio.system.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: xiio
 * @time: 2023/5/4 11:39
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyselfException extends RuntimeException{
    private Integer code;
    private String msg;
}
