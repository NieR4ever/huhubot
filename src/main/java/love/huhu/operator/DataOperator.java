package love.huhu.operator;

import cn.hutool.core.io.FileUtil;
import cn.hutool.setting.Setting;
import love.huhu.pojo.Subscription;
import love.huhu.properties.DataProperties;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;

/**
 * 订阅信息文件操作类
 * @Author nwl20
 * @create 2022/10/19 12:10
 */
public class DataOperator {
    /**
     * 读取或重新载入订阅信息
     * @param file 订阅信息文件
     */
    public void loadSubscribeData(File file) {
        if (!file.exists()) {
            FileUtil.touch(file);
        }
        /*
            对于文件中每一个订阅，查看他在现订阅列表中的存在
            如果没有，加入订阅
            反之将旧订阅的广播信息给新订阅
            再把新订阅放入旧订阅列表中
         */
        Setting setting = new Setting(file, StandardCharsets.UTF_8, false);
        HashSet<Subscription> subscriptions = new HashSet<>();
        for (String group : setting.getGroups()) {
            Subscription subscription = Subscription.convert(setting,group);
            if (!DataProperties.subscriptions.add(subscription) || !subscriptions.add(subscription)) {
                DataProperties.subscriptions.stream()
                        .filter(subscription::equals)
                        .forEach(oldSub -> {
                            subscription.setBroadcast(oldSub.getBroadcast());
                            subscriptions.add(subscription);
                        });
            }

        }
        //去除文件中没有的订阅
        DataProperties.subscriptions.retainAll(subscriptions);
    }
}
