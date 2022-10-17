package love.huhu.api;

/**
 * @Author nwl20
 * @create 2022/10/14 9:57
 */
public class Broadcast {
    private String uid;
    private String roomId;
    private Long attention;
    private Long online;
    private String description;
    private Integer liveStatus;
    private String areaName;
    private String backgroundUrl;
    private String title;
    private String userCoverUrl;
    private String keyframeUrl;
    private String liveTime;
    private String tags;

    public Broadcast() {
    }

    public Broadcast(String uid, String roomId, Long attention, Long online, String description, Integer liveStatus, String areaName, String backgroundUrl, String title, String userCoverUrl, String keyframeUrl, String liveTime, String tags) {
        this.uid = uid;
        this.roomId = roomId;
        this.attention = attention;
        this.online = online;
        this.description = description;
        this.liveStatus = liveStatus;
        this.areaName = areaName;
        this.backgroundUrl = backgroundUrl;
        this.title = title;
        this.userCoverUrl = userCoverUrl;
        this.keyframeUrl = keyframeUrl;
        this.liveTime = liveTime;
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Broadcast{" +
                "uid='" + uid + '\'' +
                ", roomId='" + roomId + '\'' +
                ", attention=" + attention +
                ", online=" + online +
                ", description='" + description + '\'' +
                ", liveStatus=" + liveStatus +
                ", areaName='" + areaName + '\'' +
                ", backgroundUrl='" + backgroundUrl + '\'' +
                ", title='" + title + '\'' +
                ", userCoverUrl='" + userCoverUrl + '\'' +
                ", keyframeUrl='" + keyframeUrl + '\'' +
                ", liveTime='" + liveTime + '\'' +
                ", tags='" + tags + '\'' +
                '}';
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public Long getAttention() {
        return attention;
    }

    public void setAttention(Long attention) {
        this.attention = attention;
    }

    public Long getOnline() {
        return online;
    }

    public void setOnline(Long online) {
        this.online = online;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLiveStatus() {
        return liveStatus;
    }

    public void setLiveStatus(Integer liveStatus) {
        this.liveStatus = liveStatus;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserCoverUrl() {
        return userCoverUrl;
    }

    public void setUserCoverUrl(String userCoverUrl) {
        this.userCoverUrl = userCoverUrl;
    }

    public String getKeyframeUrl() {
        return keyframeUrl;
    }

    public void setKeyframeUrl(String keyframeUrl) {
        this.keyframeUrl = keyframeUrl;
    }

    public String getLiveTime() {
        return liveTime;
    }

    public void setLiveTime(String liveTime) {
        this.liveTime = liveTime;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
