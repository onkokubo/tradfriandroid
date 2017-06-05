package sk.onkokubo.iot.tradfriandroid.data.access;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.eclipse.californium.core.coap.Response;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import sk.onkokubo.iot.tradfriandroid.TradfriEntityType;
import sk.onkokubo.iot.tradfriandroid.data.model.TradfriDeviceModel;
import sk.onkokubo.iot.tradfriandroid.data.model.TradfriDeviceValues;
import sk.onkokubo.iot.tradfriandroid.data.model.TradfriEntity;
import sk.onkokubo.iot.tradfriandroid.data.model.TradfriEntityContainer;
import sk.onkokubo.iot.tradfriandroid.data.model.TradfriIdListModel;
import sk.onkokubo.iot.tradfriandroid.data.model.TradfriMoodModel;
import sk.onkokubo.iot.tradfriandroid.data.model.TradfriNtpModel;
import sk.onkokubo.iot.tradfriandroid.util.LogWrapper;

/**
 * @author ondrejkubo on 03/06/2017.
 */

public final class TradfriEntityParser {

    private static final String TAG = TradfriEntityParser.class.getSimpleName();

    public static final TradfriEntityContainer parse(final Response response, final String address) {
        final Type responseType = getEntityType(response, address);
        if (responseType.equals(new TypeToken<List<Long>>() {}.getType())) {
            final TradfriIdListModel list = new TradfriIdListModel();

            Type listType = new TypeToken<List<Long>>() {}.getType();
            try {
                List<Long> idList= new Gson().fromJson(response.getPayloadString(), listType);
                list.setmIds(idList);
                return list;
            }
            catch (Exception e) {
                LogWrapper.e(TAG, "Error parsing Value list: \n"
                                + response.getPayloadString()
                                + "\n with exception: \n"
                                + e.getMessage(),
                        e);
            }
        }
        if (responseType.equals(new TypeToken<List<TradfriDeviceValues>>() {}.getType())) {
            final TradfriIdListModel list = new TradfriIdListModel();

            Type listType = new TypeToken<List<TradfriDeviceValues>>() {}.getType();
            try {
                List<Long> idList= new Gson().fromJson(response.getPayloadString(), listType);
                list.setmIds(idList);
                return list;
            }
            catch (Exception e) {
                LogWrapper.e(TAG, "Error parsing Value list: \n"
                                + response.getPayloadString()
                                + "\n with exception: \n"
                                + e.getMessage(),
                        e);
            }
        }
        else {
            try {
                return new Gson().fromJson(response.getPayloadString(), responseType);
            }
            catch (Exception e) {
                LogWrapper.e(TAG, "Error parsing Value list: \n"
                                + response.getPayloadString()
                                + "\n with exception: \n"
                                + e.getMessage(),
                        e);
            }

        }
        return null;
    }

    public static final Type getEntityType(final Response response, final String address) {
        URI uri = null;
        try {
            uri = new URI(address);
        } catch (URISyntaxException e) {
            LogWrapper.e(TAG, e.getMessage(), e);
        }
        final String[] segments = uri.getPath().split("\\/");
        if (segments.length < 2) {
            return null;
        }
        else {
            int entityCode = 0;
            try {
                entityCode = Integer.valueOf(segments[2]);
            }
            catch (Exception e) {
                return null;
            }
            TradfriEntityType type = TradfriEntityType.getByIndex(entityCode);
            // Parse top-level path
            if (segments.length == 3) {
                if (type == TradfriEntityType.TYPE_) {
                    return new TypeToken<List<TradfriDeviceValues>>() {}.getType();
                }
                else {
                    return new TypeToken<List<Long>>() {}.getType();
                }
            }
            else if (segments.length == 4) {
                if (type == TradfriEntityType.MOODS) {
                    return new TypeToken<List<Long>>() {}.getType();
                }
                switch (type) {
                    case MOODS:
                        return TradfriMoodModel.class;
                    case DEVICES:
                        return TradfriDeviceModel.class;
                    case DEVICE_LIST:
                        return new TypeToken<List<TradfriDeviceModel>>() {}.getType();
                    case NTP:
                        return TradfriNtpModel.class;
                    default:
                        return JsonObject.class;
                }
            }
            // Parse moods
            else if (segments.length == 5) {
                return TradfriMoodModel.class;
            }
        }
        return null;
    }

}
