package com.zhanyd.app.mapper;

import com.zhanyd.app.model.Question;

import java.util.List;
import java.util.Map;

public interface QuestionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Question record);

    int insertSelective(Question record);

    Question selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Question record);

    int updateByPrimaryKey(Question record);

    /**
     * 查找列表
     * @param param
     * @return
     */
    List<Map<String, Object>> selectByParam(Map<String, Object> param);
}