package com.jhhl.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jhhl.base.model.WXMessage;
import com.jhhl.config.WeChatConfig;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import java.io.File;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by YANGKANG on 16/2/28.
 */
@SuppressWarnings("ALL")
public class WxUtil {

    public final static String SNSAPI_USERINFO = "snsapi_userinfo";

    public final static String STATE = "STATE";

    public final static String CODE = "code";


    /**
     * 请求消息类型：事件
     */
    public static final String REQ_MESSAGE_TYPE_EVENT = "event";

    /**
     * 事件类型：subscribe(关注)
     */
    public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";

    /**
     * 事件类型：unsubscribe(取消关注)
     */
    public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";


    public static String getCode() {
        String response = "";
        String url = WeChatConfig.CODE_URL + "?" + "appid=" + URLEncoder.encode(WeChatConfig.APP_ID) + "&redirect_uri=" + URLEncoder.encode(WeChatConfig.REDIRECT_URL) + "&response_type" + CODE
                + "&scope=" + SNSAPI_USERINFO + "&state=" + STATE + "#wechat_redirect";
        try {
            response = HttpClientUtil.getInstance().get(url);
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public static String getAccessToken(String code) {
        String response = "";
        String url = WeChatConfig.ACCESS_TOKEN_URL + "?" + "appid=" + URLEncoder.encode(WeChatConfig.APP_ID) + "&secret=" + WeChatConfig.APP_SECRET + "&code=" + code + "&grant_type=authorization_code";
        try {
            response = HttpClientUtil.getInstance().get(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public static String getUserInfo(String accessToken, String openId) {
        String response = "";
        String url = WeChatConfig.USER_INFO_URL + "?" + "access_token=" + accessToken + "&openid=" + openId + "&lang=zh_CN";
        try {
            response = HttpClientUtil.getInstance().get(url);
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    //get请求
    public static String getUrl(String url) throws Exception {
        String str = null;
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        CloseableHttpResponse response = client.execute(get);
        HttpEntity entity = response.getEntity();
        str = EntityUtils.toString(entity, "UTF-8");
        EntityUtils.consume(entity);
        response.close();
        return str;
    }

    public static String postUrl(String url, String body) throws Exception {
        String str = null;
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        StringEntity stringEntity = new StringEntity(body, "UTF-8");
        post.setEntity(stringEntity);
        CloseableHttpResponse response = client.execute(post);
        HttpEntity entity = response.getEntity();
        str = EntityUtils.toString(entity, "UTF-8");
        EntityUtils.consume(entity);
        response.close();
        return str;
    }

    public static void saveFile(String fileName, String mesg) {

        if (StringUtils.containsIgnoreCase(System.getProperties().getProperty("os.name"), "windows"))
            fileName = WeChatConfig.PATH + "/" + fileName;
        else
            //linux
            fileName = WeChatConfig.PATH + "/" + fileName;
        try {
            FileUtils.writeStringToFile(new File(fileName), mesg, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JSONObject readFile(String fileName) {
        if (StringUtils.containsIgnoreCase(System.getProperties().getProperty("os.name"), "windows"))
            fileName = WeChatConfig.PATH  + "/" + fileName;
        else
            fileName = WeChatConfig.PATH + "/" + fileName;
        try {
            String res = FileUtils.readFileToString(new File(fileName), "utf-8");
            return JSON.parseObject(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String makeXML(WXMessage wxMessage) {
        Document tDocument = DocumentHelper.createDocument();
        Element element = tDocument.addElement("xml");
        Element elements = element.addElement("ToUserName").addText("![CDATA[" + wxMessage.getFromUserName() + "]]");
        elements.addElement("FromUserName").setText("![CDATA[" + wxMessage.getToUserName() + "]]");
        elements.addElement("CtreatTime").setText("![CDATA[" + wxMessage.getCreateTime() + "]]");
        elements.addElement("MsgType").setText("![CDATA[" + wxMessage.getMsgType() + "]]");
        elements.addElement("Content").setText("![CDATA[" + wxMessage.getContent() + "]]");
        return tDocument.asXML().replaceAll("UTF-8", "UTF-8");
    }

    public static Map<String, Object> Dom2Map(Document doc) {
        Map<String, Object> map = new HashMap<>();
        if (doc == null)
            return map;
        Element root = doc.getRootElement();
        for (Iterator iterator = root.elementIterator(); iterator.hasNext(); ) {
            Element e = (Element) iterator.next();
            //System.out.println(e.getName());
            List list = e.elements();
            if (list.size() > 0) {
                map.put(e.getName(), Dom2Map((Document) e));
            } else
                map.put(e.getName(), e.getText());
        }
        return map;
    }

    public static String getAuthUrl() {
        return WeChatConfig.CODE_URL + "?" + "appid=" + URLEncoder.encode(WeChatConfig.APP_ID) + "&redirect_uri=" + URLEncoder.encode(WeChatConfig.REDIRECT_URL) + "&response_type=" + CODE + "&scope=" + SNSAPI_USERINFO + "&state=" + STATE + "#wechat_redirect";
    }

}
