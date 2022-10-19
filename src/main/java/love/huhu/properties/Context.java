package love.huhu.properties;

import love.huhu.pojo.Configuration;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.utils.MiraiLogger;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author
 * @create 2022/9/4 15:57
 */
public class Context {
    /**
     * 配置
     */
    public static Configuration configuration;

    public static final String configDataName = "config.setting";
    public static File configData = null;
    public static MiraiLogger logger = null;
}
