package com.example.common;

/**
 * @Description:
 * @author: LinQin
 * @date: 2020/01/08
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Created by jicexosl on 2016/11/22.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
// @JSONType(alphabetic = false)
public class ResultModel implements Serializable {
    private int msgCode;
    private String message;
    private Object data = null;
    private String path = null;

}
