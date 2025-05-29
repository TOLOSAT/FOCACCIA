package model.infos;
public class SavedRegisters {
    private Integer[] r;
    private Integer r12;
    private Integer lr;
    private Integer pc;
    private Integer xpsr;

    public SavedRegisters(Integer r0, Integer r1, Integer r2, Integer r3, Integer r12, Integer lr, Integer pc, Integer xpsr) {
        this.r = new Integer[4];
        this.r[0] = r0;
        this.r[1] = r1;
        this.r[2] = r2;
        this.r[3] = r3;
        this.r12 = r12;
        this.lr = lr;
        this.pc = pc;
        this.xpsr = xpsr;
    }

    public Integer[] r() {
        return r;
    }

    public Integer r12() {
        return r12;
    }

    public Integer lr() {
        return lr;
    }

    public Integer pc() {
        return pc;
    }

    public Integer xpsr() {
        return xpsr;
    }
}