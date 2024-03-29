package com.daomi.service.impl;

import com.daomi.constant.FilePathConstant;
import com.daomi.properties.CleanAudioProperties;
import com.daomi.properties.LocalOssProperties;
import com.daomi.properties.PcmToSilkProperties;
import com.daomi.service.AudioService;
import com.daomi.util.AudioUtil;
import com.daomi.util.CommandUtil;
import com.daomi.util.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AudioServiceImpl implements AudioService {

    // 存放数据的目录
    private static String dataPath = FilePathConstant.dataPath;

    // 注入本地对象存储的配置
    @Autowired
    private LocalOssProperties localOssProperties;

    // 注入pcm转silk的配置
    @Autowired
    private PcmToSilkProperties pcmToSilkProperties;

    // 注入清理音频的配置
    @Autowired
    private CleanAudioProperties cleanAudioProperties;

    /**
     * 将源链接音频的格式转为silk格式，并返回转换后的silk链接
     * @author ItJsen
     * @date 21:46 2024/3/28
     * @param srcUrl
     * @param format
     * @return java.lang.String
     **/
    @Override
    public String toSilk(String srcUrl, String format) {
        // 1、创建所有存放音频的目录
        // 创建srcAudio父级目录
        File srcParentDir = new File(dataPath + "srcAudio");
        srcParentDir.mkdirs();
        // 创建pcmAudio父级目录
        File pcmParentDir = new File(dataPath + "pcmAudio");
        pcmParentDir.mkdirs();
        // 创建silkAudio父级目录
        File silkParentDir = new File(dataPath + "silkAudio");
        silkParentDir.mkdirs();

        // 2、下载源音频
        // 生成uuid，防止文件名重复
        String audioName = UUID.randomUUID().toString();
        // 构造源音频暂存的File对象
        File srcAudio = new File(srcParentDir, audioName + format);
        // 开始下载源音频...
        AudioUtil.downloadSrcAudio(srcUrl, srcAudio);

        // 3、转为pcm
        // 获取源音频路径
        String srcPath = srcAudio.getPath();
        log.info("{}存放路径-->{}", format.substring(1), srcPath);
        // 构造pcm路径
        String pcmPath = new File(pcmParentDir, audioName + ".pcm").getPath();
        log.info("pcm存放路径-->{}", pcmPath);
        // 构造ffmpeg命令
        List<String> ffmpegCommand = new ArrayList<>();
        ffmpegCommand.add("ffmpeg");
        ffmpegCommand.add("-i");
        ffmpegCommand.add(srcPath);
        ffmpegCommand.add("-f");
        ffmpegCommand.add("s16le");
        ffmpegCommand.add("-ac");
        ffmpegCommand.add("1");
        ffmpegCommand.add("-acodec");
        ffmpegCommand.add("pcm_s16le");
        ffmpegCommand.add(pcmPath);
        String ffmpegCommandStr = String.join(" ", ffmpegCommand);
        log.info("生成ffmpeg命令-->{}", ffmpegCommandStr);
        // 开始转为pcm...
        log.info("开始执行ffmpeg命令...");
        CommandUtil.execCommand(ffmpegCommand);
        log.info("ffmpeg命令执行完毕");
        // 执行完删除源音频
        srcAudio.delete();
        log.info("已删除{}", format.substring(1));

        // 4、转为silk
        // 构造silk路径
        String silkPath = new File(silkParentDir, audioName + ".silk").getPath();
        log.info("silk存放路径-->{}", silkPath);
        // 构造encoder命令
        List<String> encoderCommand = new ArrayList<>();
        encoderCommand.add("/app/silk/encoder");
        encoderCommand.add(pcmPath);
        encoderCommand.add(silkPath);
        encoderCommand.add("-rate");
        encoderCommand.add(pcmToSilkProperties.getRate());
        encoderCommand.add("-Fs_API");
        encoderCommand.add(pcmToSilkProperties.getApiSampleRate());
        encoderCommand.add("-Fs_maxInternal");
        encoderCommand.add(pcmToSilkProperties.getMaxIntervalSampleRate());
        encoderCommand.add("-tencent");
        String encoderCommandStr = String.join(" ", encoderCommand);
        log.info("生成encoder命令-->{}", encoderCommandStr);
        // 开始转为silk
        log.info("开始执行encoder命令...");
        CommandUtil.execCommand(encoderCommand);
        log.info("encoder命令执行完毕");
        // 执行完删除pcm
        new File(pcmPath).delete();
        log.info("已删除pcm");
        // 一段时间后删除silk
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.schedule(() -> {
            new File(silkPath).delete();
            log.info("该silk已过最大存活时间，已删除-->{}", silkPath);
        }, cleanAudioProperties.getSilkMaxAge(), TimeUnit.SECONDS);
        executor.shutdown();

        // 5、返回可直接访问的silk音频的链接
        String dataHost = localOssProperties.getHost();
        if (dataHost == null || "".equals(dataHost)){
            dataHost = IpUtil.getPublicNetIp();
        }
        String silkUrl = new StringBuilder("http://")
                .append(dataHost)
                .append(":8081")
                .append("/silk/")
                .append(audioName)
                .append(".silk")
                .toString();
        log.info("生成silk链接-->{}", silkUrl);

        return silkUrl;
    }
}
