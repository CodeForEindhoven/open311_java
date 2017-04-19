package org.codeforamerica.open311.internals.network;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Represents an ordered list of {@link X509TrustManager}s with additive trust. If any one of the composed managers
 * trusts a certificate chain, then it is trusted by the composite manager.
 *
 * This is necessary because of the fine-print on {@link SSLContext#init}: Only the first instance of a particular key
 * and/or trust manager implementation type in the array is used. (For example, only the first
 * javax.net.ssl.X509KeyManager in the array will be used.)
 *
 * @author codyaray
 * @since 4/22/2013
 * @see <a href="http://stackoverflow.com/questions/1793979/registering-multiple-keystores-in-jvm">
 *     http://stackoverflow.com/questions/1793979/registering-multiple-keystores-in-jvm
 *     </a>
 */

class CompositeX509TrustManager implements X509TrustManager {

    private final List<X509TrustManager> trustManagers;

    public CompositeX509TrustManager(List<X509TrustManager> trustManagers) {
        this.trustManagers = Collections.unmodifiableList(trustManagers);
    }

    private CompositeX509TrustManager(KeyStore keystore) {
        List<X509TrustManager> trustManagers = new ArrayList<X509TrustManager>();
        trustManagers.add(getDefaultTrustManager());
        trustManagers.add(getTrustManager(keystore));
        this.trustManagers = Collections.unmodifiableList(trustManagers);
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        for (X509TrustManager trustManager : trustManagers) {
            try {
                trustManager.checkClientTrusted(chain, authType);
                return; // someone trusts them. success!
            } catch (CertificateException e) {
                // maybe someone else will trust them
            }
        }
        throw new CertificateException("None of the TrustManagers trust this certificate chain");
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        for (X509TrustManager trustManager : trustManagers) {
            try {
                trustManager.checkServerTrusted(chain, authType);
                return; // someone trusts them. success!
            } catch (CertificateException e) {
                // maybe someone else will trust them
            }
        }
        throw new CertificateException("None of the TrustManagers trust this certificate chain");
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        List<X509Certificate> certificates = new ArrayList<X509Certificate>();
        for (X509TrustManager trustManager : trustManagers) {
            Collections.addAll(certificates, trustManager.getAcceptedIssuers());
        }
        X509Certificate[] array = new X509Certificate[certificates.size()];
        return certificates.toArray(array); // fill the array
    }

    public static TrustManager[] getTrustManagers(KeyStore keyStore) {

        return new TrustManager[] { new CompositeX509TrustManager(keyStore) };

    }

    private static X509TrustManager getDefaultTrustManager() {

        return getTrustManager(null);

    }

    private static X509TrustManager getTrustManager(KeyStore keystore) {

        return getTrustManager(TrustManagerFactory.getDefaultAlgorithm(), keystore);

    }

    private static X509TrustManager getTrustManager(String algorithm, KeyStore keystore) {
        TrustManagerFactory factory;
        try {
            factory = TrustManagerFactory.getInstance(algorithm);
            factory.init(keystore);
            return (X509TrustManager) factory.getTrustManagers()[0];
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        return null;

    }

}