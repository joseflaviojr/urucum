
/*
 *  Copyright (C) 2016-2018 Jos� Fl�vio de Souza Dias J�nior
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
 *  Direitos Autorais Reservados (C) 2016-2018 Jos� Fl�vio de Souza Dias J�nior
 *
 *  Este arquivo � parte de Urucum - <http://www.joseflavio.com/urucum/>.
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

package com.joseflavio.urucum.json;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.reflect.ReflectPermission;

/**
 * Utilit�rios para {@link JSON}.
 * @author Jos� Fl�vio de Souza Dias J�nior
 */
public class JSONUtil {
    
    /**
     * {@link ObjectMapper} padr�o para trabalhar com {@link JSON}.
     * @see #converter(String, Class, ObjectMapper)
     */
    public static ObjectMapper novoConversor() {
        
        ObjectMapper conversor = new ObjectMapper();
        
        conversor.configure( DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true );
        conversor.configure( DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true );
        conversor.configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );
        conversor.configure( DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS, true );
        
        SecurityManager sm = System.getSecurityManager();
        if( sm != null ){
            try{
                sm.checkPermission( new ReflectPermission( "suppressAccessChecks" ) );
            }catch( Exception e ){
                conversor.disable( MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS );
            }
        }
        
        return conversor;
        
    }
    
    /**
     * Converte um {@link JSON} para {@link Object},
     * atrav�s do processo de desserializa��o do {@link ObjectMapper}.
     * @see #novoConversor()
     * @see ObjectMapper#readValue(String, Class)
     */
    public static <T> T converter( String json, Class<T> classe, ObjectMapper conversor ) throws IOException, JsonParseException, JsonMappingException {
        return conversor.readValue( json, classe );
    }
    
    /**
     * @see #converter(String, Class, ObjectMapper)
     */
    public static <T> T converter( String json, Class<T> classe ) throws IOException, JsonParseException, JsonMappingException {
        return converter( json, classe, novoConversor() );
    }
    
    /**
     * @see #converter(String, Class, ObjectMapper)
     */
    public static <T> T converter( JSON json, Class<T> classe, ObjectMapper conversor ) throws IOException, JsonParseException, JsonMappingException {
        return converter( json.toString(), classe, conversor );
    }
    
    /**
     * @see #converter(String, Class, ObjectMapper)
     */
    public static <T> T converter( JSON json, Class<T> classe ) throws IOException, JsonParseException, JsonMappingException {
        return converter( json.toString(), classe, novoConversor() );
    }
    
    /**
     * Converte um {@link Object} para {@link JSON},
     * atrav�s do processo de serializa��o do {@link ObjectMapper}.
     * @see #novoConversor()
     * @see ObjectMapper#writeValueAsString(Object)
     */
    public static String converter( Object objeto, ObjectMapper conversor ) throws JsonProcessingException {
        return conversor.writeValueAsString( objeto );
    }
    
    /**
     * @see #converter(Object, ObjectMapper)
     */
    public static String converter( Object objeto ) throws JsonProcessingException {
        return converter( objeto, novoConversor() );
    }
    
}
