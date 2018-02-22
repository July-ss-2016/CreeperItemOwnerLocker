package vip.creeper.mcserverplugins.creeperitemownerlocker;

/**
 * Created by July on 2018/2/10.
 */
public class CmdConfirmEntry {
    private String confirmedCmd;
    private boolean isConfirmed;

    public CmdConfirmEntry(String confirmedCmd) {
        this.isConfirmed = false;
        this.confirmedCmd = confirmedCmd;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean b) {
        this.isConfirmed = b;
    }

    public String getConfirmedCmd() {
        return confirmedCmd;
    }
}
