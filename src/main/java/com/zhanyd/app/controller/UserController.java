package com.zhanyd.app.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhanyd.app.common.ApiResult;
import com.zhanyd.app.common.util.JwtUtils;
import com.zhanyd.app.common.util.MD5Generate;
import com.zhanyd.app.common.util.StringHelp;
import com.zhanyd.app.model.UserInfo;
import com.zhanyd.app.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.nio.charset.Charset;
import java.util.*;


/**
 * Created by zhanyd@sina.com on 2018/2/13 0013.
 */
@RestController
@EnableAutoConfiguration
@RequestMapping("/user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserService userService;

    /**
     * 用户登录
     * @param userInfo
     * @return
     */
    @PostMapping("/login")
    public ApiResult<Map<String, Object>> login(@RequestBody UserInfo userInfo) {
        ApiResult<Map<String, Object>> apiResult = new ApiResult<>();
        Map<String, Object> resultMap = new HashMap(16);

        Map<String, Object> param = new HashMap(16);
        param.put("userName", userInfo.getUserName());
        //MD5加密
        param.put("password", MD5Generate.getMD5(userInfo.getPassword().getBytes(Charset.forName("UTF-8"))));
        List<Map<String, Object>> userList = userService.selectByParam(param);

        //判断该用户是否已经存在
        if (!userList.isEmpty()) {
            Map<String, Object> user = userList.get(0);
            //生成token
            String token = JwtUtils.signJWT(StringHelp.parseInt(user.get("id")));
            if (token == null) {
                resultMap.put("msg", "生成token失败");
                return apiResult.fail(resultMap);
            } else {
                logger.info("{} 登录成功", userInfo.getUserName());
                resultMap.put("username", userInfo.getUserName());
                resultMap.put("roles", new String[]{"admin"});
                resultMap.put("accessToken", token);
                resultMap.put("refreshToken", "");
                resultMap.put("expires", "2023/10/30 00:00:00");
                return apiResult.success(resultMap);
            }
        }
        logger.info("{} 用户名或密码错误", userInfo.getUserName());
        return new ApiResult(500, "用户名或密码错误", null);
    }

    /**
     * 返回动态路由
     * @return
     */
    @GetMapping("/getAsyncRoutes")
    public ApiResult<JSONArray> getAsyncRoutes() {
        ApiResult<JSONArray> apiResult = new ApiResult<JSONArray>();

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("path", "/permission");
        JSONObject metaObject = new JSONObject();
        metaObject.put("title", "menus.permission");
        metaObject.put("icon", "lollipop");
        metaObject.put("rank", 10);
        jsonObject.put("meta", metaObject);

        JSONArray childrenArray = new JSONArray();
        JSONObject children = new JSONObject();
        children.put("path", "/permission/page/index");
        children.put("name", "PermissionPage");
        metaObject = new JSONObject();
        metaObject.put("title", "menus.permissionPage");
        metaObject.put("roles", new String[]{"admin", "common"});
        children.put("meta", metaObject);
        childrenArray.add(children);

        children = new JSONObject();
        children.put("path", "/permission/button/index");
        children.put("name", "PermissionButton");
        metaObject = new JSONObject();
        metaObject.put("title", "menus.permissionButton");
        metaObject.put("roles", new String[]{"admin", "common"});
        metaObject.put("auths", new String[]{"btn_add", "btn_edit", "btn_delete"});
        children.put("meta", metaObject);
        childrenArray.add(children);

        jsonObject.put("children", childrenArray);
        jsonArray.add(jsonObject);
        return apiResult.success(jsonArray);
    }
}
