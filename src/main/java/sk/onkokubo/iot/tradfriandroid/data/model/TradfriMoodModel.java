package sk.onkokubo.iot.tradfriandroid.data.model;

import com.google.gson.annotations.SerializedName;

import sk.onkokubo.iot.tradfriandroid.TradfriConstants;

/**
 * @author ondrejkubo on 03/06/2017.
 */

public final class TradfriMoodModel extends TradfriEntity {

    @SerializedName(TradfriConstants.ENTITY_TYPE_VALUE_DEFINITIONS)
    private TradfriDeviceValues[] moodValues;

    @SerializedName("9068")
    private byte res9068 = 0;

}
