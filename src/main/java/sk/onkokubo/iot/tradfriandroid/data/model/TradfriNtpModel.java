package sk.onkokubo.iot.tradfriandroid.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author ondrejkubo on 04/06/2017.
 */

public final class TradfriNtpModel extends TradfriEntity {


    @SerializedName("9023")
    private String ntpHost;

    @SerializedName("9059")
    private Long timestamp;

    @SerializedName("9060")
    private String readableTimestamp;

}
