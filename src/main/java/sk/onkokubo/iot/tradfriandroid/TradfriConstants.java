package sk.onkokubo.iot.tradfriandroid;


/**
 * Partly inspired by "com/ikea/tradfri/lighting/ipso/IPSOObjects.java"
 * Some of these values can be found in the official LwM2M registry:
 *     http://www.openmobilealliance.org/wp/OMNA/LwM2M/LwM2MRegistry.html
 *
 * @author r41d, ondrejkubo
 */
public final class TradfriConstants {

    public static final String DEFAULT_PSK = "";
    public static final String DISCOVERY_RESOURCE = "/.well-known/core";
    public static final String TRUSTED_STORE_PASSWORD = "rootPass";

    // Device types (contained in INSTANCE_ID = "9003")
    public static final int TYPE_REMOTE = 0;
    public static final int TYPE_BULB = 2;
    // The others need to be figured out by people who own these


    public static final String FIELD_DESCRIPTION = "3";

    // Top level navigation
    public static final String ENTITY_TYPE_DEVICES = "15001";
    public static final String ENTITY_TYPE_DEVICE_LIST = "15002";
    public static final String ENTITY_TYPE_GROUPS = "15004";
    public static final String ENTITY_TYPE_MOODS = "15005";
    public static final String ENTITY_TYPE_ = "15006";
    public static final String ENTITY_TYPE__ = "15010";
    public static final String ENTITY_TYPE_REMOTE_VALUES = "15010";
    public static final String ENTITY_TYPE_NTP = "15011";
    public static final String ENTITY_TYPE_VALUE_DEFINITIONS = "15013";

    // Values in JSON data
    public static final String NAME = "9001"; // used in both devices and groups
    public static final String DEVICE_TIMESTAMP = "9002"; // used in both devices and groups
    public static final String INSTANCE_ID = "9003"; // In devices: device ID. In groups: list of device IDs
    public static final String HS_ACCESSORY_LINK = "9018";
    public static final String LAST_CHANGET_TIMESTAMP = "9020";
    public static final String LIGHT = "3311"; // urn:oma:lwm2m:ext:3311 in LwM2M registry
    public static final String TYPE = "5750"; // "Application Type" in LwM2M registry
    public static final String ONOFF = "5850"; // "On/Off" in LwM2M registry
    public static final String DIMMER = "5851"; // "Dimmer" in LwM2M registry
    public static final String TRANSITION_TIME = "5712"; // not contained in LwM2M registry


    public static final int DEVICE_TYPE_REMOTE = 0;
    public static final int DEVICE_TYPE_REMOTE_ = 1; // not contained in LwM2M registry
    public static final int DEVICE_TYPE_LIGHT = 2; // not contained in LwM2M registry

    // Color / Temperature related, these are independent of brightness, i.e. do not change if brightness does
    public static final String COLOR = "5706";
    public static final String COLOR_X = "5709";
    public static final String COLOR_Y = "5710";
    public static final String COLOR_COLD = "f5faf6";
    public static final String COLOR_COLD_X = "24930";
    public static final String COLOR_COLD_Y = "24694";
    public static final String COLOR_NORMAL = "f1e0b5";
    public static final String COLOR_NORMAL_X = "30140";
    public static final String COLOR_NORMAL_Y = "26909";
    public static final String COLOR_WARM = "efd275";
    public static final String COLOR_WARM_X = "33135";
    public static final String COLOR_WARM_Y = "27211";

    // Dimmer related
    public static final int DIMMER_MIN = 0;
    public static final int DIMMER_MAX = 254;

}
