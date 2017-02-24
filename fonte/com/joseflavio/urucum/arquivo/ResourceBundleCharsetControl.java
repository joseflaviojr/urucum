
/*
 *  Copyright (C) 2016 José Flávio de Souza Dias Júnior
 *
 *  This file is part of Urucum - <http://www.joseflavio.com/urucum/>.
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
 *  Direitos Autorais Reservados (C) 2016 José Flávio de Souza Dias Júnior
 *
 *  Este arquivo é parte de Urucum - <http://www.joseflavio.com/urucum/>.
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

package com.joseflavio.urucum.arquivo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * {@link PropertyResourceBundle} em {@link Charset} específico.
 * @author José Flávio de Souza Dias Júnior
 * @see ResourceBundle#getBundle(String, Locale, ResourceBundle.Control)
 */
public class ResourceBundleCharsetControl extends ResourceBundle.Control {
    
    private String charset;
    
    /**
     * @param charset {@link Charset} dos {@link PropertyResourceBundle}'s. null == "UTF-8".
     */
    public ResourceBundleCharsetControl( String charset ) {
        this.charset = charset != null ? charset : "UTF-8";
    }
    
    /**
     * {@link Charset} "UTF-8"
     */
    public ResourceBundleCharsetControl() {
        this( "UTF-8" );
    }
    
    @Override
    public ResourceBundle newBundle( String baseName, Locale locale, String format, ClassLoader loader, boolean reload ) throws IllegalAccessException, InstantiationException, IOException {
        
        // Adaptação do código original
    
        String bundleName = toBundleName( baseName, locale );
        ResourceBundle bundle = null;
        
        if( format.equals( "java.class" ) ){
            
            try{
                
                @SuppressWarnings("unchecked")
                Class<? extends ResourceBundle> bundleClass = (Class<? extends ResourceBundle>) loader.loadClass( bundleName );
            
                if( ResourceBundle.class.isAssignableFrom( bundleClass ) ){
                    bundle = bundleClass.newInstance();
                }else{
                    throw new ClassCastException( bundleClass.getName() + " cannot be cast to ResourceBundle" );
                }
                
            }catch( ClassNotFoundException e ){
            }
            
        }else if( format.equals( "java.properties" ) ){
            
            final String resourceName = toResourceName0( bundleName, "properties" );
            
            if( resourceName == null ){
                return bundle;
            }
            
            final ClassLoader classLoader = loader;
            final boolean reloadFlag = reload;
            InputStream stream = null;
            
            try{
                
                stream = AccessController.doPrivileged(
                    new PrivilegedExceptionAction<InputStream>() {
                        public InputStream run() throws IOException {
                            InputStream is = null;
                            if( reloadFlag ){
                                URL url = classLoader.getResource( resourceName );
                                if( url != null ){
                                    URLConnection connection = url.openConnection();
                                    if( connection != null ){
                                        connection.setUseCaches( false );
                                        is = connection.getInputStream();
                                    }
                                }
                            }else{
                                is = classLoader.getResourceAsStream( resourceName );
                            }
                            return is;
                        }
                    }
                );
                
            }catch( PrivilegedActionException e ){
                throw (IOException) e.getException();
            }
            
            if( stream != null ){
                try{
                    bundle = new PropertyResourceBundle( new InputStreamReader( stream, charset ) );
                }finally{
                    stream.close();
                }
            }
            
        }else{
            throw new IllegalArgumentException( "unknown format: " + format );
        }
        
        return bundle;
        
    }
    
    private String toResourceName0( String bundleName, String suffix ) {
        if( bundleName.contains( "://" ) ){
            return null;
        }else{
            return toResourceName( bundleName, suffix );
        }
    }
    
    public String getCharset() {
        return charset;
    }
    
    public void setCharset( String charset ) {
        this.charset = charset;
    }
    
}
