package debug.infos;
public class SoftwareVersion {
    private Integer major;
    private Integer minor;
    private Integer patch;
    private Integer flag;

    public SoftwareVersion(Integer major, Integer minor, Integer patch, Integer flag) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.flag = flag;
    }

    public Integer major() {
        return major;
    }

    public Integer minor() {
        return minor;
    }

    public Integer patch() {
        return patch;
    }

    public Integer flag() {
        return flag;
    }

    public String toString() {
        return "v" + major + "." + minor + "." + patch + ":" + flag;
    }
}
