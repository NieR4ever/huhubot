# 直播通知机器人

[![发行版](https://img.shields.io/github/v/release/Nier4ever/huhubot?style=flat-square)](https://github.com/NieR4ever/huhubot/releases)
![下载量](https://img.shields.io/github/downloads/nier4ever/huhubot/total?style=flat-square)
[![MiraiForum](https://img.shields.io/badge/post-on%20MiraiForum-yellow)](https://mirai.mamoe.net/topic/1689)
> 主播开播时自动向指定qq群发送直播通知，现支持斗鱼和b站

**使用前应该查阅的相关文档或项目**

* [User Manual](https://github.com/mamoe/mirai/blob/dev/docs/UserManual.md)
* [Permission Command](https://github.com/mamoe/mirai/blob/dev/mirai-console/docs/BuiltInCommands.md#permissioncommand)
* [Chat Command](https://github.com/project-mirai/chat-command)
## 食用指南
0. 先使用下面这条命令安装[project-mirai/chat-command](https://github.com/project-mirai/chat-command)插件，或者手动安装

   > ./mcl --update-package net.mamoe:chat-command --channel stable --type plugin

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
  + `reload`|`cz`|`重新载入`|`读取订阅`|`载入订阅`
  + `permit`|`add`|`grant`|`授权`|`sq`
  + `cancel`|`remove`|`取消`|`qx`
  + `check`|`检查`|`jc`
+ /`sub`|`订阅`|`dy`
  + `add`|`添加`|`新增`|`update`|`修改`
  + `save`|`保存`|`bc`
  + `rm`|`删除`|`移除`|`del`|`delete`|`remove`|`sc`
  + `list`|`列表`|`all`|`lb`
  + `notify`|`通知`|`tz`
  + `help`|`帮助`|`h`|`bz`
  + `reload`|`cz`|`重新载入`|`读取订阅`|`载入订阅`
  + `echo`|`show`|`显示`|`展示`|`xs`|`zz`

> 指令具体含义可使用`/set h`和`/sub h`指令查看

## 通知模板

从1.2.0版本开始，支持通知内容中包含动态信息，编写格式为`${xxx}`，xxx表示想要插入的信息，例如`${title}`代表在当前位置显示直播间标题，不同平台支持显示的信息不同，以下面表格中的内容为准

|     参数      |      解释      | b站  | 斗鱼 |
| :-----------: | :------------: | :--: | :--: |
|      uid      |     主播id     |   &#10004;   |      |
|    roomId     |    直播间号    | &#10004; | &#10004; |
|   attention   |     粉丝数     | &#10004; |      |
|    online     | 观看人气或人数 | &#10004; |      |
|  description  |   直播间描述   | &#10004; | &#10004; |
|   areaName    |      分区      | &#10004; |      |
| backgroundUrl |    背景图片    | &#10004; |      |
|     title     |   直播间标题   | &#10004; | &#10004; |
| userCoverUrl  |   直播间封面   | &#10004; |      |
|  keyframeUrl  |  当前直播画面  | &#10004; | &#10004; |
|   liveTime    |    直播时间    | &#10004; |      |
|     tags      |      标签      | &#10004; |      |
|    roomUrl    |   直播间链接   | &#10004; | &#10004; |

## 权限管理

1. 可以使用mirai默认的权限管理指令，前提是确保你的管理员账号拥有对应的权限，如果没有，请使用`/permission permit <账号> *:*`授权
2. 建议使用本插件提供的权限管理指令管理插件内的权限
   1. `/set permit <账号> <权限1> [权限2]`：赋予`账号`权限，众所周知，<>标识的参数必填，[]标识的参数选填，可以一条命令赋予多个权限，即必须填一个或一个以上权限参数，权限参数可填`*`(插件的所有权限)，`set`(插件set指令权限)，`sub`(插件sub指令权限)
   2. `/set cancel <账号> <权限>`：

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

