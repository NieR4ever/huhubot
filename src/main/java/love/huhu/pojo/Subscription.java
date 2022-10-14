package love.huhu.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author
 * @create 2022/9/5 8:55
 */
public class Subscription {
    public Subscription(String platform, String roomId, List<Long> groups) {
        this.platform = platform;
        this.roomId = roomId;
        this.groups = groups;
        this.isStreaming = false;
        this.cancel = false;
    }
    public Subscription(String platform, String roomId, String[] groups) {
        this(platform,roomId, (List<Long>) null);
        ArrayList<Long> list = new ArrayList<>();
        for (String group : groups) {
            list.add(Long.parseLong(group));
        }
        this.groups = list;
    }

    private String platform;

    private String roomId;

    private List<Long> groups;

    private boolean isStreaming;

    @Override
    public String toString() {
        return "Subscription{" +
                "platform='" + platform + '\'' +
                ", roomId='" + roomId + '\'' +
                ", groups=" + groups +
                ", isStreaming=" + isStreaming +
                ", cancel=" + cancel +
                '}';
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public List<Long> getGroups() {
        return groups;
    }

    public void setGroups(List<Long> groups) {
        this.groups = groups;
    }

    public boolean isStreaming() {
        return isStreaming;
    }

    public void setStreaming(boolean streaming) {
        isStreaming = streaming;
    }

    public boolean isCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    private boolean cancel;
}
