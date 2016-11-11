
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

package com.joseflavio.urucum.json;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Decoração de {@link JSONObject}.
 * @author José Flávio de Souza Dias Júnior
 */
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
	
}
