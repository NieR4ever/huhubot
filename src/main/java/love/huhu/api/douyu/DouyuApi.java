package love.huhu.api.douyu;

import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import love.huhu.api.Broadcast;
import love.huhu.api.PlatformApi;
import love.huhu.pojo.Subscription;

/**
 * @Author nwl20
 * @create 2022/10/17 9:30
 */
public class DouyuApi implements PlatformApi {

    private static String url = "https://web.sinsyth.com/lxapi/douyujx.x?rid=";
    private static String roomUrl = "https://www.douyu.com/";
    @Override
    public boolean getLiveStatus(Subscription subscription) {
        String roomId = subscription.getRoomId();
        String res = HttpUtil.get(url + roomId);
        JSONObject result = JSONUtil.parseObj(res);
        if (result.getStr("state").equalsIgnoreCase("no")) {
            subscription.getBroadcast().setLiveStatus(0);
        } else if (result.getStr("state").equalsIgnoreCase("success")) {
            Broadcast broadcast = subscription.getBroadcast();
            broadcast.setLiveStatus(1);
            JSONObject data = result.getJSONObject("Rendata").getJSONObject("data");
            JSONObject dataToUse = JSONUtil.parseObj(UnicodeUtil.toString(data.toString()).replaceAll("\\\\", ""));
            packageData(dataToUse,subscription);
        }
        return subscription.getBroadcast().getLiveStatus() == 1;
    }

    private void packageData(JSONObject dataToUse, Subscription subscription) {
        Broadcast broadcast = subscription.getBroadcast();
        String roomId = dataToUse.getStr("vid");
        String title = dataToUse.getStr("roomName");
        String keyframeUrl = dataToUse.getStr("roomimg");
        String description = dataToUse.getStr("roominfo");
        broadcast.setRoomId(roomId);
        broadcast.setTitle(title);
        broadcast.setKeyframeUrl(keyframeUrl);
        broadcast.setDescription(description);
        broadcast.setRoomUrl(roomUrl+roomId);
    }
}
