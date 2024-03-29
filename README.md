# 1、功能介绍
将mp3、amr或flac格式(目前测试的这些可以)的音频链接转换为silk格式的音频，请求完毕后，服务端将给你响应一个silk格式音频的链接。
> 生成的silk链接默认5分钟内有效。如果你想要修改有效时间或者其他配置，并且你已经掌握了Docker的基础知识，那么可以阅读<a href="#4.3">自定义配置</a>。

# 2、🚀快速部署
>说明：直接拉取我已经build好的镜像进行快速部署服务
## 2.1、🍀准备工作
* 准备一台具有公网ip的服务器
* 确保服务器的8081、入方向、TCP协议的端口开放
* 确保服务器已安装Docker并且已启动
> 快速部署针对具有公网ip的服务器。如果你想部署在非公网ip的主机上，并且你已经掌握了Docker的基础知识，那么可以阅读<a href="#4.3">自定义配置</a>。

## 2.2、🍀拉取镜像
```bash
docker pull itjsen/daomi-converter:1.0.0
```
```bash
docker tag itjsen/daomi-converter:1.0.0 daomi-converter:1.0.0
```
```bash
docker rmi itjsen/daomi-converter:1.0.0
```


## 2.3、🍀创建容器
```bash
docker run --name daomi-converter \
-p 8081:8081 \
-v /app/daomi-converter/log:/app/daomi-converter/log \
-v /app/daomi-converter/data:/app/daomi-converter/data \
-d daomi-converter:1.0.0
```

## 2.4、🍀查看实时日志观察运行情况
```bash
docker logs daomi-converter -f
```
当然，你也可以在这个目录下查看到日志：[/app/daomi-converter/log]，
如果出现如下图的日志，说明运行成功：
![image](https://github.com/itjsen/daomi-converter/assets/117729928/e8f7408e-9709-4c94-adee-b040b2644a83)
<pre>
🔖
按Ctrl+C可退出实时查看日志
停止容器的运行：docker stop daomi-converter
启动已经创建好的容器：docker start daomi-converter
删除已经存在的容器(删除前须先停止容器)：docker rm daomi-converter
</pre>

# 3、🚀请求说明
* 如果音频链接的末尾包含了音频的扩展名，如：[http://localhost/music.mp3]，
那么可以直接使用<a href="#3.1">接口1</a>，也可以使用<a href="#3.2">接口2</a>。
* 如果音频链接的末尾并没有包含音频的扩展名，如：[http://localhost/music]，
那么只能使用<a href="#3.2">接口2</a>。

## <a id="3.1">3.1、💡接口1</a>
* 1)请求地址：[http://你的域名或ip:8081/audio/convertToSilk]，如：[http://localhost:8081/audio/convertToSilk]
* 2)请求方式：GET
* 3)请求参数：

|   属性   |  数据类型  | 是否必须携带 |                说明                |
|:------:|:------:|:------:|:--------------------------------:|
| srcUrl | string |   是    |               音频链接               |

* 请求示例：[http://localhost:8081/audio/convertToSilk?srcUrl=http://localhost/music.mp3]
* 响应示例：
```json
{
    "code": 1,
    "msg": null,
    "data": "http://localhost:8081/silk/music.silk"
}
```


## <a id="3.2">3.2、💡接口2</a>
* 1)请求地址：[http://你的域名或ip:8081/audio/convertToSilk]，如[http://localhost:8081/audio/convertToSilk]
* 2)请求方式：POST
* 3)请求头：[Content-Type: application/json]
* 4)请求体参数：

|   属性   |  数据类型  | 是否必须携带 |                说明                |
|:------:|:------:|:------:|:--------------------------------:|
| srcUrl | string |   是    |               音频链接               |
| format | string |   是    | 音频的格式(只能为：".mp3"、".amr"或".flac") |

* 请求体参数示例：
```json
{
    "srcUrl": "http://localhost/music.mp3",
    "format": ".mp3"
}
```
* 响应示例：
```json
{
    "code": 1,
    "msg": null,
    "data": "http://localhost:8081/silk/music.silk"
}
```


# 4、Docker镜像说明(选读)
>如果你只关心怎么快速部署、怎么使用，那下面的说明你可以不阅读。
## 4.1、容器内部日志存放目录
```
/app/daomi-converter/log/
```
## 4.2、容器内部数据存放目录
```
/app/daomi-converter/data/
```
## <a id="4.3">4.3、自定义配置</a>
容器内部配置文件存放目录：
```
/app/daomi-converter/config/
```
如果你要自定义配置，或者你是部署在非公网ip的主机上，那么需要在创建容器前做如下的工作：
* 1)新建并编写`/app/daomi-converter/config/application.yml`文件：
```yml
server:
  port: 8081
daomi-converter:
  localoss:
    # 这里可以填你服务器的域名，也可以不填。注意：如果是用虚拟机等非公网ip的主机来部署的，则这里必填你的局域网ip
    host:
  pcm-to-silk:
    # 比特率(最好别改了)
    rate: 128000
    # API采样率(最好别改了)
    api-sample-rate: 44100
    # 最大间隔采样率(可选值：12000、16000、24000，这个值越大，silk文件也越大，音质也越高)
    max-interval-sample-rate: 16000
  clean-audio:
    # silk文件的存活时长，单位是秒(按需修改)
    silk-max-age: 300
```
* 2)在创建容器时，额外挂载配置文件所在的目录：
```bash
docker run --name daomi-converter \
-p 8081:8081 \
-v /app/daomi-converter/log:/app/daomi-converter/log \
-v /app/daomi-converter/data:/app/daomi-converter/data \
-v /app/daomi-converter/config:/app/daomi-converter/config \
-d daomi-converter:1.0.0
```


# 转换原理
* 1)使用<a href="https://ffmpeg.org/">ffmpeg</a>将音频转换pcm格式
* 2)再使用<a href="https://github.com/kn007/silk-v3-decoder">silk编码器</a>将pcm格式的音频转为silk格式


# 鸣谢
感谢<a href="https://github.com/kn007/silk-v3-decoder">kn007</a>提供的silk编码器。
