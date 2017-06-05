package sk.onkokubo.iot.tradfriandroid.data.model;

import com.google.gson.annotations.SerializedName;

import sk.onkokubo.iot.tradfriandroid.TradfriConstants;

/**
 * @author ondrejkubo on 31/05/2017.
 * 3311":[
 *     {
 *         "9003":0,
 *         "5850":0,
 *         "5707":0,
 *         "5851":12,
 *         "5708":0,
 *         "5706":"efd275",
 *         "5709":33135,
 *         "5710":27211,
 *         "5711":454
 *     }
 * ]
 */

public class TradfriDeviceValues {

    @SerializedName(TradfriConstants.INSTANCE_ID)
    Integer instanceId = 0;

    @SerializedName(TradfriConstants.DIMMER)
    Integer lightLevel;

    @SerializedName(TradfriConstants.ONOFF)
    Integer on = 1;

    @SerializedName(TradfriConstants.COLOR)
    String color = "efd275";

    @SerializedName("5707")
    Integer res5707 = 0;

    @SerializedName("5708")
    Integer res5708 = 0;

    @SerializedName(TradfriConstants.COLOR_X)
    Integer colorX = 33135;

    @SerializedName(TradfriConstants.COLOR_Y)
    Integer colorY = 27211;

    @SerializedName("5711")
    Integer res5711 = 0;


}
