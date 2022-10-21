package love.huhu.operator;

import cn.hutool.core.io.FileUtil;
import cn.hutool.setting.Setting;
import love.huhu.BotMain;
import love.huhu.pojo.Configuration;
import love.huhu.properties.Context;
import net.mamoe.mirai.console.util.SemVersion;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * 配置操作类
 * @Author nwl20
 * @create 2022/10/19 12:21
 */
public class ConfigOperator {
    /**
     * 读取或重新载入配置
     * @param pluginVersion 插件版本号
     */
    public void loadConfig(SemVersion pluginVersion) {
        //获取到配置文件
        File file = Context.configData;
        if (!file.exists()) {
            //初次启动，创建文件并写入默认参数
            FileUtil.touch(file);
            Setting setting = new Setting(file, StandardCharsets.UTF_8, true);
            setting.set("enable", "true");
            setting.set("admins", "123456");
            setting.set("bilibili", "b站,bili,bilibili,B站");
            setting.set("douyu", "斗鱼,douyu");
            setting.set("version", "1.0");
            setting.store();
        }
        Setting setting = new Setting(file, StandardCharsets.UTF_8, true);
        //检查配置版本
        String version = setting.getStr("version");
        SemVersion configVersion = SemVersion.parse(version);
        if (pluginVersion.compareTo(configVersion) > 0) {
            //更新配置
            Context.logger.info("当前插件版本{" + pluginVersion + "},当前配置版本{" + configVersion + "}");
            updateConfig(setting);
        }
        Configuration configuration = Configuration.convert(setting);
        Context.configuration = configuration;
        //更新权限
        BotMain.INSTANCE.registerPermission();
    }

    /**
     * 更新配置信息
     * @param setting 用户的配置
     */
    private void updateConfig(Setting setting) {
        Setting newSetting = getNewSetting();
        setting.remove("version");
        newSetting.addSetting(setting);
        //写入文件
        newSetting.store(setting.getSettingPath());
        Context.logger.info("更新配置成功");
    }

    private Setting getNewSetting() {
        Setting setting = new Setting();
        setting.set("enable", "true");
        setting.set("admins", "123456");
        setting.set("bilibili", "b站,bili,bilibili,B站");
        setting.set("douyu", "斗鱼,douyu");
        setting.set("version", "1.2.0");
        return setting;
    }

}
