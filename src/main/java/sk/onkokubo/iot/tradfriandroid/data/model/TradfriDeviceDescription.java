package sk.onkokubo.iot.tradfriandroid.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author ondrejkubo on 31/05/2017.
 */

public final class TradfriDeviceDescription {

    @SerializedName("0")
    private String manufacturer;

    @SerializedName("1")
    private String modelName;

    @SerializedName("3")
    private String modelId;


    @SerializedName("2")
    private String res2;
    @SerializedName("6")
    private String res6;
    @SerializedName("9")
    private String res9;
}
