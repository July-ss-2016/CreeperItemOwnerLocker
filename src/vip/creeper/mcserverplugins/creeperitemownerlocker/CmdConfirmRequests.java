package vip.creeper.mcserverplugins.creeperitemownerlocker;

import java.util.HashMap;

/**
 * Created by July on 2018/2/8.
 */
public class CmdConfirmRequests {
    private HashMap<CmdConfirmType, CmdConfirmEntry> requests;

    public CmdConfirmRequests() {
        requests = new HashMap<>();
    }

    public void putRequest(CmdConfirmType confirmType, String cmd) {
        requests.put(confirmType, new CmdConfirmEntry(cmd));
    }

    public boolean isExistsRequest(CmdConfirmType confirmType) {
        return requests.containsKey(confirmType);
    }

    public CmdConfirmEntry getCmdConfirmEntry(CmdConfirmType confirmType) {
        return requests.get(confirmType);
    }

    public void removeRequest(CmdConfirmType confirmType) {
        requests.remove(confirmType);
    }
}
