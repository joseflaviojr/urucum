
/*
 *  Copyright (C) 2016-2018 Jos� Fl�vio de Souza Dias J�nior
 *  
 *  This file is part of Urucum - <http://joseflavio.com/urucum/>.
 *  
 *  Urucum is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  Urucum is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU Lesser General Public License for more details.
 *  
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Urucum. If not, see <http://www.gnu.org/licenses/>.
 */

/*
 *  Direitos Autorais Reservados (C) 2016-2018 Jos� Fl�vio de Souza Dias J�nior
 * 
 *  Este arquivo � parte de Urucum - <http://joseflavio.com/urucum/>.
 * 
 *  Urucum � software livre: voc� pode redistribu�-lo e/ou modific�-lo
 *  sob os termos da Licen�a P�blica Menos Geral GNU conforme publicada pela
 *  Free Software Foundation, tanto a vers�o 3 da Licen�a, como
 *  (a seu crit�rio) qualquer vers�o posterior.
 * 
 *  Urucum � distribu�do na expectativa de que seja �til,
 *  por�m, SEM NENHUMA GARANTIA; nem mesmo a garantia impl�cita de
 *  COMERCIABILIDADE ou ADEQUA��O A UMA FINALIDADE ESPEC�FICA. Consulte a
 *  Licen�a P�blica Menos Geral do GNU para mais detalhes.
 * 
 *  Voc� deve ter recebido uma c�pia da Licen�a P�blica Menos Geral do GNU
 *  junto com Urucum. Se n�o, veja <http://www.gnu.org/licenses/>.
 */

package com.joseflavio.urucum.comunicacao;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 * Utilit�rios para comunica��o de dados.
 * @author Jos� Fl�vio de Souza Dias J�nior
 */
public class ComunicacaoUtil {

    /**
     * Abre um {@link KeyStore} atrav�s de {@link KeyStore#load(java.io.InputStream, char[])}.
     * @param endereco Arquivo do {@link KeyStore}. Sendo null, ser� retornado um {@link KeyStore} vazio.
     * @param senha Senha para abrir o {@link KeyStore}. null == "".
     * @param tipo Tipo do {@link KeyStore}. Padr�o: "jks". Veja {@link KeyStore#getInstance(String)}.
     */
    public static KeyStore abrirKeyStore( File arquivo, String senha, String tipo ) throws
        KeyStoreException, IOException,
        NoSuchAlgorithmException, CertificateException {

        if( senha == null ) senha = "";
        if( tipo  == null ) tipo  = "jks";

        KeyStore ks = KeyStore.getInstance( tipo );

        if( arquivo == null ) return ks;

        try( FileInputStream fis = new FileInputStream( arquivo ) ){
            ks.load( fis, senha.toCharArray() );
        }

        return ks;

    }

    /**
     * {@link #abrirKeyStore(File, String, String)}
     * com base nas especifica��es feitas atrav�s
     * das {@link System#setProperty(String, String) propriedades}
     * "javax.net.ssl.keyStore", "javax.net.ssl.keyStorePassword" e "javax.net.ssl.keyStoreType".
     */
    public static KeyStore abrirKeyStore() throws
        KeyStoreException, IOException,
        NoSuchAlgorithmException, CertificateException {
         
        String ksa = System.getProperty( "javax.net.ssl.keyStore" );
        String kss = System.getProperty( "javax.net.ssl.keyStorePassword" );
        String kst = System.getProperty( "javax.net.ssl.keyStoreType" );
        
        return abrirKeyStore(
            ksa != null ? new File( ksa ) : null,
            kss,
            kst
        );

    }

    /**
     * {@link #abrirKeyStore(File, String, String)}
     * com base nas especifica��es feitas atrav�s
     * das {@link System#setProperty(String, String) propriedades}
     * "javax.net.ssl.trustStore", "javax.net.ssl.trustStorePassword" e "javax.net.ssl.trustStoreType".
     */
    public static KeyStore abrirTrustStore() throws
        KeyStoreException, IOException,
        NoSuchAlgorithmException, CertificateException {
         
        String tsa = System.getProperty( "javax.net.ssl.trustStore" );
        String tss = System.getProperty( "javax.net.ssl.trustStorePassword" );
        String tst = System.getProperty( "javax.net.ssl.trustStoreType" );
        
        return abrirKeyStore(
            tsa != null ? new File( tsa ) : null,
            tss,
            tst
        );

    }

    /**
     * {@link KeyManagerFactory#init(KeyStore, char[]) Inicializa}
     * um {@link KeyManagerFactory} com base no {@link KeyStore} obtido atrav�s de {@link #abrirKeyStore()}.
     */
    public static KeyManagerFactory iniciarKeyManagerFactory() throws
        KeyStoreException, IOException,
        NoSuchAlgorithmException, CertificateException,
        UnrecoverableKeyException {

        String senha = System.getProperty( "javax.net.ssl.keyStorePassword" );
        if( senha == null ) senha = "";
        
        KeyManagerFactory kmf = KeyManagerFactory.getInstance( KeyManagerFactory.getDefaultAlgorithm() );
        kmf.init( abrirKeyStore(), senha.toCharArray() );

        return kmf;

    }

    /**
     * {@link TrustManagerFactory#init(KeyStore) Inicializa}
     * um {@link TrustManagerFactory} com base no {@link KeyStore} obtido atrav�s de {@link #abrirTrustStore()}.
     */
    public static TrustManagerFactory iniciarTrustManagerFactory() throws
        KeyStoreException, IOException,
        NoSuchAlgorithmException, CertificateException {

        TrustManagerFactory tmf = TrustManagerFactory.getInstance( TrustManagerFactory.getDefaultAlgorithm() );
        tmf.init( abrirTrustStore() );

        return tmf;

    }

    /**
     * {@link SSLContext#init(javax.net.ssl.KeyManager[], javax.net.ssl.TrustManager[], SecureRandom) Inicializa}
     * um {@link SSLContext} com base em {@link #iniciarKeyManagerFactory()} e {@link #iniciarTrustManagerFactory()}.
     * @return {@link SSLContext} para comunica��o atrav�s do protocolo SSL/TLS.
     */
    public static SSLContext iniciarSSLContext() throws
        KeyStoreException, IOException,
        NoSuchAlgorithmException, CertificateException,
        UnrecoverableKeyException, KeyManagementException {

        SSLContext contexto = SSLContext.getInstance( "TLS" );

        contexto.init(
            iniciarKeyManagerFactory().getKeyManagers(),
            iniciarTrustManagerFactory().getTrustManagers(),
            new SecureRandom()
        );

        return contexto;

    }

}