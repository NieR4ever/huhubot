package love.huhu.operator;

import cn.hutool.core.io.FileUtil;
import cn.hutool.setting.Setting;
import love.huhu.pojo.Subscription;
import love.huhu.properties.DataProperties;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

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
        Setting setting = new Setting(file, StandardCharsets.UTF_8, true);
        for (String group : setting.getGroups()) {
            Subscription subscription = Subscription.convert(setting,group);
            //保留当前订阅信息的同时读取文件订阅
            if (!DataProperties.subscriptions.add(subscription)) {
                //订阅信息中已有该订阅名，使用文件订阅覆盖已有订阅
                Iterator<Subscription> iterator = DataProperties.subscriptions.iterator();
                while (iterator.hasNext()) {
                    Subscription oldSub = iterator.next();
                    subscription.setBroadcast(oldSub.getBroadcast());
                    break;
                }
                //移除添加
                System.out.println("移除前集合的长度："+DataProperties.subscriptions.size());
                DataProperties.subscriptions.remove(subscription);
                System.out.println("当前集合的长度："+DataProperties.subscriptions.size());
                DataProperties.subscriptions.add(subscription);
            }
        }
    }
}
