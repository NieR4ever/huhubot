# 直播通知机器人

![发行版](https://img.shields.io/github/v/release/Nier4ever/huhubot?style=flat-square)![下载量](https://img.shields.io/github/downloads/nier4ever/huhubot/total?style=flat-square)

> 主播开播时自动向指定qq群发送直播通知

## 食用指南

1. 将本插件放入`plugins`文件夹中
2. 启动`mcl`，此时将在`config/love.huhu.bot`目录下生成`config.setting`配置文件
3. 编辑配置文件

```properties
[]
# 是否在mcl启动时启动本插件，默认开启
enable = true 
# 管理员列表，以英文逗号`,`分隔
admins = 123456
# 可以识别的平台别称，嫌打字麻烦可以添加一个字母代表当前平台，不同平台别称不可重复
bilibili = b站,bili,bilibili,B站
douyu = 斗鱼,douyu
# 如果你不知道你在做什么请不要修改版本号
version = 1.0
```

4. 再次启动mcl
5. 愉快地和bot互动吧

## 常见问题

+ bot因指令参数不正常异常终止：没有做异常处理，所以请保证指令参数输入正确

## 指令

+ /`set`|`设置`|`sz`

  + `start`|`启动`|`开启`|`run`
  + `stop`|`暂停`|`停止`|`关闭`|`close`
  + `help`|`帮助`|`h`|`bz`
+ /`sub`|`订阅`|`dy`
  + `add`|`添加`|`新增`|`update`|`修改`
  + `save`|`保存`|`bc`
  + `rm`|`删除`|`移除`|`del`|`delete`|`remove`|`sc`
  + `list`|`列表`|`all`|`lb`
  + `notify`|`通知`|`tz`
  + `help`|`帮助`|`h`|`bz`

> 指令具体含义可使用`set h`和`sub h`指令查看

## 进阶

直播间的订阅信息存放在`data/love.huhu.bot`文件夹下`subscriptions.setting`中

一般的文件格式为

```properties
#分组 每个分组代表一条订阅
[订阅名]
#平台名称，支持 B站：BILIBILI；斗鱼：DOUYU；
platform = BILIBILI
#房间号
roomId = 7192947
#通知群号，群号间以英文逗号`,`分隔，如：123456,789123
groups = 228029238
#进行通知的bot的qq号
botNumber = 3556473927
#通知内容，存储的格式为MiraiCode
notifyMiraiCode = 开播[mirai:image:{CB0B82A3-6E99-2B67-7228-49A56D388744}.jpg]
```

如果想要进行`@全体成员`或拼接其他在输入框中不便输入的内容，可以在此修改，具体生成MiraiCode的方式可以在[Mirai - Messages | mirai (mamoe.net)](https://docs.mirai.mamoe.net/Messages.html#mirai-码)进行查看

