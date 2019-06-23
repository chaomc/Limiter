package site.higgs.limiter;

public class LimiterRuntimeExeception extends RuntimeException {

    public LimiterRuntimeExeception() {
    }

    public LimiterRuntimeExeception(String message) {
        super(message);
    }

    public LimiterRuntimeExeception(String message, Throwable cause) {
        super(message, cause);
    }

    public LimiterRuntimeExeception(Throwable cause) {
        super(cause);
    }

    public LimiterRuntimeExeception(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
