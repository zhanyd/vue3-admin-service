package com.zhanyd.app.controller;

import com.github.pagehelper.PageInfo;
import com.zhanyd.app.common.ApiResult;
import com.zhanyd.app.model.Question;
import com.zhanyd.app.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.pagehelper.page.PageMethod.startPage;

@RestController
@EnableAutoConfiguration
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    QuestionService questionService;

    /**
     * 获取问题列表
     * @param pageNum
     * @param pageSize
     * @param question
     * @param answer
     * @return
     */
    @GetMapping("/questions/{pageNum}/{pageSize}")
    public ApiResult<PageInfo<Map<String, Object>>> questions(@PathVariable Integer pageNum, @PathVariable Integer pageSize,
                                                              String question, String answer) {
        ApiResult<PageInfo<Map<String, Object>>> apiResult = new ApiResult<PageInfo<Map<String, Object>>>();
        Map<String, Object> param = new HashMap<>(16);
        param.put("question", question);
        param.put("answer", answer);
        startPage(null == pageNum ? 1 : pageNum, null == pageSize ? 20 : pageSize);
        List<Map<String, Object>> questionList = questionService.selectByParam(param);
        PageInfo<Map<String,Object>> page = new PageInfo<Map<String,Object>>(questionList);
        return apiResult.success(page);
    }

    /**
     * 新增问题
     * @param Question
     * @param request
     * @return
     */
    @PostMapping("/question")
    public ApiResult<String> addQuestion(@RequestBody Question Question, HttpServletRequest request ) {
        questionService.insertSelective(Question,request);
        return new ApiResult<String>().success("新增成功");
    }

    /**
     * 更新问题
     * @param Question
     * @param request
     * @return
     */
    @PutMapping("/question")
    public ApiResult<String> updateQuestion(@RequestBody Question Question, HttpServletRequest request ) {
        questionService.updateByPrimaryKeySelective(Question,request);
        return new ApiResult<String>().success("修改成功");
    }

    /**
     * 删除问题
     * @param id
     * @return
     */
    @DeleteMapping("/question/{id}")
    public ApiResult<String> delQuestion(@PathVariable Integer id) {
        questionService.deleteByPrimaryKey(id);
        return new ApiResult<String>().success("删除成功");
    }

    /**
     * 获取问题详情
     * @param id
     * @return
     */
    @GetMapping("/question/{id}")
    public ApiResult<Question> getQuestion(@PathVariable Integer id) {
        ApiResult<Question> apiResult = new ApiResult();
        Question question = questionService.selectByPrimaryKey(id);
        return apiResult.success(question);
    }

}
