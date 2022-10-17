package love.huhu.command;

import cn.hutool.cron.CronUtil;
import cn.hutool.setting.Setting;
import love.huhu.BotMain;
import love.huhu.properties.Context;
import love.huhu.properties.DataProperties;
import love.huhu.task.TimeTask;
import net.mamoe.mirai.console.command.CommandOwner;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JCompositeCommand;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

/**
 * @Author
 * @create 2022/9/5 8:24
 */
public final class SettingCommand extends JCompositeCommand {


    public SettingCommand() {
        this(BotMain.INSTANCE,"set","set");
    }

    public SettingCommand(@NotNull CommandOwner owner, @NotNull String primaryName, @NotNull String... secondaryNames) {
        super(owner, primaryName, secondaryNames);
    }

    @SubCommand("start")
    public void start(CommandSender sender) {
        if (!Context.configuration.getEnable()) {
            Context.configuration.setEnable(true);
            cron();
            sender.sendMessage("启动成功");
        } else {
            sender.sendMessage("请勿重复启动");
        }
    }
    private void cron() {
        if (Context.configuration.getEnable()) {
            CronUtil.start();
        } else {
            CronUtil.getScheduler().stop(false);
        }
    }
    @SubCommand("stop")
    public void stop(CommandSender sender) {
        if (Context.configuration.getEnable()) {
            Context.configuration.setEnable(false);
            cron();
            sender.sendMessage("暂停成功");
        } else {
            sender.sendMessage("还没有启动呢");
        }
    }

    @SubCommand("help")
    public void help(CommandSender sender) {
        MessageChainBuilder builder = new MessageChainBuilder();
        builder
                .append("/set start").append("\n")
                .append("解释：开始监听直播间列表").append("\n")
                .append("/set stop").append("\n")
                .append("解释：停止监听直播间列表").append("\n");
        MessageChain chain = builder.build();
        sender.sendMessage(chain);
    }

}
