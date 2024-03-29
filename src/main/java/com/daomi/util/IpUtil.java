package com.daomi.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class IpUtil {

    /**
     * 获取当前主机的公网ip
     * @author ItJsen
     * @date 15:26 2024/3/28
     * @return java.lang.String
     **/
    public static String getPublicNetIp(){
        String publicNetIp = null;
        try {
            URL url = new URL("https://checkip.amazonaws.com/");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            publicNetIp = br.readLine().trim();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return publicNetIp;
    }

}
