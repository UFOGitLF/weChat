package com.jhhl.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jhhl.config.WeChatConfig;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;

/**
 * 微信功能分装service
 * Created by yangkang on 2016/7/16.
 */
@SuppressWarnings("ALL")
@Service
public class WxService {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(WxService.class);

    //发送消息接口
    private static String sendMsgUrl = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=";

    //根据分组进行群发接口
    private static final String sendByGroupUrl = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=";

    //根据列表id群发接口
    private static final String sendMsgBylistsId = "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=";
    
    //公众号创建分组接口
    private static final String createGroupUrl = "https://api.weixin.qq.com/cgi-bin/groups/create?access_token=";

    //公众号查询所有分组接口
    private static final String selectGroupsUrl = "https://api.weixin.qq.com/cgi-bin/groups/get?access_token=";

    //查询用户所在分组
    private static final String selectUserUrl = "https://api.weixin.qq.com/cgi-bin/groups/getid?access_token=";

    private static final String templateMsgUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    private static final String createMenuUrl = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=";


    public static void main(String[] args) throws IOException {
        sendMessage("o3IE_xNkhawxkayvU23SAER5JnsU", "fuck u");
        createMenu();
    }

    public static void sendImgMsg(String openid) {
        String str = "{\"articles\":[{\"title\":\"聚合互利平台介绍\",\"description\":\"\",\"url\":\"http://api.fjdjia.com/wechat/seller/aboutUs\",\"picurl\":\"http://szyz.oss-cn-qingdao.aliyuncs.com/img_MTQ4MDA3MDIxMzM0Mg==.png\"},{\"title\":\"商家入驻流程\",\"description\":\"师傅认证后大量接单\",\"url\":\"http://api.fjdjia.com/service/seller/seller\",\"picurl\":\"http://szyz.oss-cn-qingdao.aliyuncs.com/img_MTQ4MDA3MDQwODM2OQ==.png\"},{\"title\":\"师傅注册流程\",\"description\":\"师傅认证后大量接单\",\"url\":\"http://api.fjdjia.com/service/worker/worker\",\"picurl\":\"http://szyz.oss-cn-qingdao.aliyuncs.com/img_MTQ4MDA3MDUyMDM5OA==.png\"}]}";
        JSONArray articlesCont = new JSONArray();
        JSONObject firstContent = new JSONObject();
        firstContent.put("title", "聚合互利平台介绍");
        firstContent.put("description", "");
        firstContent.put("url", "http://api.fjdjia.com");
        firstContent.put("picurl", "http://szyz.oss-cn-qingdao.aliyuncs.com/img_MTQ3MzE0NjY2MDMwMA==.png");
        articlesCont.add(firstContent);
        JSONObject articlesJson = new JSONObject();
        JSONObject obj = (JSONObject) JSON.parse(str);
        articlesJson.put("articles", obj);
        JSONObject sendJson = new JSONObject();
        sendJson.put("touser", openid);
        sendJson.put("msgtype", "news");
        sendJson.put("news", obj);
        try {
            String res = WxUtil.postUrl(sendMsgUrl + getToken(), sendJson.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void sendTemplateMsg(JSONObject jsonObject) {
        try {
            String res = WxUtil.postUrl(templateMsgUrl + getToken(), jsonObject.toJSONString());
            System.out.println(res);
            log.warn(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int createMenu() throws IOException {
        String user_define_menu = "{\"button\":" +
                "[{\"name\":\"互利购物\",\"" +
                "sub_button\":[" +
                "{\"type\":\"view\",\"name\":\"百分百返利\"," +
                "\"url\":\"http://yktest.tunnel.qydev.com/api/public/route\"}," +
                "{\"type\":\"view\",\"name\":\"百分比商城\"," +
                "\"url\":\"http://www.baidu.com\"}" +
                "]}," +
                "{\"name\":\"互利积分\",\"sub_button\":[" +
                "{\"type\":\"view\",\"name\":\"保险入口\",\"url\":\"http://www.baidu.com\"}," +
                "{\"type\":\"view\",\"name\":\"线下服务\",\"url\":\"http://www.baidu.com\"}," +
                "{\"type\":\"view\",\"name\":\"互利游戏\",\"url\":\"http://www.baidu.com\"}]}]}";
        try {
            URL url = new URL(createMenuUrl + getToken());
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");//连接超时30秒
            System.setProperty("sun.net.client.defaultReadTimeout", "30000"); //读取超时30秒
            http.connect();
            OutputStream os = http.getOutputStream();
            os.write(user_define_menu.getBytes("UTF-8"));//传入参数
            os.flush();
            os.close();
            InputStream is = http.getInputStream();
            int size = is.available();
            byte[] jsonBytes = new byte[size];
            is.read(jsonBytes);
            String message = new String(jsonBytes, "UTF-8");
            System.out.println(message);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void sendMsgBylistsId(String[] arr, String content) {
        JSONObject textJson = new JSONObject();
        textJson.put("content", content);
        JSONObject sendJson = new JSONObject();
        sendJson.put("touser", arr);
        sendJson.put("msgtype", "text");
        sendJson.put("text", textJson);
        System.out.println(sendJson.toJSONString());
        try {
            String res = WxUtil.postUrl(sendMsgBylistsId + getToken(), sendJson.toJSONString());
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void sendMsgByGroup(int groupId, String content) {
        JSONObject filterJson = new JSONObject();
        boolean bool = false;
        filterJson.put("is_to_all", bool);
        filterJson.put("group_id", groupId);
        JSONObject textJson = new JSONObject();
        textJson.put("content", content);
        JSONObject sendAllJson = new JSONObject();
        sendAllJson.put("filter", filterJson);
        sendAllJson.put("text", textJson);
        sendAllJson.put("msgtype", "text");
        try {
            String res = WxUtil.postUrl(sendByGroupUrl + getToken(), sendAllJson.toJSONString());
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void selectUser(String userOpenId) {
        JSONObject userJson = new JSONObject();
        userJson.put("openid", userOpenId);
        try {
            String res = WxUtil.postUrl(selectUserUrl + getToken(), userJson.toJSONString());
            JSONObject backResult = JSONObject.parseObject(res);
            System.out.println(backResult.getString("groupid"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void selectGroups() {
        try {
            String res = WxUtil.getUrl(selectGroupsUrl + getToken());
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createGroup() {
        JSONObject groupName = new JSONObject();
        groupName.put("name", "worker");
        JSONObject createGroup = new JSONObject();
        createGroup.put("group", groupName);
        try {
            String res = WxUtil.postUrl(createGroupUrl + getToken(), createGroup.toJSONString());
            JSONObject obj = JSONObject.parseObject(res);
            String group = obj.getString("group");
            JSONObject groupJson = JSONObject.parseObject(group);
            //从返回结果获取到id和name
            String id = groupJson.getString("id");
            String name = groupJson.getString("name");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(String openId, String content) {
        JSONObject messageJson = new JSONObject();
        messageJson.put("touser", openId);
        messageJson.put("msgtype", "text");
        JSONObject textJson = new JSONObject();
        textJson.put("content", content);
        messageJson.put("text", textJson);
        System.out.println(messageJson.toJSONString());
        try {
            String resJson = WxUtil.postUrl(sendMsgUrl + getToken(), messageJson.toJSONString());
            log.warn("微信消息发送装状态：" + resJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取token
    public static String getToken() {
        //token需要保存本地
        File f = new File(WeChatConfig.PATH);
        if (!f.exists()) {
            f.mkdirs();
        }
        String fileName = "wxToken.log";
        File file = new File(f, fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        JSONObject tokenJson = WxUtil.readFile(fileName);
        if (tokenJson != null && tokenJson.size() > 0 && System.currentTimeMillis() <= tokenJson.getLongValue("mTime")) {
            String wxToken = tokenJson.getString("access_token");
            return wxToken;
        } else {
            try {
                String url = WeChatConfig.TOKEN_URL.replaceAll("APPID",WeChatConfig.APP_ID).replaceAll("SECRET",WeChatConfig.APP_SECRET);
                String retStr = WxUtil.getUrl(url);
                JSONObject json = JSON.parseObject(retStr);
                json.put("mTime", System.currentTimeMillis() + (7000 * 1000));
                WxUtil.saveFile(fileName, json.toJSONString());
                return json.getString("access_token");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

}
