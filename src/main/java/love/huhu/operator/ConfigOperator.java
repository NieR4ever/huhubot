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
 * @Author nwl20
 * @create 2022/10/19 12:21
 */
public class ConfigOperator {
    public void loadConfig(SemVersion pluginVersion) {
        File file = Context.configData;
        if (!file.exists()) {
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
            updateConfig(setting, getNewSetting());
            Context.logger.info("配置文件更新成功");
        }
        Configuration configuration = Configuration.convert(setting);
        Context.configuration = configuration;
        //更新权限
        BotMain.INSTANCE.registerPermission();
    }

    private void updateConfig(Setting setting, Setting newSetting) {
        setting.addSetting(newSetting);
        setting.store();
    }

    private Setting getNewSetting() {
        Setting setting = new Setting();
//        setting.set("bilibili","b站,bili,B站");
//        setting.set("douyu","斗鱼,douyu");
        setting.set("version","1.1.0");
        return setting;
    }
}
