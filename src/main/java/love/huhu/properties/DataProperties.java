package love.huhu.properties;

import love.huhu.api.bilibili.BiliBiliApi;
import love.huhu.pojo.Subscription;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author
 * @create 2022/9/5 8:55
 */
public class DataProperties {
    public static Set<Subscription> subscriptions = new HashSet<>();
    public static BiliBiliApi api = new BiliBiliApi();
}
