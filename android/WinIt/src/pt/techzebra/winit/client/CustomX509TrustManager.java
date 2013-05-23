package pt.techzebra.winit.client;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class CustomX509TrustManager implements X509TrustManager {
    private X509TrustManager standard_trust_manager_ = null;
    
    public CustomX509TrustManager(KeyStore keystore) throws NoSuchAlgorithmException, KeyStoreException {
        super();
        
        TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        factory.init(keystore);
        
        TrustManager[] trust_managers = factory.getTrustManagers();
        if (trust_managers.length == 0) {
            throw new NoSuchAlgorithmException("No trust manager found");
        }
        
        standard_trust_manager_ = (X509TrustManager) trust_managers[0];
    }
    
    @Override
    public void checkClientTrusted(X509Certificate[] chain, String auth_type)
            throws CertificateException {
        standard_trust_manager_.checkClientTrusted(chain, auth_type);
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String auth_type)
            throws CertificateException {
        int chain_length = chain.length;
        if (chain_length > 1) {
            int current_index;
            for (current_index = 0; current_index < chain.length; ++current_index) {
                boolean found_next = false;
                for (int next_index = current_index + 1; next_index < chain.length; ++next_index) {
                    if (chain[current_index].getIssuerDN().equals(chain[next_index].getIssuerDN())) {
                        found_next = true;
                        if (next_index != current_index + 1) {
                            X509Certificate temp_certificate = chain[next_index];
                            chain[next_index] = chain[current_index + 1];
                            chain[current_index + 1] = temp_certificate;
                        }
                        break;
                    }
                }
                
                if (!found_next) {
                    break;
                }
            }
            
            chain_length = current_index + 1;
            X509Certificate last_certificate = chain[chain_length - 1];
            Date now = new Date();
            if (last_certificate.getSubjectDN().equals(last_certificate.getIssuerDN()) && now.after(last_certificate.getNotAfter())) {
                --chain_length;
            }
        }
        
        standard_trust_manager_.checkServerTrusted(chain, auth_type);
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return standard_trust_manager_.getAcceptedIssuers();
    }
    
}
