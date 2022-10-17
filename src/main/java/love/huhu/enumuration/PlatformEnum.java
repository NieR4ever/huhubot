package love.huhu.enumuration;

import love.huhu.api.PlatformApi;
import love.huhu.api.bilibili.BiliBiliApi;
import love.huhu.api.douyu.DouyuApi;

/**
 * @Author nwl20
 * @create 2022/10/14 11:03
 */
public enum PlatformEnum {
    BILIBILI(new BiliBiliApi()),
    DOUYU(new DouyuApi());

    PlatformEnum(PlatformApi api) {
        this.api = api;
    }
    public PlatformApi api;
}
