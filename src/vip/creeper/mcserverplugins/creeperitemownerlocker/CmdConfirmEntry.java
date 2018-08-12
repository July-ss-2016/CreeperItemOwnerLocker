package vip.creeper.mcserverplugins.creeperitemownerlocker;

/**
 * Created by July on 2018/2/10.
 */
class CmdConfirmEntry {
    private String confirmedCmd;
    private boolean isConfirmed;

    CmdConfirmEntry(String confirmedCmd) {
        this.isConfirmed = false;
        this.confirmedCmd = confirmedCmd;
    }

    boolean isConfirmed() {
        return isConfirmed;
    }

    void setConfirmed(boolean b) {
        this.isConfirmed = b;
    }

    String getConfirmedCmd() {
        return confirmedCmd;
    }
}
