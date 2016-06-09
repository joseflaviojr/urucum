
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

package com.joseflavio.urucum.javabeans;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * Utilitários para JavaBeans.
 * @author José Flávio de Souza Dias Júnior
 */
public class JavaBeansUtil {

	/**
	 * Procura por um {@link Field} de uma {@link Class}, incluindo a hierarquia desta.
	 * @return <code>null</code>, se não encontrar o {@link Field}.
	 */
	public static Field procurarCampo( Class<?> classe, String nome ) {
		
		Field campo = null;
		
		while( classe != null ){
			try{
				return classe.getDeclaredField( nome );
			}catch( Exception e ){
			}
			classe = classe.getSuperclass();
		}
		
		return campo;
		
	}
	
	/**
	 * Procura pelo {@link Method} "get" correspondente a um {@link Field} de uma {@link Class}, incluindo a hierarquia desta.
	 * @return <code>null</code>, se não encontrar o {@link Field} ou o {@link Method}.
	 */
	public static Method procurarGet( Class<?> classe, String campoNome ) {
		
		Field campo = procurarCampo( classe, campoNome );
		if( campo == null ) return null;
		Class<?> tipo = campo.getType();

		Method metodo = null;
		String prefixo = tipo == boolean.class || tipo == Boolean.class ? "is" : "get";
		String metodoNome = prefixo + Character.toUpperCase( campoNome.charAt( 0 ) ) + campoNome.substring( 1 );
		
		while( classe != null ){
			try{
				return classe.getDeclaredMethod( metodoNome );
			}catch( Exception e ){
			}
			classe = classe.getSuperclass();
		}
		
		return metodo;
		
	}
	
	/**
	 * Procura pelo {@link Method} "get" correspondente a um {@link Field}, a partir da {@link Field#getDeclaringClass()}.
	 * @return <code>null</code>, se não encontrar o {@link Method}.
	 */
	public static Method procurarGet( Field campo ) {
		
		Class<?> classe = campo.getDeclaringClass();
		
		Class<?> tipo = campo.getType();
		String campoNome = campo.getName();

		Method metodo = null;
		String prefixo = tipo == boolean.class || tipo == Boolean.class ? "is" : "get";
		String metodoNome = prefixo + Character.toUpperCase( campoNome.charAt( 0 ) ) + campoNome.substring( 1 );
		
		while( classe != null ){
			try{
				return classe.getDeclaredMethod( metodoNome );
			}catch( Exception e ){
			}
			classe = classe.getSuperclass();
		}
		
		return metodo;
		
	}
	
	/**
	 * Procura pelo {@link Method} "set" correspondente a um {@link Field} de uma {@link Class}, incluindo a hierarquia desta.
	 * @return <code>null</code>, se não encontrar o {@link Field} ou o {@link Method}.
	 */
	public static Method procurarSet( Class<?> classe, String campoNome ) {
		
		Field campo = procurarCampo( classe, campoNome );
		if( campo == null ) return null;
		Class<?> tipo = campo.getType();

		Method metodo = null;
		String metodoNome = "set" + Character.toUpperCase( campoNome.charAt( 0 ) ) + campoNome.substring( 1 );
		
		while( classe != null ){
			try{
				return classe.getDeclaredMethod( metodoNome, tipo );
			}catch( Exception e ){
			}
			classe = classe.getSuperclass();
		}
		
		return metodo;
		
	}
	
	/**
	 * Procura pelo {@link Method} "set" correspondente a um {@link Field}, a partir da {@link Field#getDeclaringClass()}.
	 * @return <code>null</code>, se não encontrar o {@link Method}.
	 */
	public static Method procurarSet( Field campo ) {
		
		Class<?> classe = campo.getDeclaringClass();
		
		Class<?> tipo = campo.getType();
		String campoNome = campo.getName();
		
		Method metodo = null;
		String metodoNome = "set" + Character.toUpperCase( campoNome.charAt( 0 ) ) + campoNome.substring( 1 );
		
		while( classe != null ){
			try{
				return classe.getDeclaredMethod( metodoNome, tipo );
			}catch( Exception e ){
			}
			classe = classe.getSuperclass();
		}
		
		return metodo;
		
	}
	
	/**
	 * Retorna todos os {@link Field}s de uma {@link Class}, incluindo herdados.
	 * @param classe Classe base.
	 * @see Class#getDeclaredFields()
	 */
	public static Set<Field> getCampos( Class<?> classe ) {
		
		Set<Field> campos = new HashSet<Field>();
		
		while( classe != null ){
			for( Field campo : classe.getDeclaredFields() ){
				campos.add( campo );
			}
			classe = classe.getSuperclass();
		}
		
		return campos;
		
	}
	
}
