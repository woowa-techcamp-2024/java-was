package http.startline;

public abstract class StartLine {

    private final String version;

    protected StartLine(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public abstract String toString();
}
