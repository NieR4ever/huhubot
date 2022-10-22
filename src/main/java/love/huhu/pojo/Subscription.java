package love.huhu.pojo;

import cn.hutool.core.convert.Convert;
import cn.hutool.setting.Setting;
import love.huhu.api.Broadcast;
import love.huhu.enumuration.PlatformEnum;
import love.huhu.properties.Context;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * @Author
 * @create 2022/9/5 8:55
 */
public class Subscription {
    public Subscription() {
        this.broadcast = new Broadcast();
    }


    private String name;

    private PlatformEnum platform;

    private String roomId;

    private String[] groups;

    private Broadcast broadcast;

    private Long botNumber;

    private String notifyMiraiCode;

    public Subscription(String name, String platform, String roomId, String[] groups,Long botNumber) {
        this();
        this.name = name;
        this.platform = resolvePlatform(platform);
        this.roomId = roomId;
        this.groups = groups;
        this.botNumber = botNumber;
        this.notifyMiraiCode = "开播了";
    }

    public Subscription(String name) {
        this.name = name;
    }

    private PlatformEnum resolvePlatform(String platform) {
        Map<PlatformEnum, String[]> map = Context.configuration.getPlatformEnumMap();
        PlatformEnum[] result = new PlatformEnum[1];
        map.forEach((key,value)->{
            if (Arrays.asList(value).contains(platform)) {
                result[0] = key;
            }
        });
        return result[0];
    }

    public static Subscription convert(Setting setting,String group) {
        Subscription subscription =  new Subscription();
        String enumStr = setting.getStr("platform", group, "");
        PlatformEnum platform = Convert.toEnum(PlatformEnum.class, enumStr);

        String roomId = setting.getStr("roomId", group, "");
        Long botNumber = setting.getLong("botNumber", group);
        String[] groups = setting.getStrings("groups", group);
        String notifyMiraiCode = setting.getStr("notifyMiraiCode",group,"开播了");
        subscription.name = group;
        subscription.platform = platform;
        subscription.roomId = roomId;
        subscription.groups = groups;
        subscription.botNumber = botNumber;
        subscription.notifyMiraiCode = notifyMiraiCode;
        return subscription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscription that = (Subscription) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "name='" + name + '\'' +
                ", platform=" + platform +
                ", roomId='" + roomId + '\'' +
                ", groups=" + Arrays.toString(groups) +
                ", broadcast=" + broadcast +
                ", botNumber=" + botNumber +
                ", notifyMiraiCode='" + notifyMiraiCode + '\'' +
                '}';
    }

    public String getNotifyMiraiCode() {
        return notifyMiraiCode;
    }

    public void setNotifyMiraiCode(String notifyMiraiCode) {
        this.notifyMiraiCode = notifyMiraiCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlatformEnum getPlatform() {
        return platform;
    }

    public void setPlatform(PlatformEnum platform) {
        this.platform = platform;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String[] getGroups() {
        return groups;
    }

    public void setGroups(String[] groups) {
        this.groups = groups;
    }

    public Long getBotNumber() {
        return botNumber;
    }

    public void setBotNumber(Long botNumber) {
        this.botNumber = botNumber;
    }

    public Broadcast getBroadcast() {
        return broadcast;
    }

    public void setBroadcast(Broadcast broadcast) {
        this.broadcast = broadcast;
    }
}
