package love.huhu.operator;

import cn.hutool.core.io.FileUtil;
import cn.hutool.setting.Setting;
import love.huhu.pojo.Subscription;
import love.huhu.properties.DataProperties;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author nwl20
 * @create 2022/10/19 12:10
 */
public class DataOperator {
    public void loadSubscribeData(File file) {
        if (!file.exists()) {
            FileUtil.touch(file);
        }
        Setting setting = new Setting(file, StandardCharsets.UTF_8, true);
        for (String group : setting.getGroups()) {
            Subscription subscription = Subscription.convert(setting,group);
            DataProperties.subscriptions.add(subscription);
        }
    }
}
