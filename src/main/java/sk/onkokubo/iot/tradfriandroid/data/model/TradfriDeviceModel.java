package sk.onkokubo.iot.tradfriandroid.data.model;

import com.google.gson.annotations.SerializedName;

import sk.onkokubo.iot.tradfriandroid.TradfriConstants;

/**
 * @author ondrejkubo on 04/06/2017.
 */

public final class TradfriDeviceModel extends TradfriEntity {


    @SerializedName(TradfriConstants.LAST_CHANGET_TIMESTAMP)
    private Long lastChangedTimestamp;

    @SerializedName(TradfriConstants.LIGHT)
    private TradfriDeviceValues[] deviceValues;

    @SerializedName(TradfriConstants.ENTITY_TYPE_REMOTE_VALUES)
    private TradfriDeviceValues[] remoteValues;

    @SerializedName(TradfriConstants.FIELD_DESCRIPTION)
    private TradfriDeviceDescription deviceDescription;

}
