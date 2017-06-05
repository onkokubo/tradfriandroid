package sk.onkokubo.iot.tradfriandroid.caller;

import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.Endpoint;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.scandium.DTLSConnector;
import org.eclipse.californium.scandium.config.DtlsConnectorConfig;
import org.eclipse.californium.scandium.dtls.cipher.CipherSuite;
import org.eclipse.californium.scandium.dtls.pskstore.InMemoryPskStore;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

import sk.onkokubo.iot.tradfriandroid.TradfriConstants;
import sk.onkokubo.iot.tradfriandroid.util.LogWrapper;


/**
 * https://stackoverflow.com/questions/4065379/how-to-create-a-bks-bouncycastle-format-java-keystore-that-contains-a-client-c
 * https://stackoverflow.com/questions/24033346/nosuchalgorithmexception-with-keystore-getinstance-in-android-application
 * @author ondrejkubo on 29/05/2017.
 */

public final class TradfriEndpointFactory {

    private final String TAG = TradfriEndpointFactory.class.getSimpleName();

    private static TradfriEndpointFactory instance;

    private final Map<TradfriEndpointIdentifier, Endpoint> endpoints = new HashMap<>();

    private final TradfriGateway mGateway;

    private TradfriEndpointFactory(
            final TradfriGateway gateway) {
        mGateway = gateway;
    }

    public static final TradfriEndpointFactory getInstance(final TradfriGateway gateway) {
        if (instance == null) {
            instance = new TradfriEndpointFactory(gateway);
        }
        return instance;
    }

    public final Endpoint getEndpoint(final Request request, final String secret) {
        final TradfriEndpointIdentifier id = new TradfriEndpointIdentifier(
                mGateway.getHost(),
                request.getDestinationPort(),
                mGateway.getSecret());
        if (endpoints.get(id) != null) {
            return endpoints.get(id);
        }
        else if (endpoints.get(id) != null) {
            try {
                endpoints.get(id).start();
            } catch (IOException e) {
                LogWrapper.e(TAG, e.getMessage(), e);
            }
            return endpoints.get(id);
        }
        else {
            return createEndpoint(id, request);
        }
    }

    private final Endpoint createEndpoint(final TradfriEndpointIdentifier id, final Request request) {
        InputStream inTrust;
        KeyStore trustStore;
        Certificate[] trustedCertificates;
        try {
            trustStore = KeyStore.getInstance("BKS");
            // inTrust = new FileInputStream(TRUST_STORE_LOCATION);

            InputStream keyInputStream = new ByteArrayInputStream(mGateway.getTrustedStore());
            trustStore.load(keyInputStream, TradfriConstants.TRUSTED_STORE_PASSWORD.toCharArray());
            trustedCertificates = new Certificate[1];
            trustedCertificates[0] = trustStore.getCertificate("root");
        } catch (FileNotFoundException e) {
            LogWrapper.e(TAG, e.getMessage(), e);
            return null;
        } catch (KeyStoreException e) {
            LogWrapper.e(TAG, e.getMessage(), e);
            return null;
        }
        catch (IOException e) {
            LogWrapper.e(TAG, e.getMessage(), e);
            return null;
        } catch (NoSuchAlgorithmException e) {
            LogWrapper.e(TAG, e.getMessage(), e);
            return null;
        } catch (CertificateException e) {
            LogWrapper.e(TAG, e.getMessage(), e);
            return null;
        }

        DtlsConnectorConfig.Builder builder;
        builder = new DtlsConnectorConfig.Builder(new InetSocketAddress(0));
        builder.setTrustStore(trustedCertificates);
        builder.setRetransmissionTimeout(12000);
        builder.setMaxRetransmissions(20);
        // use PSK - empty string
        final InMemoryPskStore pskStore = new InMemoryPskStore();
        pskStore.addKnownPeer(
                new InetSocketAddress(request.getDestination(),
                        request.getDestinationPort()),
                TradfriConstants.DEFAULT_PSK,
                id.getSecret().getBytes());
        builder.setPskStore(pskStore);
        builder.setSupportedCipherSuites(new CipherSuite[] {CipherSuite.TLS_PSK_WITH_AES_128_CCM_8});

        final DTLSConnector dtlsconnector = new DTLSConnector(builder.build(), null);
        NetworkConfig cfg = NetworkConfig.getStandard();
        cfg.setInt(NetworkConfig.Keys.ACK_TIMEOUT, 5000);
        final Endpoint dtlsEndpoint = new CoapEndpoint(dtlsconnector, cfg);
        try {
            dtlsEndpoint.start();
        } catch (IOException e) {
            LogWrapper.e(TAG, e.getMessage(), e);
            return null;
        }
        endpoints.put(id, dtlsEndpoint);
        return dtlsEndpoint;
    }

}
