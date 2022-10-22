package love.huhu.command;

import cn.hutool.core.util.StrUtil;
import cn.hutool.cron.CronUtil;
import kotlin.sequences.Sequence;
import love.huhu.BotMain;
import love.huhu.operator.ConfigOperator;
import love.huhu.properties.Context;
import net.mamoe.mirai.console.command.CommandOwner;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JCompositeCommand;
import net.mamoe.mirai.console.permission.AbstractPermitteeId;
import net.mamoe.mirai.console.permission.Permission;
import net.mamoe.mirai.console.permission.PermissionId;
import net.mamoe.mirai.console.permission.PermissionService;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * @Author
 * @create 2022/9/5 8:24
 */
public final class SettingCommand extends JCompositeCommand {

    private static final String parentPermissionStr = "love.huhu.bot:";

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
        registerPermission();
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
                .append("解释：重新读取配置文件").append("\n")
                .append("--------------\n")
                .append("/set permit <qq号> <权限名> [权限名2]").append("\n")
                .append("解释：授予某人插件中的权限").append("\n")
                .append("--------------\n")
                .append("/set cancel <qq号> <权限名>").append("\n")
                .append("解释：取消某人插件中的权限").append("\n")
                .append("--------------\n")
                .append("/set check <qq号>").append("\n")
                .append("解释：检查某人插件中的权限").append("\n");


        MessageChain chain = builder.build();
        sender.sendMessage(chain);
    }

    @SubCommand({"permit","add","grant","授权","sq"})
    public void permit(CommandSender sender, Long permittee, String... permissions) {
        StringBuffer sb = new StringBuffer();
        AbstractPermitteeId.ExactUser user = new AbstractPermitteeId.ExactUser(permittee);
        Arrays.stream(permissions)
                .map(this::processPermissionStr)
                .map(PermissionId::parseFromString)
                .forEach(permissionId -> {
                    try {
                        //检查操作用户有没有权限
                        User granter = sender.getUser();
                        if (granter != null) {
                            boolean b = PermissionService.testPermission(permissionId, new AbstractPermitteeId.ExactUser(granter.getId()));
                            if (!b) {
                                throw new NoSuchElementException("你没有"+permissionId.getName()+"权限，不能将这条权限赋予别人");
                            }
                        }
                        //赋予权限
                        PermissionService.permit(user,permissionId);
                        sb.append(permittee)
                                .append("-->")
                                .append(permissionId.getName())
                                .append("\n");
                    } catch (NoSuchElementException e) {
                        e.printStackTrace();
                        sb.append(permittee)
                                .append("-x->")
                                .append(permissionId.getName())
                                .append("|")
                                .append(e.getMessage())
                                .append("\n");
                    }
                });
        sender.sendMessage(sb.toString());
    }
    @SubCommand({"cancel","remove","取消","qx"})
    public void cancel(CommandSender sender, Long permittee, String permissionStr) {
        permissionStr = processPermissionStr(permissionStr);
        AbstractPermitteeId.ExactUser user = new AbstractPermitteeId.ExactUser(permittee);

        //获取插件对应的权限
        PermissionId permissionId = PermissionId.parseFromString(permissionStr);
        try{
            PermissionService.cancel(user,permissionId,true);
        } catch (RuntimeException e) {
            e.printStackTrace();
            sender.sendMessage(e.getMessage());
            return;
        }
        sender.sendMessage(StrUtil.format("取消{}的{}权限成功",permittee,permissionId.getName()));
    }
    @SubCommand({"check","检查","jc"})
    public void check(CommandSender sender, Long permittee) {
        AbstractPermitteeId.ExactUser user = new AbstractPermitteeId.ExactUser(permittee);

        Sequence<? extends Permission> permissions = PermissionService.getInstance().getPermittedPermissions(user);
        StringBuilder sb = new StringBuilder();
        sb.append(permittee).append("拥有以下权限").append("\n");
        permissions.iterator().forEachRemaining(permission -> sb.append(permission.getId()).append("\n" ));
        sender.sendMessage(sb.toString());
    }

    private String processPermissionStr(String permissionStr) {
        if (permissionStr.contains(":")) {
            return permissionStr;
        }
        permissionStr = permissionStr.trim().equals("*") ? permissionStr : "command."+permissionStr;
        return parentPermissionStr.concat(permissionStr);
    }
    public void registerPermission() {
        //将配置中的管理员字符数组转成权限被许可人列表
        List<AbstractPermitteeId.ExactUser> users = Arrays.stream(Context.configuration.getAdmins())
                .map(admin-> new AbstractPermitteeId.ExactUser(Long.parseLong(admin)))
                .collect(Collectors.toList());
        //获取插件对应的权限
        PermissionId permissionId = PermissionId.parseFromString(processPermissionStr("*"));
        //将为注册的管理员设置权限
        users.forEach(user->{
            if (!PermissionService.hasPermission(user,permissionId)) {
                PermissionService.permit(user,permissionId);
            }
        });
    }

}
