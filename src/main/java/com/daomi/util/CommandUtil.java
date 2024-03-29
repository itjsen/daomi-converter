package com.daomi.util;

import com.daomi.constant.MessageConstant;
import com.daomi.exception.CommandTimeoutException;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CommandUtil {

    /**
     * 读取命令执行的输出流，并且将读取到的流输出到日志
     * @author ItJsen
     * @date 17:31 2024/3/26
     * @param inputStream
     **/
    private static void readStream(InputStream inputStream) {
        new Thread(() -> {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.startsWith("Packets encoded:")){
                        log.info(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    /**
     * 在终端中执行一个指定的命令
     * @author ItJsen
     * @date 17:35 2024/3/26 
     * @param command 
     **/
    public static void execCommand(List command){
        try {
            Process process = new ProcessBuilder(command).redirectErrorStream(true).start();
            // 读取命令的输出流和错误流
            readStream(process.getInputStream());
            if (!process.waitFor(15, TimeUnit.SECONDS)){
                throw new CommandTimeoutException(MessageConstant.COMMAND_TIMEOUT);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}
