/**
 * 下面的转换接口，只支持mp3、amr或flac格式的源链接音频进行转换
 */
package com.daomi.controller;

import com.daomi.constant.AudioConstant;
import com.daomi.constant.MessageConstant;
import com.daomi.exception.NullUrlException;
import com.daomi.exception.SrcFormatNotSupportedException;
import com.daomi.pojo.dto.ConvertToSilkDTO;
import com.daomi.result.Result;
import com.daomi.service.AudioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/audio")
@Slf4j
public class AudioController {

    // 注入AudioService
    @Autowired
    private AudioService audioService;

    /**
     * 将源链接的音频转换为silk格式的，最终会响应给客户端一个silk格式音频的链接。
     * @author ItJsen
     * @date 20:18 2024/3/6
     * @param convertToSilkDTO
     * @return com.audio.result.Result<java.lang.String>
     **/
    @PostMapping("/convertToSilk")
    public Result<String> convertToSilk(@RequestBody ConvertToSilkDTO convertToSilkDTO) {
        // 1、检查源音频的格式
        log.info("请求参数-->{}", convertToSilkDTO);
        // 获取源音频的格式
        String format = convertToSilkDTO.getFormat();
        // 获取允许的格式
        List<String> formatPattern = Arrays.asList(AudioConstant.EXTEND_NAME_PATTERN);
        // 开始检查源音频的格式...
        if (!formatPattern.contains(format)){
            // 不是支持的源格式，抛出自定义异常
            throw new SrcFormatNotSupportedException(MessageConstant.SRC_FORMAT_NOT_SUPPORTED);
        }

        // 2、调用service层进行转换
        String silkUrl = audioService.toSilk(convertToSilkDTO.getSrcUrl(), format);

        return Result.success(silkUrl);
    }

    /**
     * 将源链接的音频转换为silk格式的，最终会响应给客户端一个silk格式音频的链接。
     * @author ItJsen
     * @date 21:53 2024/3/28
     * @param srcUrl
     * @return com.daomi.result.Result<java.lang.String>
     **/
    @GetMapping("/convertToSilk")
    public Result<String> convertToSilk(String srcUrl){
        // 检查参数是否为空
        if (srcUrl == null || "".equals(srcUrl)){
            throw new NullUrlException(MessageConstant.NULL_URL);
        }
        // 获取音频的格式
        String format = srcUrl.substring(srcUrl.lastIndexOf("."));
        // 调用service层进行转换
        String silkUrl = audioService.toSilk(srcUrl, format);
        return Result.success(silkUrl);
    }

}
