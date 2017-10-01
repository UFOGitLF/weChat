package com.jhhl.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 微信配置信息
 * Created by 楊康 on 2017/10/1.
 */
@Component
public class WeChatConfig {

    @PostConstruct
    public void init() {
        APP_ID = appId;
        APP_SECRET = appSecret;
        TOKEN_URL = getTokenUrl;
        CODE_URL = codeUrl;
        ACCESS_TOKEN_URL = accessTokenUrl;
        USER_INFO_URL = userInfoUrl;
        REDIRECT_URL = redirectUrl;
        PATH = path;
    }

    @Value("${weChat.appId:wx6481f43c35d6b1f2}")
    private String appId;

    @Value("${weChat.appSecret:d13e6b5698d8ec09cfb32e47164064e4}")
    private String appSecret;

    @Value("${weChat.getTokenUrl}")
    private String getTokenUrl;

    @Value("${weChat.path}")
    private String path;

    @Value("${weChat.codeUrl:https://open.weixin.qq.com/connect/oauth2/authorize}")
    private String codeUrl;

    @Value("${weChat.accessTokenUrl:https://api.weixin.qq.com/sns/oauth2/access_token}")
    private String accessTokenUrl;

    @Value("${weChat.userInfoUrl:https://api.weixin.qq.com/sns/userinfo}")
    private String userInfoUrl;

    @Value("${weChat.redirectUrl:http://yktest.tunnel.qydev.com/api/public/auth}")
    private String redirectUrl;

    public static String APP_ID;

    public static String APP_SECRET;

    public static String TOKEN_URL;

    public static String PATH;

    //auth
    public static String CODE_URL;

    public static String ACCESS_TOKEN_URL;

    public static String  USER_INFO_URL;

    public static String REDIRECT_URL;



}
