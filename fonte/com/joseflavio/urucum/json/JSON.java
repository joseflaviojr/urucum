
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

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.Serializable;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

/**
 * Decora��o de {@link JSONObject}.
 * @author Jos� Fl�vio de Souza Dias J�nior
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.NONE)
@JsonSerialize(using=JSONSerializador.class)
@JsonDeserialize(using=JSONDesserializador.class)
public class JSON extends JSONObject implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * @see JSONObject#JSONObject()
	 */
	public JSON() {
	}

	/**
	 * @see JSONObject#JSONObject(JSONTokener)
	 */
	public JSON( JSONTokener x ) throws JSONException {
		super( x );
	}

	/**
	 * @see JSONObject#JSONObject(Map)
	 */
	public JSON( Map<?,?> map ) {
		super( map );
	}

	/**
	 * @see JSONObject#JSONObject(Object)
	 */
	public JSON( Object bean ) {
		super( bean );
	}

	/**
	 * @see JSONObject#JSONObject(String)
	 */
	public JSON( String source ) throws JSONException {
		super( source );
	}

	/**
	 * @see JSONObject#JSONObject(JSONObject, String[])
	 */
	public JSON( JSONObject jo, String[] names ) {
		super( jo, names );
	}

	/**
	 * @see JSONObject#JSONObject(Object, String[])
	 */
	public JSON( Object object, String[] names ) {
		super( object, names );
	}

	/**
	 * @see JSONObject#JSONObject(String, Locale)
	 */
	public JSON( String baseName, Locale locale ) throws JSONException {
		super( baseName, locale );
	}
	
	/**
	 * {@link JSON} com as mesmas chaves e valores contidos no {@link JSONObject} indicado.<br>
	 * � feita uma inclus�o direta, sem realizar {@link Object#clone() clone} ou transforma��o.
	 */
	public JSON( JSONObject json ) {
		for( String k : json.keySet() ){
			super.put( k, json.get( k ) );
		}
	}
	
	@Override
	public JSON accumulate( String key, Object value ) throws JSONException {
		return (JSON) super.accumulate( key, value );
	}
	
	@Override
	public JSON append( String key, Object value ) throws JSONException {
		return (JSON) super.append( key, value );
	}
	
	@Override
	public JSON increment( String key ) throws JSONException {
		return (JSON) super.increment( key );
	}
	
	@Override
	public JSON put( String key, boolean value ) throws JSONException {
		return (JSON) super.put( key, value );
	}
	
	@Override
	public JSON put( String key, Collection<?> value ) throws JSONException {
		return (JSON) super.put( key, value );
	}
	
	@Override
	public JSON put( String key, double value ) throws JSONException {
		return (JSON) super.put( key, value );
	}
	
	@Override
	public JSON put( String key, int value ) throws JSONException {
		return (JSON) super.put( key, value );
	}
	
	@Override
	public JSON put( String key, long value ) throws JSONException {
		return (JSON) super.put( key, value );
	}
	
	@Override
	public JSON put( String key, Map<?,?> value ) throws JSONException {
		return (JSON) super.put( key, value );
	}
	
	@Override
	public JSON put( String key, Object value ) throws JSONException {
		return (JSON) super.put( key, value );
	}
	
	@Override
	public JSON putOnce( String key, Object value ) throws JSONException {
		return (JSON) super.putOnce( key, value );
	}
	
	@Override
	public JSON putOpt( String key, Object value ) throws JSONException {
		return (JSON) super.putOpt( key, value );
	}
	
	/**
	 * Converte {@link #getJSONObject(String)} para {@link JSON}.
	 */
	public JSON getJSON( String key ) throws JSONException {
		return new JSON( super.getJSONObject( key ) );
	}
	
	/**
	 * Converte {@link #optJSONObject(String)} para {@link JSON}.
	 */
	public JSON optJSON( String key ) {
		JSONObject o = super.optJSONObject( key );
		return o != null ? new JSON( o ) : null;
	}
	
}
