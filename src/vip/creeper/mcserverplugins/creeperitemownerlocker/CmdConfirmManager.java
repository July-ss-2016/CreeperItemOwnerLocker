package vip.creeper.mcserverplugins.creeperitemownerlocker;

import java.util.HashMap;

/**
 * Created by July on 2018/2/8.
 */
class CmdConfirmManager {
    private HashMap<String, CmdConfirmRequests> requests;

    CmdConfirmManager() {
        requests = new HashMap<>();
    }

    void putRequest(String playerName, CmdConfirmType confirmType, String cmd) {
        CmdConfirmRequests request;

        if (requests.containsKey(playerName)) {
            request = requests.get(playerName);
        } else {
            request = new CmdConfirmRequests();
        }

        request.putRequest(confirmType, cmd);
        requests.put(playerName, request);
    }

    boolean isExistsRequest(String playerName, CmdConfirmType confirmType) {
        return requests.containsKey(playerName) && requests.get(playerName).isExistsRequest(confirmType);

    }

    CmdConfirmRequests getRequests(String playerName) {
        return requests.get(playerName);
    }
}
