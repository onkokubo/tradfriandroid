package sk.onkokubo.iot.tradfriandroid.caller;

import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eclipse.californium.core.coap.Response;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import sk.onkokubo.iot.tradfriandroid.TradfriConstants;
import sk.onkokubo.iot.tradfriandroid.data.access.TradfriEntityParser;
import sk.onkokubo.iot.tradfriandroid.data.model.TradfriDeviceValues;
import sk.onkokubo.iot.tradfriandroid.data.model.TradfriEntity;
import sk.onkokubo.iot.tradfriandroid.util.LogWrapper;

/**
 * @author ondrejkubo on 04/06/2017.
 */
public class TradfriGateway {

    private final static String TAG = TradfriGateway.class.getSimpleName();

    private final InputStream mTrustedStoreInputStream;
    private byte[] mTrustedStore = {};
    private final String mSecret;
    private String mProtocol = "coaps:";
    private String mHost = "localhost";
    private String mPath = TradfriConstants.DISCOVERY_RESOURCE;

    private static final List<String> IGNORED_ENDPOINTS = new ArrayList<>();
    {
        IGNORED_ENDPOINTS.add("reset");
        IGNORED_ENDPOINTS.add("add");
        IGNORED_ENDPOINTS.add("remove");
    }

    private final List<String> endpoints = new ArrayList<>();

    public TradfriGateway(final TradfriGateway.Builder builder) {
        try {
            byte[] buffer = new byte[8192];
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            while ((bytesRead = builder.mTrustedStoreInputStream.read(buffer)) != -1)
            {
                output.write(buffer, 0, bytesRead);
            }
            mTrustedStore = output.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mTrustedStoreInputStream = builder.mTrustedStoreInputStream;
        mSecret = builder.mSecret;
        mProtocol = builder.mProtocol;
        mHost = builder.mHost;
        mPath = builder.mPath;
        initTopology();
    }

    @Deprecated
    final InputStream getTrustedStoreInputStream() {
        return mTrustedStoreInputStream;
    }
    final byte[] getTrustedStore() {
        return mTrustedStore;
    }
    final String getSecret() {
        return mSecret;
    }
    final String getHost() {
        return mHost;
    }

    private final void initTopology() {
        LogWrapper.d(TAG, "requesting topology");
        final String url = TradfriEndpointClient.createUrl(
                mProtocol,
                mHost,
                mPath
        );

        final TradfriEndpointClient client = new TradfriEndpointClient(this);
        client.createRequest(client.METHOD_GET, url);

        final Observable<Pair<String, Response>> resourceObservable = client.getRx(url);
        resourceObservable.subscribe(
                new Consumer<Pair<String, Response>>() {
                    @Override
                    public void accept(Pair<String, Response> result) throws Exception {
                        if ("".equals(result.second.getPayloadString())) {
                            initTopology();
                        } else {
                            LogWrapper.d(TAG, "Received data: " + result.second.getPayloadString());
                            Matcher m = Pattern.compile("(\\<\\/(.+?)\\>)")
                                    .matcher(result.second.getPayloadString());
                            LogWrapper.d(TAG, "Found endpoints: ");
                            while (m.find()) {
                                LogWrapper.d(TAG, "    " + m.group(2));
                                boolean add = true;
                                for (final String ignored : IGNORED_ENDPOINTS) {
                                    if (m.group(2).indexOf(ignored) != -1) {
                                        add = false;
                                    }
                                }
                                if (add) {
                                    endpoints.add(m.group(2));
                                }
                            }
                            scanEndpoints(client);
                        }
                    }
                }
        );
    }

    private final void scanEndpoints(final TradfriEndpointClient client) {
        for (final String endpoint: endpoints) {
            final String url = TradfriEndpointClient.createUrl(
                    mProtocol,
                    mHost,
                    "/" + endpoint
            );
            client.getRx(url).subscribe(endpointDataConsumer);
        }
    }


    private final Consumer<Pair<String, Response>> endpointDataConsumer = new Consumer<Pair<String, Response>>() {
        @Override
        public void accept(Pair<String, Response> result) throws Exception {
            LogWrapper.d(TAG, "Response from address: " + result.first);
            //LogWrapper.i(TAG, TradfriEntityParser.getEntityType(result.second, result.first).toString());
            //LogWrapper.d(TAG, "Content: \n" + result.second);
            List<Long> idList;
            TradfriEntity tradfriEntity = new TradfriEntity();
            //TradfriEntityParser.parse(result.second, result.first);
            if (result.second.getPayloadString().length() == 0) {
                LogWrapper.e(TAG, "Empty response for: " + result.first);
                return;
            }
            if (result.second.getPayloadString().substring(0,2).equals("[{")) {
                Type listType = new TypeToken<List<TradfriDeviceValues>>() {}.getType();
                try {
                    idList= new Gson().fromJson(result.second.getPayloadString(), listType);
                }
                catch (Exception e) {
                    LogWrapper.e(TAG, "Error parsing Value list: \n"
                                    + result.second
                                    + "\n with exception: \n"
                                    + e.getMessage(),
                            e);
                }
            }
            else if (result.second.getPayloadString().substring(0,1).equals("[")) {
                Type listType = new TypeToken<List<Long>>() {}.getType();
                try {
                    idList= new Gson().fromJson(result.second.getPayloadString(), listType);
                }
                catch (Exception e) {
                    LogWrapper.e(TAG, "Error parsing ID list: \n"
                                    + result.second
                                    + "\n with exception: \n"
                                    + e.getMessage(),
                            e);
                }
            }
            else {
                try {
                    tradfriEntity = new Gson().fromJson(result.second.getPayloadString(), TradfriEntity.class);
                }
                catch (Exception e) {
                    LogWrapper.e(TAG, "Error parsing: \n"
                                    + result.second
                                    + "\n with exception: \n"
                                    + e.getMessage(),
                            e);
                }
            }
            LogWrapper.d(TAG, "parsed " + tradfriEntity.toString());
        }
    };

    public static final class Builder {

        private InputStream mTrustedStoreInputStream = null;
        private String mProtocol = "coaps";
        private String mHost = "localhost";
        private String mPath = TradfriConstants.DISCOVERY_RESOURCE;
        private String mSecret = "";

        public final Builder setTrustedStore(final String trustedStoreString) {
            mTrustedStoreInputStream = new ByteArrayInputStream(trustedStoreString.getBytes(StandardCharsets.UTF_8));
            return this;
        }
        public final Builder setTrustedStore(final InputStream trustedStore) {
            mTrustedStoreInputStream = trustedStore;
            return this;
        }
        public final Builder setProtocol(final String protocol) {
            mProtocol = protocol;
            return this;
        }
        public final Builder setHost(final String host) {
            mHost = host;
            return this;
        }
        public final Builder setPath(final String path) {
            mPath = path;
            return this;
        }
        public final Builder setSecret(final String secret) {
            mSecret = secret;
            return this;
        }

        public final TradfriGateway build() {
            return new TradfriGateway(this);
        }

    }

}
