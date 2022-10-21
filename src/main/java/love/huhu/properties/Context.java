package love.huhu.properties;

import love.huhu.pojo.Configuration;
import net.mamoe.mirai.utils.MiraiLogger;

import java.io.File;

/**
 * 全局上下文
 * @Author
 * @create 2022/9/4 15:57
 */
public class Context {
    /**
     * 配置
     */
    public static Configuration configuration;
    //配置文件名
    public static final String configDataName = "config.setting";
    //配置文件
    public static File configData = null;
    //日志
    public static MiraiLogger logger = null;
}
