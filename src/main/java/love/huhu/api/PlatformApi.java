package love.huhu.api;

import love.huhu.pojo.Subscription;

/**
 * @Author nwl20
 * @create 2022/10/16 16:12
 */
public interface PlatformApi {
    boolean getLiveStatus(Subscription subscription);
}
