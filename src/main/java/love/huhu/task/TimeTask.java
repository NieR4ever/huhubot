package love.huhu.task;

import love.huhu.operator.MessageChainOperator;
import love.huhu.pojo.Subscription;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.message.code.MiraiCode;
import net.mamoe.mirai.message.data.MessageChain;

import java.util.Arrays;

/**
 * 开播是的发送通知线程
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
            //加载动态信息
            MessageChainOperator op = new MessageChainOperator();
            String dynamicMiraiCode = op.processTemplate(subscription);
            MessageChain chain = MiraiCode.deserializeMiraiCode(dynamicMiraiCode);
            Arrays.stream(subscription.getGroups()).forEach(group-> bot.getGroup(Long.parseLong(group)).sendMessage(chain));
        }
    }
}
