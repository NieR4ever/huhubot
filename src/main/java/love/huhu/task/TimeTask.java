package love.huhu.task;

import love.huhu.pojo.Subscription;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.message.code.MiraiCode;
import net.mamoe.mirai.message.data.MessageChain;

import java.util.Arrays;

/**
 * @Author nwl20
 * @create 2022/10/16 16:08
 */
public class TimeTask extends Thread{
    public TimeTask(Subscription subscription) {
        this.subscription = subscription;
    }
    private Subscription subscription;
    @Override
    public void run() {
        boolean oldLiveStatus = subscription.getBroadcast().getLiveStatus() != null && subscription.getBroadcast().getLiveStatus() == 1;
        boolean newLiveStatus = subscription.getPlatform().api.getLiveStatus(subscription);
        if (!oldLiveStatus && newLiveStatus) {
            //之前未开播,现在开播了
            //通知群
            Bot bot = Bot.getInstance(subscription.getBotNumber());
            MessageChain chain = MiraiCode.deserializeMiraiCode(subscription.getNotifyMiraiCode());
            Arrays.stream(subscription.getGroups()).forEach(group-> bot.getGroup(Long.parseLong(group)).sendMessage(chain));
        }
    }
}
