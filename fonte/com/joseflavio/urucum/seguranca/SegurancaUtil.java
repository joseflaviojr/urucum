
/*
 *  Copyright (C) 2016-2018 José Flávio de Souza Dias Júnior
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
 *  Direitos Autorais Reservados (C) 2016-2018 José Flávio de Souza Dias Júnior
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

package com.joseflavio.urucum.seguranca;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Utilitários de segurança: criptografia e assinatura digital.
 * @author José Flávio de Souza Dias Júnior
 */
public class SegurancaUtil {
    
    /**
     * Gera um par de chaves (pública/privada) para criptografia assimétrica.
     * @param algoritmo Veja {@link KeyPairGenerator#getInstance(String)}
     * @param tamanho Veja {@link KeyPairGenerator#initialize(int)}
     */
    public static KeyPair gerarChaveAssimetrica( String algoritmo, int tamanho ) throws NoSuchAlgorithmException {
        KeyPairGenerator gerador = KeyPairGenerator.getInstance( algoritmo );
        gerador.initialize( tamanho, new SecureRandom() );
        return gerador.generateKeyPair();
    }
    
    /**
     * Chaves RSA de 2048 bits.
     * @see #gerarChaveAssimetrica(String, int)
     */
    public static KeyPair gerarChaveAssimetrica() throws NoSuchAlgorithmException {
        return gerarChaveAssimetrica( "RSA", 2048 );
    }
    
    /**
     * Gera uma chave secreta para criptografia simétrica.
     * @param algoritmo Veja {@link KeyGenerator#getInstance(String)}
     * @param tamanho Veja {@link KeyGenerator#init(int)}
     */
    public static SecretKey gerarChaveSimetrica( String algoritmo, int tamanho ) throws NoSuchAlgorithmException {
        KeyGenerator gerador = KeyGenerator.getInstance( algoritmo );
        gerador.init( tamanho, new SecureRandom() );
        return gerador.generateKey();
    }
    
    /**
     * Chave AES de 128 bits.
     * @see #gerarChaveSimetrica(String, int)
     */
    public static SecretKey gerarChaveSimetrica() throws NoSuchAlgorithmException {
        return gerarChaveSimetrica( "AES", 128 );
    }
    
    /**
     * Salva uma {@link Key chave} criptográfica num {@link File}.
     * @see Key#getEncoded()
     */
    public static void salvarChave( Key chave, File arquivo ) throws IOException {
        try( FileOutputStream fos = new FileOutputStream( arquivo ) ){
            fos.write( chave.getEncoded() );
        }
    }
    
    /**
     * Obtém uma {@link SecretKey} a partir de um {@link File}.
     * @param arquivo Arquivo no qual a chave está armazenada.
     * @param algoritmo Veja {@link SecretKeyFactory#getInstance(String)}
     */
    public static SecretKey obterChaveSimetrica( File arquivo, String algoritmo ) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        try( ByteArrayOutputStream baos = new ByteArrayOutputStream() ){
            SecretKeyFactory fabrica = SecretKeyFactory.getInstance( algoritmo );
            Files.copy( arquivo.toPath(), baos );
            return fabrica.generateSecret( new SecretKeySpec( baos.toByteArray(), algoritmo ) );
        }
    }
    
    /**
     * Obtém uma {@link SecretKey} AES a partir de um {@link File}.
     * @see #obterChaveSimetrica(File, String)
     */
    public static SecretKey obterChaveSimetrica( File arquivo ) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        return obterChaveSimetrica( arquivo, "AES" );
    }
    
    /**
     * Obtém uma {@link PublicKey} a partir de um {@link File}, formato X.509.
     * @param arquivo Arquivo no qual a chave está armazenada.
     * @param algoritmo Veja {@link KeyFactory#getInstance(String)}
     */
    public static PublicKey obterChavePublica( File arquivo, String algoritmo ) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        try( ByteArrayOutputStream baos = new ByteArrayOutputStream() ){
            KeyFactory fabrica = KeyFactory.getInstance( algoritmo );
            Files.copy( arquivo.toPath(), baos );
            return fabrica.generatePublic( new X509EncodedKeySpec( baos.toByteArray() ) );
        }
    }
    
    /**
     * Obtém uma {@link PublicKey} RSA a partir de um {@link File}, formato X.509.
     * @see #obterChavePublica(File, String)
     */
    public static PublicKey obterChavePublica( File arquivo ) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        return obterChavePublica( arquivo, "RSA" );
    }
    
    /**
     * Obtém uma {@link PrivateKey} a partir de um {@link File}, formato PKCS#8.
     * @param arquivo Arquivo no qual a chave está armazenada.
     * @param algoritmo Veja {@link KeyFactory#getInstance(String)}
     */
    public static PrivateKey obterChavePrivada( File arquivo, String algoritmo ) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        try( ByteArrayOutputStream baos = new ByteArrayOutputStream() ){
            KeyFactory fabrica = KeyFactory.getInstance( algoritmo );
            Files.copy( arquivo.toPath(), baos );
            return fabrica.generatePrivate( new PKCS8EncodedKeySpec( baos.toByteArray() ) );
        }
    }
    
    /**
     * Obtém uma {@link PrivateKey} RSA a partir de um {@link File}, formato PKCS#8.
     * @see #obterChavePrivada(File, String)
     */
    public static PrivateKey obterChavePrivada( File arquivo ) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        return obterChavePrivada( arquivo, "RSA" );
    }
    
    /**
     * {@link Signature#sign() Assina} digitalmente um conteúdo.
     * @param conteudo Conteúdo a ser assinado digitalmente.
     * @param algoritmo Ver {@link Signature#getInstance(String)}.
     * @return assinatura digital do conteúdo.
     */
    public static byte[] assinar( byte[] conteudo, String algoritmo, PrivateKey chave ) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature caneta = Signature.getInstance( algoritmo );
        caneta.initSign( chave );
        caneta.update( conteudo );
        return caneta.sign();
    }
    
    /**
     * {@link Signature#sign() Assina} digitalmente um conteúdo com "SHA256withRSA".
     * @param conteudo Conteúdo a ser assinado digitalmente.
     * @return assinatura digital do conteúdo.
     * @see #assinar(byte[], String, PrivateKey)
     */
    public static byte[] assinar( byte[] conteudo, PrivateKey chave ) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        return assinar( conteudo, "SHA256withRSA", chave );
    }
    
    /**
     * {@link Signature#verify(byte[])}  Verifica} a originalidade de um conteúdo assinado digitalmente.
     * @param assinatura Assinatura digital do conteúdo.
     * @param conteudo Conteúdo original.
     * @param algoritmo Ver {@link Signature#getInstance(String)}.
     * @return true, se assinatura compatível com conteúdo.
     */
    public static boolean verificar( byte[] assinatura, byte[] conteudo, String algoritmo, PublicKey chave ) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature caneta = Signature.getInstance( algoritmo );
        caneta.initVerify( chave );
        caneta.update( conteudo );
        return caneta.verify( assinatura );
    }
    
    /**
     * {@link Signature#verify(byte[])}  Verifica} a originalidade de um conteúdo assinado digitalmente com "SHA256withRSA".
     * @param assinatura Assinatura digital do conteúdo.
     * @param conteudo Conteúdo original.
     * @return true, se assinatura compatível com conteúdo.
     * @see #verificar(byte[], byte[], String, PublicKey)
     */
    public static boolean verificar( byte[] assinatura, byte[] conteudo, PublicKey chave ) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        return verificar( assinatura, conteudo, "SHA256withRSA", chave );
    }
    
    private static byte[] criptografia( int operacao, byte[] conteudo, String transformacao, Key chave, IvParameterSpec iv )
        throws
        NoSuchAlgorithmException, NoSuchPaddingException,
        InvalidKeyException, InvalidAlgorithmParameterException,
        IllegalBlockSizeException, BadPaddingException {
        
        Cipher cifra = Cipher.getInstance( transformacao );
        cifra.init( operacao, chave, iv );
        return cifra.doFinal( conteudo );
        
    }
    
    /**
     * {@link Cipher Codifica} um conteúdo de acordo com um método criptográfico.
     * @param conteudo Conteúdo a ser codificado.
     * @param transformacao Veja {@link Cipher#getInstance(String)}
     * @return conteúdo codificado.
     * @see Cipher
     * @see Cipher#ENCRYPT_MODE
     */
    public static byte[] codificar( byte[] conteudo, String transformacao, Key chave, IvParameterSpec iv )
        throws
        NoSuchAlgorithmException, NoSuchPaddingException,
        InvalidKeyException, InvalidAlgorithmParameterException,
        IllegalBlockSizeException, BadPaddingException {
        
        return criptografia( Cipher.ENCRYPT_MODE, conteudo, transformacao, chave, iv );
        
    }
    
    /**
     * {@link Cipher Codifica} um conteúdo utilizando "RSA/ECB/PKCS1Padding", se chave assimétrica, ou "AES/CBC/PKCS5Padding", se chave simétrica.
     * @see #codificar(byte[], String, Key, IvParameterSpec)
     */
    public static byte[] codificar( byte[] conteudo, Key chave, IvParameterSpec iv )
        throws
        NoSuchAlgorithmException, NoSuchPaddingException,
        InvalidKeyException, InvalidAlgorithmParameterException,
        IllegalBlockSizeException, BadPaddingException {
        
        if( chave instanceof PublicKey || chave instanceof PrivateKey ){
            return codificar( conteudo, "RSA/ECB/PKCS1Padding", chave, iv );
        }else{
            return codificar( conteudo, "AES/CBC/PKCS5Padding", chave, iv );
        }
        
    }
    
    /**
     * {@link Cipher Decodifica} um conteúdo de acordo com um método criptográfico.
     * @param conteudo Conteúdo a ser decodificado.
     * @param transformacao Veja {@link Cipher#getInstance(String)}
     * @return conteúdo decodificado.
     * @see Cipher
     * @see Cipher#DECRYPT_MODE
     */
    public static byte[] decodificar( byte[] conteudo, String transformacao, Key chave, IvParameterSpec iv )
        throws
        NoSuchAlgorithmException, NoSuchPaddingException,
        InvalidKeyException, InvalidAlgorithmParameterException,
        IllegalBlockSizeException, BadPaddingException {
        
        return criptografia( Cipher.DECRYPT_MODE, conteudo, transformacao, chave, iv );
        
    }
    
    /**
     * {@link Cipher Decodifica} um conteúdo utilizando "RSA/ECB/PKCS1Padding", se chave assimétrica, ou "AES/CBC/PKCS5Padding", se chave simétrica.
     * @see #decodificar(byte[], String, Key, IvParameterSpec)
     */
    public static byte[] decodificar( byte[] conteudo, Key chave, IvParameterSpec iv )
        throws
        NoSuchAlgorithmException, NoSuchPaddingException,
        InvalidKeyException, InvalidAlgorithmParameterException,
        IllegalBlockSizeException, BadPaddingException {
        
        if( chave instanceof PublicKey || chave instanceof PrivateKey ){
            return decodificar( conteudo, "RSA/ECB/PKCS1Padding", chave, iv );
        }else{
            return decodificar( conteudo, "AES/CBC/PKCS5Padding", chave, iv );
        }
        
    }
    
}
