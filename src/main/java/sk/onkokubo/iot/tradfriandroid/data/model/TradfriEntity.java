package sk.onkokubo.iot.tradfriandroid.data.model;

import com.google.gson.annotations.SerializedName;

import sk.onkokubo.iot.tradfriandroid.TradfriConstants;

/**
 * @author ondrejkubo on 31/05/2017.
 */

public class TradfriEntity implements TradfriEntityContainer {

    @SerializedName(TradfriConstants.NAME)
    protected String name;

    @SerializedName(TradfriConstants.DEVICE_TIMESTAMP)
    protected Long deviceTimestamp;

    @SerializedName(TradfriConstants.ONOFF)
    protected Long onOff;


}
