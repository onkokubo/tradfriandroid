package sk.onkokubo.iot.tradfriandroid.data.model;

/**
 * @author ondrejkubo on 28/04/2017.
 */

public class TradfriRequestModel {

    protected String mUri = "";
    protected String mMethod = "";
    protected byte[] mPayload = {};

    public final void setUri(String uri) {
        mUri = uri;
    }

    public final String getUri() {
        return mUri;
    }

    public final void setMethod(String method) {
        mMethod = method;
    }

    public final String getMethod() {
        return mMethod;
    }

    public final void setPayload(byte[] payload) {
        mPayload = payload;
    }

    public final byte[] getPayload() {
        return mPayload;
    }

}
