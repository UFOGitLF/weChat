package com.jhhl.controller;


import com.jhhl.base.model.WXMessage;
import com.jhhl.util.DecriptUtil;
import com.jhhl.util.WxService;
import com.jhhl.util.WxUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;

/**
 *
 * Created by yangkang on 01/10/17.
 */

@Controller
@RequestMapping("/weChat")
public class WeChatController {

    private final Logger log = Logger.getLogger(WeChatController.class);

    private final static String TOKEN = "szyz123456";

    @ResponseBody
    @RequestMapping("/check")
    public void check(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.warn(request);
        // 微信加密签名
        String signature = request.getParameter("signature");
        // 随机字符串
        String echostr = request.getParameter("echostr");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        String[] str = {TOKEN, timestamp, nonce};
        Arrays.sort(str); // 字典序排序
        String bigStr = str[0] + str[1] + str[2];
        // SHA1加密
        String digest = DecriptUtil.SHA1(bigStr).toLowerCase();
        // 确认请求来至微信
        if (digest.equals(signature)) {
            StringBuilder sb = new StringBuilder();
            InputStream is = request.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String s ;
            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
            String xml = sb.toString();
            String result;
            if (echostr != null && echostr.length() > 1) {
                result = echostr;
                response.getWriter().print(result);
            } else {
                WXMessage wxMessage = new WXMessage();
                Document document = DocumentHelper.parseText(xml);
                Map<String, Object> map = WxUtil.Dom2Map(document);
                String fromUserName = (String) map.get("FromUserName");
                String toUserName = (String) map.get("ToUserName");
                String msgType = (String) map.get("MsgType");
                wxMessage.setToUserName(fromUserName);
                wxMessage.setFromUserName(toUserName);
                wxMessage.setCreateTime(new Date().getTime());
                wxMessage.setMsgType("text");
                wxMessage.setFuncFlag(0);
                //处理事件
                String eventType = (String) map.get("Event");
                if (msgType.equals(WxUtil.REQ_MESSAGE_TYPE_EVENT)) {
                    if (eventType.equals(WxUtil.EVENT_TYPE_SUBSCRIBE)) {
                        WxService.sendImgMsg(fromUserName);
                        response.getWriter().print(WxUtil.makeXML(wxMessage));
                    } else if (eventType.equals(WxUtil.EVENT_TYPE_UNSUBSCRIBE)) {
                        response.getWriter().print(wxMessage);
                    }
                }
            }
        }
    }

}

