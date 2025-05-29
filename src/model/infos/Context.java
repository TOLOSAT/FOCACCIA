package model.infos;
public class Context {
    private SoftwareVersion version;
    private SoftwareState state;
    private Integer bootCount;
    private Integer failedBootCount;

    public Context(SoftwareVersion version, SoftwareState state, Integer bootCount, Integer failedBootCount) {
        this.version = version;
        this.state = state;
        this.bootCount = bootCount;
        this.failedBootCount = failedBootCount;
    }

    public SoftwareVersion getVersion() {
        return version;
    }

    public SoftwareState getState() {
        return state;
    }

    public Integer getBootCount() {
        return bootCount;
    }

    public Integer getFailedBootCount() {
        return failedBootCount;
    }
}
