package sk.onkokubo.iot.tradfriandroid.data.model;

import com.google.gson.annotations.SerializedName;

import sk.onkokubo.iot.tradfriandroid.TradfriConstants;

/**
 * @author ondrejkubo on 04/06/2017.
 */

public final class TradfriGroupModel extends TradfriEntity {

    @SerializedName("9039")
    private Long res9039;

    @SerializedName(TradfriConstants.HS_ACCESSORY_LINK)
    private TradfriAccessoryLinkModel accessoryLink;
}
