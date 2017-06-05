package sk.onkokubo.iot.tradfriandroid;

/**
 * @author ondrejkubo on 03/06/2017.
 */
public enum TradfriEntityType {

    DEVICES(15001),
    DEVICE_LIST(15002),
    GROUPS(15004),
    MOODS(15005),
    TYPE_(15006),
    REMOTE_VALUES(15010),
    NTP(15011),
    VALUE_DEFINITIONS(15013);

    int mTypeOrdinal = 0;

    TradfriEntityType(int ord) {
        this.mTypeOrdinal = ord;
    }

    public static TradfriEntityType getByIndex(int index) {
        for (TradfriEntityType m : TradfriEntityType.values()) {
            if (m.mTypeOrdinal == index) {
                return m;
            }
        }
        return null;
    }

    public static int getIndexForType(TradfriEntityType type) {
        for (TradfriEntityType m : TradfriEntityType.values()) {
            if (m == type) {
                return m.mTypeOrdinal;
            }
        }
        return 0;
    }


}
