package love.huhu;

import cn.hutool.cron.CronUtil;
import love.huhu.command.SettingCommand;
import love.huhu.command.SubscribeCommand;
import love.huhu.operator.ConfigOperator;
import love.huhu.operator.DataOperator;
import love.huhu.properties.Context;
import love.huhu.properties.DataProperties;
import love.huhu.task.TimeTask;
import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.permission.AbstractPermitteeId;
import net.mamoe.mirai.console.permission.PermissionId;
import net.mamoe.mirai.console.permission.PermissionService;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.BotOnlineEvent;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * bot插件启动类
 */
public final class BotMain extends JavaPlugin {
    public static final BotMain INSTANCE = new BotMain();
    //操作data文件夹
    private DataOperator dataOperator = new DataOperator();
    //操作config文件夹
    private ConfigOperator configOperator = new ConfigOperator();
    //插件信息配置
    private BotMain() {
        super(new JvmPluginDescriptionBuilder( // 必要属性
                "love.huhu.bot", // id
                "1.2.0" // version
        ).author("无糖雪碧")
                .name("提醒开播机器人")
                .info("为qq群提供提醒主播开播的服务")
                .build());
//        super(JvmPluginDescription.loadFromResource());
    }

    @Override
    public void onEnable() {
        init();
        GlobalEventChannel.INSTANCE.parentScope(BotMain.INSTANCE).subscribeOnce(BotOnlineEvent.class, event -> {
            //开始监听直播间
            listenBroadcast();
        });

    }

    public void registerPermission() {
        //将配置中的管理员字符数组转成权限被许可人列表
        List<AbstractPermitteeId.ExactUser> users = Arrays.stream(Context.configuration.getAdmins())
                .map(admin-> new AbstractPermitteeId.ExactUser(Long.parseLong(admin)))
                .collect(Collectors.toList());
        //获取插件对应的权限
        PermissionId permissionId = this.getParentPermission().getId();
        //将为注册的管理员设置权限
        users.forEach(user->{
            if (!PermissionService.hasPermission(user,permissionId)) {
                PermissionService.permit(user,permissionId);
            }
        });
    }


    /**
     * 监听直播间
     */
    private void listenBroadcast() {
        //计划任务，每分钟对每一个订阅查询一次直播间信息
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
        //初始化配置,数据文件路径
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