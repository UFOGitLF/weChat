package com.jhhl.controller;

import com.alibaba.fastjson.JSONObject;
import com.jhhl.consts.MsgConst;
import com.jhhl.exception.CheckResultException;
import com.jhhl.util.WxUtil;
import org.apache.log4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;

/**
 *
 * Created by 杨康 on 2017/10/1.
 */
@RestController
@RequestMapping("/api/public")
public class WxFrontController  {

    private final Logger log = Logger.getLogger(WxFrontController.class);

    @Resource
    Environment env;

    @RequestMapping(value = "/route")
    public void  getOpenId(HttpServletResponse response, HttpServletRequest request) throws IOException {
        String ua = request.getHeader("user-agent").toLowerCase();
        //只有微信端才可以请求
        if (!(ua.indexOf(MsgConst.FROM_WECHAT) > 0)) {// 是微信浏览器
            log.warn("is not weChat request,please be careful");
            throw new CheckResultException(MsgConst.ILLEGAL_OPERATION);
        }
        HttpSession session = request.getSession();
        String openId = (String) session.getAttribute("openId");
        if (StringUtils.isEmpty(openId)) {
            session.setAttribute("originUri",request.getRequestURL().toString());
            String requestUrl = WxUtil.getAuthUrl();
            response.sendRedirect(requestUrl);
            return;
        }
        /**
         * 获取openId 操作 业务逻辑
         */
        response.sendRedirect("http://juhehuli.com:8081/");
    }

    /**
     * 微信回调接口
     *
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    @RequestMapping("/auth")
    public void auth(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String code = request.getParameter("code");
        log.warn("wx code:" + code);
        if (code != null) {
            String res = WxUtil.getAccessToken(code);
            log.warn("wx res:" + res);
            if (res != null) {
                JSONObject json = (JSONObject) JSONObject.parse(res);
                String openId = json.getString("openid");
                HttpSession session = request.getSession();
                String originUri = (String) session.getAttribute("originUri");
                log.warn("originUri:" + originUri);
                session.setAttribute("openId", openId);
                log.warn("wx open id:" + openId);
                response.sendRedirect(originUri);
            }
        }
    }


}
