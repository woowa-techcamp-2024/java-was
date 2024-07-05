package http.startline;

public abstract class StartLine {

    private String version;

    protected StartLine() {
    }

    protected StartLine(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public abstract String toString();
}
