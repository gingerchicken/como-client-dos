package net.como.client.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.util.UUID;

public class APIWrapper {
    public static UUID getPlayerUUID(String username) {
        String uuid = JsonParser.parseString(HttpUtils.getInstance().get("https://playerdb.co/api/player/minecraft/" + username).sendString()).getAsJsonObject()
                .getAsJsonObject("data").getAsJsonObject("player").get("id").getAsString();

        return UUID.fromString(uuid);
    }

    public static JsonArray getMinefortServerList(int limit) {
        String resp = HttpUtils.getInstance().post("https://api.minefort.com/v1/servers/list").bodyJson("{\"pagination\": { \"skip\": 0, \"limit\": "+ limit +" }, \"sort\": { \"field\": \"players.online\", \"order\": \"desc\" }}").sendString();
        return JsonParser.parseString(resp).getAsJsonObject().get("result").getAsJsonArray();
    }
}
