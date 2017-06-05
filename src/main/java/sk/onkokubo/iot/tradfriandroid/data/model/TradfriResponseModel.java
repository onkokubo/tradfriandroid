package sk.onkokubo.iot.tradfriandroid.data.model;

import org.eclipse.californium.core.CoapResponse;

/**
 * @author ondrejkubo on 28/04/2017.
 */

public abstract class TradfriResponseModel {

    protected CoapResponse mResponse = null;

    public final void setResponse(CoapResponse response) {
        mResponse = response;
    }

    public final CoapResponse getmResponse() {
        return mResponse;
    }
}
