
/*
 *  Copyright (C) 2016-2021 José Flávio de Souza Dias Júnior
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
 *  Direitos Autorais Reservados (C) 2016-2021 José Flávio de Souza Dias Júnior
 * 
 *  Este arquivo é parte de Urucum - <http://joseflavio.com/urucum/>.
 * 
 *  Urucum é software livre: você pode redistribuí-lo e/ou modificá-lo
 *  sob os termos da Licença Pública Menos Geral GNU conforme publicada pela
 *  Free Software Foundation, tanto a versão 3 da Licença, como
 *  (a seu critério) qualquer versão posterior.
 * 
 *  Urucum é distribuído na expectativa de que seja útil,
 *  porém, SEM NENHUMA GARANTIA; nem mesmo a garantia implícita de
 *  COMERCIABILIDADE ou ADEQUAÇÃO A UMA FINALIDADE ESPECÍFICA. Consulte a
 *  Licença Pública Menos Geral do GNU para mais detalhes.
 * 
 *  Você deve ter recebido uma cópia da Licença Pública Menos Geral do GNU
 *  junto com Urucum. Se não, veja <http://www.gnu.org/licenses/>.
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
 * Utilitários para comunicação de dados.
 * @author José Flávio de Souza Dias Júnior
 */
public class ComunicacaoUtil {

    /**
     * Abre um {@link KeyStore} através de {@link KeyStore#load(java.io.InputStream, char[])}.
     * @param arquivo Arquivo do {@link KeyStore}. Sendo null, será retornado um {@link KeyStore} vazio.
     * @param senha Senha para abrir o {@link KeyStore}. null == "".
     * @param tipo Tipo do {@link KeyStore}. Padrão: "jks". Veja {@link KeyStore#getInstance(String)}.
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
     * com base nas especificações feitas através
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
     * com base nas especificações feitas através
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
     * um {@link KeyManagerFactory} com base no {@link KeyStore} obtido através de {@link #abrirKeyStore()}.
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
     * um {@link TrustManagerFactory} com base no {@link KeyStore} obtido através de {@link #abrirTrustStore()}.
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
     * @return {@link SSLContext} para comunicação através do protocolo SSL/TLS.
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