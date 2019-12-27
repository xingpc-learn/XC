package com.xuecheng.framework.domain.learning.response;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.ResultCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.framework.domain.learning.response
 * @ClassName: LearningCode
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/20 17:21
 * @Version: 1.0
 */
@ToString
public enum LearningCode implements ResultCode {

    LEARNING_GETMEDIA_ERROR(false,23001,"获取媒资路径错误！"),
    CHOOSECOURSE_USERISNULL(false,23002,"选择课程用户不存在！"),
    CHOOSECOURSE_TASKISNULL(false,23003,"选择课程任务为空！");

    //操作代码
    @ApiModelProperty(value = "操作是否成功", example = "true", required = true)
    boolean success;

    //操作代码
    @ApiModelProperty(value = "操作代码", example = "23001", required = true)
    int code;

    //提示信息
    @ApiModelProperty(value = "操作提示", example = "操作过于频繁！", required = true)
    String message;

    private LearningCode(boolean success, int code, String message){
        this.success = success;
        this.code = code;
        this.message = message;
    }

    private static final ImmutableMap<Integer, LearningCode> CACHE;

    static {
        final ImmutableMap.Builder<Integer, LearningCode> builder = ImmutableMap.builder();
        for (LearningCode learningCode : values()) {
            builder.put(learningCode.code(), learningCode);
        }
        CACHE = builder.build();
    }

    @Override
    public boolean success() {
        return false;
    }

    @Override
    public int code() {
        return 0;
    }

    @Override
    public String message() {
        return null;
    }
}
