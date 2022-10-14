package love.huhu.properties;

import love.huhu.api.LivingRoomApi;
import love.huhu.pojo.Subscription;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author
 * @create 2022/9/5 8:55
 */
public class DataProperties {
    public static List<Subscription> subscriptions = new ArrayList<>();
    public static LivingRoomApi api = new LivingRoomApi();
}
