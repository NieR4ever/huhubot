package love.huhu;

import cn.hutool.core.lang.Assert;
import cn.hutool.cron.CronUtil;
import cn.hutool.extra.ssh.JschUtil;
import cn.hutool.extra.tokenizer.TokenizerUtil;
import love.huhu.pojo.Subscription;
import love.huhu.properties.ConfigProperties;
import love.huhu.properties.DataProperties;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.BotOnlineEvent;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.message.data.AtAll;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.utils.ExternalResource;

import java.io.File;
import java.util.HashMap;

public final class BotMain extends JavaPlugin {
    public static final BotMain INSTANCE = new BotMain();

    private BotMain() {
        super(new JvmPluginDescriptionBuilder("love.huhu.bot", "1.0")
                .name("提醒开播机器人")
                .info("为qq群和个人提供提醒主播开播的服务")
                .author("无糖雪碧")
                .build());
    }


    @Override
    public void onEnable() {
        GlobalEventChannel.INSTANCE.parentScope(BotMain.INSTANCE).subscribeAlways(BotOnlineEvent.class, event -> {
            //初始化bot
            ConfigProperties.bot = Bot.getInstance(Long.parseLong(ConfigProperties.botQQNumber));
            listenPrivate();
        });
    }

    private void listenPrivate() {
        ConfigProperties.bot.getEventChannel().parentScope(BotMain.INSTANCE).subscribeAlways(FriendMessageEvent.class, event -> {
            String command = event.getMessage().contentToString();
            if (!command.startsWith("#")) return;
            command = command.replace("#", "");
            System.out.println("command"+command);
            if (command.matches("^启动$") && !ConfigProperties.enablePlugin) {
                System.out.println("正在启动插件");
                ConfigProperties.enablePlugin = true;
                DataProperties.subscriptions.forEach(
                        subscription -> {
                            CronUtil.schedule("* * * * *", (Runnable) () -> {
                                boolean newLiveStatus = DataProperties.api.getLiveStatus(subscription.getRoomId());
                                boolean oldLiveStatus = subscription.isStreaming();
                                if (!oldLiveStatus && newLiveStatus) {
                                    //之前未开播,现在开播了
                                    //通知群
                                    subscription.getGroups().forEach(g -> {
                                        System.out.println("开始通知"+g);
                                        Group group = ConfigProperties.bot.getGroup(g);
                                        Assert.notNull(group,"未找到群号为{}的群，请检查群号或加入该群",g);
                                        //todo 这里不应该写死
                                        MessageChainBuilder builder = new MessageChainBuilder();
                                        File kaibo = new File(BotMain.INSTANCE.getDataFolderPath() + "/kaibo.png");
                                        MessageChain msg = builder.append("开播了")
//                                                .append(AtAll.INSTANCE)
                                                .append(ExternalResource.uploadAsImage(kaibo, group)).build();
                                        group.sendMessage(msg);
                                    });
                                }
                                //更新开播状态
                                if (oldLiveStatus ^ newLiveStatus) {
                                    subscription.setStreaming(newLiveStatus);
                                }
                            });
                        }
                );

                CronUtil.start();
                System.out.println("启动成功");
            }
            if (command.matches("^关闭$") && ConfigProperties.enablePlugin) {
                System.out.println("正在关闭插件");
                ConfigProperties.enablePlugin = false;
                CronUtil.stop();
            }
            if (command.matches("^订阅.*$")) {
                System.out.println("正在订阅直播间");
                String s = command.replace("订阅 ", "");
                String[] params = s.split(ConfigProperties.delimiter);
                if (params.length != 3) {
                    event.getSubject().sendMessage("参数格式不正确，应为\n" +
                            "#订阅 【平台】 【房间号】【通知群号(使用逗号分隔)】\n" +
                            "例如：\n" +
                            "#订阅 b站 1013 228029238,500111023");
                }
                String platform = params[0];
                String roomId = params[1];
                String[] groups = params[2].replaceAll("，", ",").split(",");
                Subscription subscription = new Subscription(platform, roomId, groups);
                DataProperties.subscriptions.add(subscription);
                event.getSubject().sendMessage("订阅成功");
            }
            if (command.matches("^查看订阅$")) {
                System.out.println("正在查看订阅");
                StringBuilder sb = new StringBuilder();
                DataProperties.subscriptions.forEach(subscription -> {
                    sb.append(subscription.toString());
                });
                if (sb.toString().isEmpty()) {
                    event.getSubject().sendMessage("订阅列表为空");
                } else{
                    event.getSubject().sendMessage(sb.toString());
                }
            }
        });
    }
}