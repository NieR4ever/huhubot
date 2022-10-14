package love.huhu.listener;

import cn.hutool.core.date.DateUtil;
import love.huhu.BotMain;
import love.huhu.properties.ConfigProperties;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.Listener;
import net.mamoe.mirai.event.events.FriendMessageEvent;

import java.util.Date;

/**
 * @Author
 * @create 2022/9/5 8:24
 */
public class SettingListener {

    private Bot bot;
    public SettingListener(Bot bot) {
        System.out.println(bot.getId()+"启动设置监听");
        this.bot = bot;
        buildListener();
    }

    private void buildListener() {
        Listener listener= bot.getEventChannel().parentScope(BotMain.INSTANCE).subscribeAlways(FriendMessageEvent.class, event->{
            String command = event.getMessage().contentToString();
            if (!command.startsWith("#")) return;
            switch (command.replace("#","")) {
                case "启动":
                    enablePlugin(true);
                    event.getSubject().sendMessage("功能已启动");
                    break;
                case "关闭":
                    enablePlugin(false);
                    event.getSubject().sendMessage("命令已关闭");
                default:
                    event.getSubject().sendMessage("命令有误");
            }
        });
    }

    public void enablePlugin(boolean flag) {
        ConfigProperties.enablePlugin = flag;
    }
    public void subscribe(String doNotDisturbStart,String doNotDisturbEnd) {
        Date start = DateUtil.date().toJdkDate();
        Date end = null;
        Date dnds = DateUtil.parse(doNotDisturbStart).toJdkDate();
        Date dnde = DateUtil.parse(doNotDisturbEnd).toJdkDate();

//        Subscription subscription = new Subscription(start, end, dnds, dnde);
//        DataProperties.subscriptions.add(subscription);
    }
}
