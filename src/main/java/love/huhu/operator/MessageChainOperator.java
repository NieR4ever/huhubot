package love.huhu.operator;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import love.huhu.api.Broadcast;
import love.huhu.pojo.Subscription;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.utils.ExternalResource;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 解析通知内容中的模板
 *
 * @Author nwl20
 * @create 2022/10/20 10:56
 */
public class MessageChainOperator {
    private static final String regex_uri = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
    private static final String regex_img = "(bmp|jpg|png|tif|gif|pcx|tga|exif|fpx|svg|psd|cdr|pcd|dxf|ufo|eps|ai|raw|WMF|webp|jpeg)$";
    private Subscription subscription;
    public String processTemplate(Subscription subscription) {
        this.subscription = subscription;
        StringBuilder sb = new StringBuilder(subscription.getNotifyMiraiCode());
        //解析出被${}包含的变量名
        List<String> variables = resolveVariableName(sb);
        //插值
        insertValue(variables, subscription.getBroadcast(), sb);
        return sb.toString();
    }

    @SuppressWarnings("")
    private void insertValue(List<String> names, Broadcast broadcast, StringBuilder message) {
        Object [] args = new String[names.size()];
        //查询变量对应的值
        for (int i = 0; i < names.size(); i++) {
            String value;
            try {
                //获取key对应的value
                Field field = broadcast.getClass().getDeclaredField(names.get(i));
                field.setAccessible(true);
                value = (String) field.get(broadcast);
//                System.err.println(this+"57.value"+i+":"+value);
                if (!StrUtil.isBlank(value)) {
                    args[i] = value;
                } else {
                    args[i] = null;
                }

            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
                args[i] = null;
            }
        }
//        System.err.println(this+"69.args"+Arrays.toString(args));
        //解析想要发送的消息类型
        resolveMessageType(names,(String[]) args);
        String format =StrUtil.format(message.toString(), args);
        message.replace(0,message.length(),format);
    }

    private void resolveMessageType(List<String> names, String[] args) {
        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i).replaceAll(" ", "").toLowerCase();
            if (StrUtil.isBlank(args[i]) || !args[i].matches(regex_uri)) {
                continue;
            }
            String type = HttpRequest.head(args[i]).execute().header("Content-Type");
            if(StrUtil.isBlank(type)) {
                continue;
            }
            if (!type.startsWith("image/") && !type.matches(regex_img)) {
                continue;
            }
            byte[] bytes = HttpUtil.downloadBytes(args[i]);
            //上传图片
            Image image = Contact.uploadImage(Bot.getInstance(subscription.getBotNumber()).getAsFriend(), ExternalResource.create(bytes));
            args[i] = image.serializeToMiraiCode();
        }
    }

    private List<String> resolveVariableName(StringBuilder sb) {
        ArrayList<String> list = new ArrayList<>();
        //匹配${}
        Pattern pattern = Pattern.compile("\\$\\{([a-zA-Z]+)\\}");
        Matcher matcher = pattern.matcher(sb);
        while (matcher.find()) {
            String variableStr = matcher.group();
            int start = matcher.start();
            //替换成{}
            sb.replace(start, start + variableStr.length(), "{}");
            String name = variableStr.substring(2, variableStr.length() - 1);
            list.add(name);
            //修改了sb，重新匹配
            matcher = pattern.matcher(sb);
        }

        return list;
    }

}
