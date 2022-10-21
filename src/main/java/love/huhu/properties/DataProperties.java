package love.huhu.properties;

import love.huhu.pojo.Subscription;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author
 * @create 2022/9/5 8:55
 */
public class DataProperties {
    //订阅列表
    public static Set<Subscription> subscriptions = new HashSet<>();
    //存储订阅信息的文件
    public static File subscribeData = null;
    //存储订阅信息的文件
    public static final String subscribeDataName = "subscriptions.setting";
}
