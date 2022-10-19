package love.huhu.command;

import cn.hutool.cron.CronUtil;
import cn.hutool.setting.Setting;
import love.huhu.BotMain;
import love.huhu.operator.ConfigOperator;
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
        this(BotMain.INSTANCE,"set","设置","sz");
    }

    public SettingCommand(@NotNull CommandOwner owner, @NotNull String primaryName, @NotNull String... secondaryNames) {
        super(owner, primaryName, secondaryNames);
    }

    private ConfigOperator configOperator = new ConfigOperator();
    @SubCommand({"start","启动","开启","run"})
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
    @SubCommand({"stop","暂停","停止","关闭","close"})
    public void stop(CommandSender sender) {
        if (Context.configuration.getEnable()) {
            Context.configuration.setEnable(false);
            cron();
            sender.sendMessage("暂停成功");
        } else {
            sender.sendMessage("还没有启动呢");
        }
    }

    @SubCommand({"reload","cz","重新载入","读取订阅","载入订阅"})
    public void reload(CommandSender sender) {
        configOperator.loadConfig(BotMain.INSTANCE.getDescription().getVersion());
        sender.sendMessage("加载配置文件成功，只有插件是否启动的配置会在下次运行才被应用");
    }
    @SubCommand({"help","帮助","h","bz"})
    public void help(CommandSender sender) {
        MessageChainBuilder builder = new MessageChainBuilder();
        builder
                .append("/set start").append("\n")
                .append("解释：开始监听直播间列表，默认为开启").append("\n")
                .append("--------------\n")
                .append("/set stop").append("\n")
                .append("解释：停止监听直播间列表").append("\n")
                .append("--------------\n")
                .append("/set reload").append("\n")
                .append("解释：重新读取配置文件").append("\n");
        MessageChain chain = builder.build();
        sender.sendMessage(chain);
    }

}
