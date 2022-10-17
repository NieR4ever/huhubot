package love.huhu;

import cn.hutool.core.io.FileUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.setting.Setting;
import love.huhu.command.SettingCommand;
import love.huhu.command.SubscribeCommand;
import love.huhu.pojo.Configuration;
import love.huhu.pojo.Subscription;
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

    private BotMain() {
        super(new JvmPluginDescriptionBuilder( // 必要属性
                "love.huhu.bot", // id
                "1.0" // version
        ).author("无糖雪碧")
                .name("提醒开播机器人")
                .info("为qq群提供提醒主播开播的服务")
                .build());
    }

    @Override
    public void onEnable() {
        init();
        registerPermission();
        GlobalEventChannel.INSTANCE.parentScope(BotMain.INSTANCE).subscribeAlways(BotOnlineEvent.class, event -> {
            //开始监听直播间
            listenBroadcast();
        });
    }

    private void registerPermission() {
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
            System.out.println("正在查询开播状态");
            DataProperties.subscriptions.forEach(subscription -> new TimeTask(subscription).start());
        });
        if (Context.configuration.getEnable()) {
            CronUtil.start();
        }
    }

    private void init() {
        //读取订阅数据
        loadSubscribeData("subscriptions.setting");
        //读取配置
        loadConfig("config.setting");
        //注册命令
        registerCommand();

    }

    private void registerCommand() {
        CommandManager.INSTANCE.registerCommand(new SettingCommand(),false);
        CommandManager.INSTANCE.registerCommand(new SubscribeCommand(),false);
    }


    private void loadConfig(String configFileName) {
        File file = resolveConfigFile(configFileName);
        if (!file.exists()) {
            FileUtil.touch(file);
            Setting setting = new Setting(file, StandardCharsets.UTF_8, true);
            setting.set("enable","true");
            setting.set("admins","123456");
            setting.set("bilibili","b站,bili,bilibili,B站");
            setting.set("douyu","斗鱼,douyu");
            setting.set("version","1.0");
            setting.store();
        }
        Setting setting = new Setting(file, StandardCharsets.UTF_8, true);
        //检查配置版本
        String version = setting.getStr("version");
        SemVersion pluginVersion = this.getDescription().getVersion();
        SemVersion configVersion = SemVersion.parse(version);
        if (pluginVersion.compareTo(configVersion)>0) {
            //更新配置
            getLogger().info("当前插件版本{"+pluginVersion+"},当前配置版本{"+configVersion+"}");
            updateConfig(setting,getNewSetting());
            getLogger().info("配置文件更新成功");
        }
        Configuration configuration = Configuration.convert(setting);
        Context.configuration = configuration;
    }

    private void updateConfig(Setting setting, Setting newSetting) {
        setting.addSetting(newSetting);
        setting.store();
    }

    private Setting getNewSetting() {
        Setting setting = new Setting();
//        setting.set("bilibili","b站,bili,B站");
//        setting.set("douyu","斗鱼,douyu");
//        setting.set("version","1.0");
        return setting;
    }

    private void loadSubscribeData(String dataFileName) {
        File file = resolveDataFile(dataFileName);
        if (!file.exists()) {
            FileUtil.touch(file);
        }
        Setting setting = new Setting(file, StandardCharsets.UTF_8, true);
        Set<Subscription> subscriptions = new HashSet<>();
        for (String group : setting.getGroups()) {
            Subscription subscription = Subscription.convert(setting,group);
            subscriptions.add(subscription);
        }
        DataProperties.subscriptions = subscriptions;
    }

}