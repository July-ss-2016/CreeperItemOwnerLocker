package vip.creeper.mcserverplugins.creeperitemownerlocker;

import java.util.HashMap;

/**
 * Created by July on 2018/2/8.
 */
class CmdConfirmRequests {
    private HashMap<CmdConfirmType, CmdConfirmEntry> requests;

    CmdConfirmRequests() {
        requests = new HashMap<>();
    }

    void putRequest(CmdConfirmType confirmType, String cmd) {
        requests.put(confirmType, new CmdConfirmEntry(cmd));
    }

    boolean isExistsRequest(CmdConfirmType confirmType) {
        return requests.containsKey(confirmType);
    }

    CmdConfirmEntry getCmdConfirmEntry(CmdConfirmType confirmType) {
        return requests.get(confirmType);
    }

    void removeRequest(CmdConfirmType confirmType) {
        requests.remove(confirmType);
    }
}
