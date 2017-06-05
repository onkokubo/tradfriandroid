package sk.onkokubo.iot.tradfriandroid.caller;

/**
 * @author ondrejkubo on 29/05/2017.
 */

public final class TradfriEndpointIdentifier {

    private final String mServer;
    private final Integer mPort;
    private final String mSecret;

    public TradfriEndpointIdentifier(final String server, final Integer port, final String secret) {
        mServer = server;
        mPort = port;
        mSecret = secret;
    }

    public final String getServer() {
        return mServer;
    }

    public final Integer getPort() {
        return mPort;
    }

    public final String getSecret() {
        return mSecret;
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj instanceof TradfriEndpointIdentifier) {
            if (((TradfriEndpointIdentifier) obj).getServer().equals(mServer)
                    && ((TradfriEndpointIdentifier) obj).getPort() == mPort
                    && ((TradfriEndpointIdentifier) obj).getSecret() == mSecret) {
                return true;
            }
        }
        return false;
    }

}