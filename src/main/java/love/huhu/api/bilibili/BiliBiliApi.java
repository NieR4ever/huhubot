package love.huhu.api.bilibili;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import love.huhu.api.Broadcast;
import love.huhu.api.PlatformApi;
import love.huhu.pojo.Subscription;

/**
 * @Author
 * @create 2022/8/28 16:42
 * 查询开播信息
 */
public class BiliBiliApi implements PlatformApi {
    private static String url = "https://api.live.bilibili.com/room/v1/Room/get_info?";

    public boolean getLiveStatus(Subscription subscription) {
        String roomId = subscription.getRoomId();
        String s = HttpUtil.get(url + "room_id=" + roomId);
        JSONObject object = JSONUtil.parseObj(s);
        if (object.getInt("code") == 1) {
            System.err.println("live room{"+roomId+"} not exist,msg{"+object.getStr("msg")+"}");
            return false;
        }
        Object data = object.getObj("data");
        packageData(data,subscription);
//        System.err.println("room {"+roomId+"} 's live status is {"+subscription.getBroadcast().getLiveStatus()+"}");
        return subscription.getBroadcast().getLiveStatus() == 1;
    }

    private void packageData(Object data,Subscription subscription) {

        JSONObject info = JSONUtil.parseObj(data);
        String uid = info.getStr("uid");
        String roomId = info.getStr("room_id");
        Long attention = info.getLong("attention");
        Long online = info.getLong("online");
        String description = info.getStr("description");
        String areaName = info.getStr("area_name");
        String backgroundUrl = info.getStr("background");
        String title = info.getStr("title");
        String userCoverUrl = info.getStr("user_cover");
        String keyFrameUrl = info.getStr("keyframe");
        String liveTime = info.getStr("live_time");
        String tags = info.getStr("tags");
        Integer liveStatus = info.getInt("live_status");

        Broadcast broadcast = subscription.getBroadcast();
        broadcast.setUid(uid);
        broadcast.setRoomId(roomId);
        broadcast.setAttention(attention);
        broadcast.setOnline(online);
        broadcast.setDescription(description);
        broadcast.setAreaName(areaName);
        broadcast.setBackgroundUrl(backgroundUrl);
        broadcast.setTitle(title);
        broadcast.setUserCoverUrl(userCoverUrl);
        broadcast.setKeyframeUrl(keyFrameUrl);
        broadcast.setLiveStatus(liveStatus);
        broadcast.setLiveTime(liveTime);
        broadcast.setTags(tags);
    }
}
