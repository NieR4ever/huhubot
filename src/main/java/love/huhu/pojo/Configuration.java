package love.huhu.pojo;

import cn.hutool.setting.Setting;
import love.huhu.enumuration.PlatformEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author nwl20
 * @create 2022/10/14 10:53
 */
public class Configuration {
    private Boolean enable;
    private String[] admins;
    private Map<PlatformEnum,String[]> platformEnumMap;

    public static Configuration convert(Setting setting) {
        Configuration configuration = new Configuration();
        //获取配置
        Boolean enable = setting.getBool("enable");
        String[] admins = setting.getStrings("admins");
        String[] bilibili = setting.getStrings("bilibili");
        String[] douyu = setting.getStrings("douyu");
        //设置配置
        configuration.enable = enable;
        configuration.admins = admins;
        configuration.platformEnumMap = new HashMap<PlatformEnum,String[]>() {{
            put(PlatformEnum.BILIBILI,bilibili);
            put(PlatformEnum.DOUYU,douyu);
        }};
        return configuration;
    }

    public Map<PlatformEnum, String[]> getPlatformEnumMap() {
        return platformEnumMap;
    }

    public void setPlatformEnumMap(Map<PlatformEnum, String[]> platformEnumMap) {
        this.platformEnumMap = platformEnumMap;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String[] getAdmins() {
        return admins;
    }

    public void setAdmins(String[] admins) {
        this.admins = admins;
    }
}
