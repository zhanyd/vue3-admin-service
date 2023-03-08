package com.zhanyd.app.service;

import com.zhanyd.app.common.util.JwtUtils;
import com.zhanyd.app.mapper.QuestionMapper;
import com.zhanyd.app.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class QuestionService {

    @Autowired
    QuestionMapper questionMapper;

    public int insertSelective(Question record, HttpServletRequest request) {
        record.setCreateBy(JwtUtils.getUserId(request));
        record.setCreateTime(new Date());
        return questionMapper.insertSelective(record);
    }

    public Question selectByPrimaryKey(Integer id) {
        return questionMapper.selectByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(Question record, HttpServletRequest request) {
        record.setUpdateBy(JwtUtils.getUserId(request));
        record.setUpdateTime(new Date());
        return questionMapper.updateByPrimaryKeySelective(record);
    }

    public int deleteByPrimaryKey(Integer id) {
        return questionMapper.deleteByPrimaryKey(id);
    }

    public List<Map<String, Object>> selectByParam(Map<String, Object> param) {
        return questionMapper.selectByParam(param);
    }


}
