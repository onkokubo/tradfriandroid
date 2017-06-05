package sk.onkokubo.iot.tradfriandroid.data.model;

import com.google.gson.annotations.SerializedName;

import sk.onkokubo.iot.tradfriandroid.TradfriConstants;

/**
 * @author ondrejkubo on 04/06/2017.
 */

public final class TradfriAccessoryLinkModel extends TradfriEntity {

    public class TradfriDeviceList {
        @SerializedName(TradfriConstants.INSTANCE_ID)
        private Long[] deviceIds = {};
    }

    @SerializedName(TradfriConstants.ENTITY_TYPE_DEVICE_LIST)
    TradfriDeviceList deviceList;

}
