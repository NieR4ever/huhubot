package love.huhu;

import cn.hutool.core.io.FileUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.setting.Setting;
import love.huhu.command.SettingCommand;
import love.huhu.command.SubscribeCommand;
import love.huhu.operator.ConfigOperator;
import love.huhu.operator.DataOperator;
import love.huhu.pojo.Configuration;
import love.huhu.properties.Context;
import love.huhu.properties.DataProperties;
import love.huhu.task.TimeTask;
import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.permission.AbstractPermitteeId;
import net.mamoe.mirai.console.permission.PermissionId;
import net.mamoe.mirai.console.permission.PermissionService;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.console.util.SemVersion;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.BotOnlineEvent;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public final class BotMain extends JavaPlugin {
    public static final BotMain INSTANCE = new BotMain();

    private DataOperator dataOperator = new DataOperator();
    private ConfigOperator configOperator = new ConfigOperator();
    private BotMain() {
        super(new JvmPluginDescriptionBuilder( // 必要属性
                "love.huhu.bot", // id
                "1.1.0" // version
        ).author("无糖雪碧")
                .name("提醒开播机器人")
                .info("为qq群提供提醒主播开播的服务")
                .build());
    }

    @Override
    public void onEnable() {
        init();
//        registerPermission();
        GlobalEventChannel.INSTANCE.parentScope(BotMain.INSTANCE).subscribeAlways(BotOnlineEvent.class, event -> {
            //开始监听直播间
            listenBroadcast();
        });
    }

    public void registerPermission() {
        List<AbstractPermitteeId.ExactUser> users = Arrays.stream(Context.configuration.getAdmins())
                .map(admin-> new AbstractPermitteeId.ExactUser(Long.parseLong(admin)))
                .collect(Collectors.toList());
        PermissionId permissionId = this.getParentPermission().getId();
        users.forEach(user->{
            if (!PermissionService.hasPermission(user,permissionId)) {
                PermissionService.permit(user,permissionId);
            }
        });
    }


    private void listenBroadcast() {
        CronUtil.schedule("* * * * * *", (Runnable) () -> {
            DataProperties.subscriptions.forEach(subscription -> new TimeTask(subscription).start());
        });
        if (Context.configuration.getEnable()) {
            CronUtil.start();
            Context.logger.info("开始监听直播间列表");
        } else {
            Context.logger.info("使用/set start指令开始监听");
        }
    }

    private void init() {
        initProperties();
        //读取订阅数据
        loadSubscribeData();
        //读取配置
        loadConfig();
        //注册命令
        registerCommand();

    }

    private void initProperties() {
        DataProperties.subscribeData = resolveDataFile(DataProperties.subscribeDataName);
        Context.configData = resolveConfigFile(Context.configDataName);
        Context.logger = getLogger();
    }

    private void registerCommand() {
        CommandManager.INSTANCE.registerCommand(new SettingCommand(),false);
        CommandManager.INSTANCE.registerCommand(new SubscribeCommand(),false);
    }


    private void loadConfig() {
        configOperator.loadConfig(getDescription().getVersion());
    }


    private void loadSubscribeData() {
        dataOperator.loadSubscribeData(DataProperties.subscribeData);
    }

}