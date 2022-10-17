package love.huhu.command;

import cn.hutool.setting.Setting;
import love.huhu.BotMain;
import love.huhu.pojo.Subscription;
import love.huhu.properties.DataProperties;
import net.mamoe.mirai.console.command.CommandOwner;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JCompositeCommand;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.MessageContent;
import org.jetbrains.annotations.NotNull;

import javax.xml.crypto.Data;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author nwl20
 * @create 2022/10/16 9:27
 */
public class SubscribeCommand extends JCompositeCommand {
    public SubscribeCommand() {
        this(BotMain.INSTANCE,"sub","订阅","dy");
    }
    public SubscribeCommand(@NotNull CommandOwner owner, @NotNull String primaryName, @NotNull String... secondaryNames) {
        super(owner, primaryName, secondaryNames);
    }

    @SubCommand({"add","添加","新增","update","修改"})
    public void add(CommandSender sender,String name, String platform,String roomId, String... groups) {
        Subscription subscription = new Subscription(name, platform, roomId, groups,sender.getBot().getId());
        if (DataProperties.subscriptions.contains(subscription)) {
            //存在就修改
            DataProperties.subscriptions.stream()
                    .filter(sub -> sub.hashCode() == subscription.hashCode())
                    .findFirst().map(sub -> {
                        sub.setBotNumber(subscription.getBotNumber());
                        sub.setGroups(subscription.getGroups());
                        sub.setRoomId(subscription.getRoomId());
                        sub.setPlatform(subscription.getPlatform());
                        return sub;
                    });
            sender.sendMessage("订阅已存在，更新订阅成功");
            return;
        }
        DataProperties.subscriptions.add(subscription);
        sender.sendMessage("添加订阅成功");
    }
    @SubCommand({"save","保存","bc"})
    public void save(CommandSender sender) {
        Setting setting = new Setting(BotMain.INSTANCE.resolveDataFile("subscriptions.setting").getAbsoluteFile(), StandardCharsets.UTF_8, true);
        List<String> groupToRemove = setting.getGroups().stream().filter(group -> DataProperties.subscriptions.stream().noneMatch(subscription -> subscription.getName().equals(group))).collect(Collectors.toList());
        groupToRemove.forEach(setting::clear);
        DataProperties.subscriptions.forEach(subscription -> {
            setting.setByGroup("platform",subscription.getName(),subscription.getPlatform().name());
            setting.setByGroup("roomId",subscription.getName(),subscription.getRoomId());
            setting.setByGroup("groups",subscription.getName(),String.join(",",subscription.getGroups()));
            setting.setByGroup("botNumber",subscription.getName(), String.valueOf(subscription.getBotNumber()));
            setting.setByGroup("notifyMiraiCode",subscription.getName(), subscription.getNotifyMiraiCode());
        });
        setting.store();
        sender.sendMessage("保存/修改订阅成功");
    }
    @SubCommand({"rm","删除","移除","del","delete","remove","sc"})
    public void rm(CommandSender sender, String name) {
        DataProperties.subscriptions.removeIf(subscription -> subscription.getName().equals(name.trim()));
        sender.sendMessage("删除订阅成功");
    }
    @SubCommand({"list","列表","all","lb"})
    public void list(CommandSender sender) {
        MessageChainBuilder builder = new MessageChainBuilder();
        if (DataProperties.subscriptions.isEmpty()) {
            sender.sendMessage("订阅列表为空");
            return;
        }
        DataProperties.subscriptions.forEach(subscription -> {
            builder
                    .append("订阅名：").append(subscription.getName()).append("\n")
                    .append("平台：").append(subscription.getPlatform().name()).append("\n")
                    .append("房间号：").append(subscription.getRoomId()).append("\n")
                    .append("直播状态：").append(subscription.getBroadcast().getLiveStatus() != null && subscription.getBroadcast().getLiveStatus() == 1 ? "正在直播" : "未开播").append("\n")
                    .append("通知群号：").append(String.join(",", subscription.getGroups())).append("\n")
                    .append("-----------------\n");
        });
        sender.sendMessage(builder.build());
    }
    @SubCommand({"notify","通知","tz"})
    public void notify(CommandSender sender,String name, MessageContent... messages) {
        MessageChainBuilder builder = new MessageChainBuilder();
        for (MessageContent message : messages) {
            builder.append(message);
        }
        MessageChain chain = builder.build();
        String notifyMiraiCode = chain.serializeToMiraiCode();
        System.out.println(notifyMiraiCode);
        DataProperties.subscriptions.forEach(subscription -> {
            if (subscription.getName().equals(name)) {
                subscription.setNotifyMiraiCode(notifyMiraiCode);
                System.out.println(subscription);
            }
        });
        sender.sendMessage("修改通知内容成功");
    }
    @SubCommand({"help","帮助","h","bz"})
    public void help(CommandSender sender) {
        MessageChainBuilder builder = new MessageChainBuilder();
        builder
                .append("/sub add <订阅名(唯一)> <平台> <房间号> <通知群号列表>").append("\n")
                .append("解释：添加一条直播订阅，当订阅名重复将覆盖先前订阅").append("\n")
                .append("示例：/sub add 测试 平台名称 房间号 群1 群2").append("\n")
                .append("--------------\n")
                .append("/sub save").append("\n")
                .append("解释：保存当前直播订阅列表到本地，所有改动后不执行该条指令代表只在本次运行中生效").append("\n")
                .append("--------------\n")
                .append("/sub rm <订阅名>").append("\n")
                .append("解释：删除一条订阅").append("\n")
                .append("--------------\n")
                .append("/sub list").append("\n")
                .append("解释：显示当前订阅列表").append("\n")
                .append("--------------\n")
                .append("/sub notify <订阅名> <通知消息内容>").append("\n")
                .append("解释：编辑在群里展示的通知内容").append("\n")
                .append("示例：/sub notify 订阅名 开播了[图片]").append("\n");

        MessageChain chain = builder.build();
        sender.sendMessage(chain);
    }

}
