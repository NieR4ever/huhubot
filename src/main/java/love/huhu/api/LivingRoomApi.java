package love.huhu.api;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

/**
 * @Author
 * @create 2022/8/28 16:42
 * 查询开播信息
 */
public class LivingRoomApi {
    private static String url = "https://api.live.bilibili.com/room/v1/Room/get_info?";

    public boolean getLiveStatus(String roomId) {
        String s = HttpUtil.get(url + "room_id=" + roomId);
        JSONObject object = JSONUtil.parseObj(s);
        if (object.getInt("code") == 1) {
            System.err.println("live room{"+roomId+"} not exist,msg{"+object.getStr("msg")+"}");
            return false;
        }
        Object data = object.getObj("data");
        JSONObject data1 = JSONUtil.parseObj(data);
        Integer liveStatus = data1.getInt("live_status");
        System.err.println("room {"+roomId+"} 's live status is {"+liveStatus+"}");
        return liveStatus == 1;
    }

}
