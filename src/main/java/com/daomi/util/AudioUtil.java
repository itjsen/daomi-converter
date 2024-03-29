package com.daomi.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * 音频相关工具
 */
@Slf4j
public class AudioUtil {

    /**
     * 下载源音频到本地
     * @author ItJsen
     * @date 20:18 2024/3/6
     * @param srcUrl
     * @param target
     **/
    public static void downloadSrcAudio(String srcUrl, File target) {
        log.info("开始下载源音频...");
        try {
            URL url = new URL(srcUrl);
            URLConnection conn = url.openConnection();
            try (InputStream in = conn.getInputStream();
                 FileOutputStream out = new FileOutputStream(target)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) != -1) {
                    out.write(buffer, 0, length);
                }
            }
            log.info("源音频下载完毕！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
