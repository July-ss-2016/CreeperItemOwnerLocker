package vip.creeper.mcserverplugins.creeperitemownerlocker.managers;

import vip.creeper.mcserverplugins.creeperitemownerlocker.CmdConfirmRequests;
import vip.creeper.mcserverplugins.creeperitemownerlocker.CmdConfirmType;

import java.util.HashMap;

/**
 * Created by July on 2018/2/8.
 */
public class CmdConfirmManager {
    private HashMap<String, CmdConfirmRequests> requests;

    public CmdConfirmManager() {
        requests = new HashMap<>();
    }

    public void putRequest(String playerName, CmdConfirmType confirmType, String cmd) {
        CmdConfirmRequests request;

        if (requests.containsKey(playerName)) {
            request = requests.get(playerName);
        } else {
            request = new CmdConfirmRequests();
        }

        request.putRequest(confirmType, cmd);
        requests.put(playerName, request);
    }

    public boolean isExistsRequest(String playerName, CmdConfirmType confirmType) {
        if (!requests.containsKey(playerName)) {
            return false;
        }

        return requests.get(playerName).isExistsRequest(confirmType);
    }

    public CmdConfirmRequests getRequests(String playerName) {
        return requests.get(playerName);
    }
}
