package com.xuecheng.framework.domain.course.ext;

import com.xuecheng.framework.domain.course.Teachplan;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * Created by admin on 2018/2/7.
 * 此类为教学计划扩展类，包含教学计划所有信息以及children参数
 */
@Data
@ToString
public class TeachplanNode extends Teachplan {

    List<TeachplanNode> children;

    //媒资信息
    private String mediaId;
    private String mediaFileOriginalName;

}
