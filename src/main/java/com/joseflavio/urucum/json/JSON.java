
/*
 *  Copyright (C) 2016-2020 José Flávio de Souza Dias Júnior
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
 *  Direitos Autorais Reservados (C) 2016-2020 José Flávio de Souza Dias Júnior
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

package com.joseflavio.urucum.json;

import java.io.Serializable;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONPointer;
import org.json.JSONTokener;

//TODO Revisar sempre: herdar todos os métodos possíveis.

/**
 * Decoração e otimização de {@link JSONObject}.
 * @author José Flávio de Souza Dias Júnior
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.NONE)
@JsonSerialize(using=JSONSerializador.class)
@JsonDeserialize(using=JSONDesserializador.class)
public class JSON extends JSONObject implements Serializable {

	private static final long serialVersionUID = 1L;

	private transient String fonte;

	private transient boolean semente = false;

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
	 * Constrói um {@link JSON} com base num objeto especificado textualmente.
	 * Por questões de desempenho, se assim desejar, a conversão efetiva ocorrerá somente quando
	 * algum método desta classe for chamado, com exceção de {@link #toString()}, que
	 * inicialmente poderá retornar o texto-fonte fornecido. No caso de não optar por conversão imediata,
	 * deve-se garantir que o texto-fonte esteja correto, pois a verificação será tardia.
	 * @param fonte Texto-fonte do objeto.
	 * @param conversaoImediata Realizar conversão imediata do texto-fonte para {@link JSON}?
	 * @see JSONObject#JSONObject(String)
	 */
	public JSON( String fonte, boolean conversaoImediata ) throws JSONException {

		this.fonte = fonte;
		this.semente = true;

		if( conversaoImediata ) germinar();

	}

	/**
	 * {@link JSON#JSON(String, boolean)} sem conversão imediata.
	 */
	public JSON( String fonte ) throws JSONException {
		this( fonte, false );
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
	 * É feita uma inclusão direta, sem realizar {@link Object#clone() clone} ou transformação.
	 */
	public JSON( JSONObject json ) {
		for( String k : json.keySet() ){
			super.put( k, json.get( k ) );
		}
	}

	private void germinar() throws JSONException {
		if( semente ){
			semente = false;
			germinar( new JSONTokener(fonte) );
			fonte = null;
		}
	}

	/**
	 * Mesmo comportamento de {@link JSON#JSON(JSONTokener)}.
	 */
	private void germinar( JSONTokener x ) throws JSONException {

		char c;
		String key;

		if (x.nextClean() != '{') {
			throw x.syntaxError("A JSONObject text must begin with '{'");
		}
		for (;;) {
			c = x.nextClean();
			switch (c) {
				case 0:
					throw x.syntaxError("A JSONObject text must end with '}'");
				case '}':
					return;
				default:
					x.back();
					key = x.nextValue().toString();
			}

			// The key is followed by ':'.

			c = x.nextClean();
			if (c != ':') {
				throw x.syntaxError("Expected a ':' after a key");
			}

			// Use syntaxError(..) to include error location

			if (key != null) {
				// Check if key exists
				if (this.opt(key) != null) {
					// key already exists
					throw x.syntaxError("Duplicate key \"" + key + "\"");
				}
				// Only add value if non-null
				Object value = x.nextValue();
				if (value!=null) {
					this.put(key, value);
				}
			}

			// Pairs are separated by ','.

			switch (x.nextClean()) {
				case ';':
				case ',':
					if (x.nextClean() == '}') {
						return;
					}
					x.back();
					break;
				case '}':
					return;
				default:
					throw x.syntaxError("Expected a ',' or '}'");
			}
		}

	}
	
	@Override
	public JSON accumulate( String key, Object value ) throws JSONException {
		if( semente ) germinar();
		return (JSON) super.accumulate( key, value );
	}
	
	@Override
	public JSON append( String key, Object value ) throws JSONException {
		if( semente ) germinar();
		return (JSON) super.append( key, value );
	}
	
	@Override
	public JSON increment( String key ) throws JSONException {
		if( semente ) germinar();
		return (JSON) super.increment( key );
	}
	
	@Override
	public JSON put( String key, boolean value ) throws JSONException {
		if( semente ) germinar();
		return (JSON) super.put( key, value );
	}
	
	@Override
	public JSON put( String key, Collection<?> value ) throws JSONException {
		if( semente ) germinar();
		return (JSON) super.put( key, value );
	}
	
	@Override
	public JSON put( String key, double value ) throws JSONException {
		if( semente ) germinar();
		return (JSON) super.put( key, value );
	}
	
	@Override
	public JSON put( String key, int value ) throws JSONException {
		if( semente ) germinar();
		return (JSON) super.put( key, value );
	}
	
	@Override
	public JSON put( String key, long value ) throws JSONException {
		if( semente ) germinar();
		return (JSON) super.put( key, value );
	}
	
	@Override
	public JSON put( String key, Map<?,?> value ) throws JSONException {
		if( semente ) germinar();
		return (JSON) super.put( key, value );
	}
	
	@Override
	public JSON put( String key, Object value ) throws JSONException {
		if( semente ) germinar();
		return (JSON) super.put( key, value );
	}
	
	@Override
	public JSON putOnce( String key, Object value ) throws JSONException {
		if( semente ) germinar();
		return (JSON) super.putOnce( key, value );
	}
	
	@Override
	public JSON putOpt( String key, Object value ) throws JSONException {
		if( semente ) germinar();
		return (JSON) super.putOpt( key, value );
	}
	
	/**
	 * Converte {@link #getJSONObject(String)} para {@link JSON}.
	 */
	public JSON getJSON( String key ) throws JSONException {
		if( semente ) germinar();
		return new JSON( super.getJSONObject( key ) );
	}
	
	/**
	 * Converte {@link #optJSONObject(String)} para {@link JSON}.
	 */
	public JSON optJSON( String key ) {
		if( semente ) germinar();
		JSONObject o = super.optJSONObject( key );
		return o != null ? new JSON( o ) : null;
	}

	@Override
	public Object get(String key) throws JSONException {
		if( semente ) germinar();
		return super.get(key);
	}

	@Override
	public <E extends Enum<E>> E getEnum(Class<E> clazz, String key) throws JSONException {
		if( semente ) germinar();
		return super.getEnum(clazz, key);
	}

	@Override
	public boolean getBoolean(String key) throws JSONException {
		if( semente ) germinar();
		return super.getBoolean(key);
	}

	@Override
	public BigInteger getBigInteger(String key) throws JSONException {
		if( semente ) germinar();
		return super.getBigInteger(key);
	}

	@Override
	public BigDecimal getBigDecimal(String key) throws JSONException {
		if( semente ) germinar();
		return super.getBigDecimal(key);
	}

	@Override
	public double getDouble(String key) throws JSONException {
		if( semente ) germinar();
		return super.getDouble(key);
	}

	@Override
	public float getFloat(String key) throws JSONException {
		if( semente ) germinar();
		return super.getFloat(key);
	}

	@Override
	public Number getNumber(String key) throws JSONException {
		if( semente ) germinar();
		return super.getNumber(key);
	}

	@Override
	public int getInt(String key) throws JSONException {
		if( semente ) germinar();
		return super.getInt(key);
	}

	@Override
	public JSONArray getJSONArray(String key) throws JSONException {
		if( semente ) germinar();
		return super.getJSONArray(key);
	}

	@Override
	public JSONObject getJSONObject(String key) throws JSONException {
		if( semente ) germinar();
		return super.getJSONObject(key);
	}

	@Override
	public long getLong(String key) throws JSONException {
		if( semente ) germinar();
		return super.getLong(key);
	}

	@Override
	public String getString(String key) throws JSONException {
		if( semente ) germinar();
		return super.getString(key);
	}

	@Override
	public boolean has(String key) {
		if( semente ) germinar();
		return super.has(key);
	}

	@Override
	public boolean isNull(String key) {
		if( semente ) germinar();
		return super.isNull(key);
	}

	@Override
	public Iterator<String> keys() {
		if( semente ) germinar();
		return super.keys();
	}

	@Override
	public Set<String> keySet() {
		if( semente ) germinar();
		return super.keySet();
	}

	@Override
	protected Set<Map.Entry<String, Object>> entrySet() {
		if( semente ) germinar();
		return super.entrySet();
	}

	@Override
	public int length() {
		if( semente ) germinar();
		return super.length();
	}

	@Override
	public boolean isEmpty() {
		if( semente ) germinar();
		return super.isEmpty();
	}

	@Override
	public JSONArray names() {
		if( semente ) germinar();
		return super.names();
	}

	@Override
	public Object opt(String key) {
		if( semente ) germinar();
		return super.opt(key);
	}

	@Override
	public <E extends Enum<E>> E optEnum(Class<E> clazz, String key) {
		if( semente ) germinar();
		return super.optEnum(clazz, key);
	}

	@Override
	public <E extends Enum<E>> E optEnum(Class<E> clazz, String key, E defaultValue) {
		if( semente ) germinar();
		return super.optEnum(clazz, key, defaultValue);
	}

	@Override
	public boolean optBoolean(String key) {
		if( semente ) germinar();
		return super.optBoolean(key);
	}

	@Override
	public boolean optBoolean(String key, boolean defaultValue) {
		if( semente ) germinar();
		return super.optBoolean(key, defaultValue);
	}

	@Override
	public BigDecimal optBigDecimal(String key, BigDecimal defaultValue) {
		if( semente ) germinar();
		return super.optBigDecimal(key, defaultValue);
	}

	@Override
	public BigInteger optBigInteger(String key, BigInteger defaultValue) {
		if( semente ) germinar();
		return super.optBigInteger(key, defaultValue);
	}

	@Override
	public double optDouble(String key) {
		if( semente ) germinar();
		return super.optDouble(key);
	}

	@Override
	public double optDouble(String key, double defaultValue) {
		if( semente ) germinar();
		return super.optDouble(key, defaultValue);
	}

	@Override
	public float optFloat(String key) {
		if( semente ) germinar();
		return super.optFloat(key);
	}

	@Override
	public float optFloat(String key, float defaultValue) {
		if( semente ) germinar();
		return super.optFloat(key, defaultValue);
	}

	@Override
	public int optInt(String key) {
		if( semente ) germinar();
		return super.optInt(key);
	}

	@Override
	public int optInt(String key, int defaultValue) {
		if( semente ) germinar();
		return super.optInt(key, defaultValue);
	}

	@Override
	public JSONArray optJSONArray(String key) {
		if( semente ) germinar();
		return super.optJSONArray(key);
	}

	@Override
	public JSONObject optJSONObject(String key) {
		if( semente ) germinar();
		return super.optJSONObject(key);
	}

	@Override
	public long optLong(String key) {
		if( semente ) germinar();
		return super.optLong(key);
	}

	@Override
	public long optLong(String key, long defaultValue) {
		if( semente ) germinar();
		return super.optLong(key, defaultValue);
	}

	@Override
	public Number optNumber(String key) {
		if( semente ) germinar();
		return super.optNumber(key);
	}

	@Override
	public Number optNumber(String key, Number defaultValue) {
		if( semente ) germinar();
		return super.optNumber(key, defaultValue);
	}

	@Override
	public String optString(String key) {
		if( semente ) germinar();
		return super.optString(key);
	}

	@Override
	public String optString(String key, String defaultValue) {
		if( semente ) germinar();
		return super.optString(key, defaultValue);
	}

	@Override
	public JSON put(String key, float value) throws JSONException {
		if( semente ) germinar();
		super.put(key, value);
		return this;
	}

	@Override
	public Object query(String jsonPointer) {
		if( semente ) germinar();
		return super.query(jsonPointer);
	}

	@Override
	public Object query(JSONPointer jsonPointer) {
		if( semente ) germinar();
		return super.query(jsonPointer);
	}

	@Override
	public Object optQuery(String jsonPointer) {
		if( semente ) germinar();
		return super.optQuery(jsonPointer);
	}

	@Override
	public Object optQuery(JSONPointer jsonPointer) {
		if( semente ) germinar();
		return super.optQuery(jsonPointer);
	}

	@Override
	public Object remove(String key) {
		if( semente ) germinar();
		return super.remove(key);
	}

	@Override
	public boolean similar(Object other) {
		if( semente ) germinar();
		return super.similar(other);
	}

	@Override
	public JSONArray toJSONArray(JSONArray names) throws JSONException {
		if( semente ) germinar();
		return super.toJSONArray(names);
	}

	@Override
	public String toString() {
		return semente ? fonte : super.toString();
	}

	@Override
	public String toString(int indentFactor) throws JSONException {
		if( semente ) germinar();
		return super.toString(indentFactor);
	}

	@Override
	public Writer write(Writer writer) throws JSONException {
		if( semente ) germinar();
		return super.write(writer);
	}

	@Override
	public Writer write(Writer writer, int indentFactor, int indent) throws JSONException {
		if( semente ) germinar();
		return super.write(writer, indentFactor, indent);
	}

	@Override
	public Map<String, Object> toMap() {
		if( semente ) germinar();
		return super.toMap();
	}

	@Override
	public int hashCode() {
		if( semente ) germinar();
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if( semente ) germinar();
		return super.equals(obj);
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		if( semente ) germinar();
		return super.clone();
	}

}
