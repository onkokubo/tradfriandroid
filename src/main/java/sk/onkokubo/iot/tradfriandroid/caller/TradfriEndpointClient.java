package sk.onkokubo.iot.tradfriandroid.caller;

import android.util.Pair;

import org.eclipse.californium.core.CaliforniumLogger;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.network.Endpoint;
import org.eclipse.californium.core.network.EndpointManager;
import org.eclipse.californium.scandium.ScandiumLogger;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Callable;
import java.util.logging.Level;

import io.reactivex.Observable;
import sk.onkokubo.iot.tradfriandroid.util.LogWrapper;

/**
 * @author ondrejkubo on 28/04/2017.
 */

public final class TradfriEndpointClient {

    private static final String TAG = TradfriEndpointClient.class.getSimpleName();

    static {
        CaliforniumLogger.initialize();
        CaliforniumLogger.setLevel(Level.WARNING);

        ScandiumLogger.initialize();
        ScandiumLogger.setLevel(Level.FINER);
    }

    private String mPsk;
    private static boolean loop = true;
    private static Endpoint dtlsEndpoint;
    private final TradfriGateway mGateway;

    // resource URI path used for discovery
    private static final String DISCOVERY_RESOURCE = "/.well-known/core";

    // indices of command line parameters
    private static final int IDX_METHOD          = 0;
    private static final int IDX_URI             = 1;
    private static final int IDX_PAYLOAD         = 2;

    // exit codes for runtime errors
    private static final int ERR_MISSING_METHOD  = 1;
    private static final int ERR_UNKNOWN_METHOD  = 2;
    private static final int ERR_MISSING_URI     = 3;
    private static final int ERR_BAD_URI         = 4;
    private static final int ERR_REQUEST_FAILED  = 5;
    private static final int ERR_RESPONSE_FAILED = 6;


    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_PUT = "PUT";
    public static final String METHOD_DELETE = "DELETE";
    public static final String METHOD_DISCOVER = "DISCOVER";
    public static final String METHOD_OBSERVE = "OBSERVE";

    public TradfriEndpointClient(final TradfriGateway gateway) {
        mGateway = gateway;
    }

    public static final String createUrl(
            final String protocol,
            final String host,
            final String endpoint
    ) {
        final StringBuilder sb = new StringBuilder(protocol);
        sb.append("://");
        sb.append(host);
        sb.append(endpoint);
        return sb.toString();
    }

    public final Response get(final String address) {
        return request(METHOD_GET, address, new byte[0]);
    }

    public final Observable<Pair<String, Response>> getRx(final String address) {
        return Observable.fromCallable(new Callable<Pair<String, Response>>() {
            @Override
            public Pair<String, Response> call() throws Exception {
                return new Pair<String, Response>(address, get(address));
            }

        });
    }

    public final Response post(final String address) {
        return request(METHOD_POST, address, new byte[0]);
    }

    public final Response put(final String address) {
        return request(METHOD_PUT, address, new byte[0]);
    }

    public final Response delete(final String address) {
        return request(METHOD_DELETE, address, new byte[0]);
    }

    public final Request createRequest(final String method, final String address) {
        return createRequest (method, address, new byte[0]);
    }

    public final Request createRequest(final String method, final String address, byte[] payload) {
        final Request request = newRequest(method);
        URI uri = null;
        try {
            uri = new URI(address);
        } catch (URISyntaxException e) {
            LogWrapper.e(TAG, e.getMessage(), e);
            return request;
        }

        // set request URI
        if (method.equals(METHOD_DISCOVER) && (uri.getPath() == null || uri.getPath().isEmpty() || uri.getPath().equals("/"))) {
            // add discovery resource path to URI
            try {
                uri = new URI(uri.getScheme(), uri.getAuthority(), DISCOVERY_RESOURCE, uri.getQuery());

            } catch (URISyntaxException e) {
                LogWrapper.e(TAG, "Failed to parse URI: " + e.getMessage());
            }
        }

        request.setURI(uri);
        request.setPayload(payload);
        request.getOptions().setContentFormat(MediaTypeRegistry.TEXT_PLAIN);
        return request;
    }

    private final Response request(final String method, final String address, byte[] payload) {
        final Request request = createRequest(method, address, payload);
        return request(request);
    }


    private final Response request(final Request request) {
        Response response = null;
        final TradfriEndpointFactory factory = TradfriEndpointFactory.getInstance(mGateway);
        dtlsEndpoint = factory.getEndpoint(request, mPsk);

        EndpointManager.getEndpointManager().setDefaultSecureEndpoint(dtlsEndpoint);
        // execute request
        try {
            request.send();

            // loop for receiving multiple responses
            do {

                // receive response
                try {
                    response = request.waitForResponse();
                } catch (InterruptedException e) {
                    LogWrapper.e(TAG, "Failed to receive response: " + e.getMessage(), e);
                }

                // output response

                if (response != null) {

                    // check of response contains resources
                    if (response.getOptions().isContentFormat(MediaTypeRegistry.APPLICATION_LINK_FORMAT)) {

                        return response;

                        // output discovered resources

                    } else {
                        // check if link format was expected by client
                        if (request.getCode().equals(METHOD_DISCOVER)) {
                            LogWrapper.e(TAG, "Server error: Link format not specified");
                        }
                        else {
                            return response;
                        }
                    }

                } else {
                    // no response received
                    LogWrapper.e(TAG, "Request timed out");
                    break;
                }

            } while (loop);

        } catch (Exception e) {
            LogWrapper.e(TAG, "Failed to execute request: " + e.getMessage(), e);
        }
        return response;

    }

    private final static Request newRequest(String method) {
        if (method.equals(METHOD_GET)) {
            return Request.newGet();
        } else if (method.equals(METHOD_POST)) {
            return Request.newPost();
        } else if (method.equals(METHOD_PUT)) {
            return Request.newPut();
        } else if (method.equals(METHOD_DELETE)) {
            return Request.newDelete();
        } else if (method.equals(METHOD_DISCOVER)) {
            return Request.newGet();
        } else if (method.equals(METHOD_OBSERVE)) {
            Request request = Request.newGet();
            request.setObserve();
            loop = true;
            return request;
        } else {
            LogWrapper.e(TAG,"Unknown method: " + method);
            return null;
        }
    }

}
