package debug.infos;
public class Call {
    private Integer lr;
    private Integer fp;

    public Call(Integer lr, Integer fp) {
        this.lr = lr;
        this.fp = fp;
    }

    public Integer lr() {
        return lr;
    }

    public Integer fp() {
        return fp;
    }
}
