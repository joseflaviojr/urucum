
/*
 *  Copyright (C) 2016 Jos� Fl�vio de Souza Dias J�nior
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
 *  Direitos Autorais Reservados (C) 2016 Jos� Fl�vio de Souza Dias J�nior
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

package com.joseflavio.urucum.javabeans;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * Utilit�rios para JavaBeans.
 * @author Jos� Fl�vio de Souza Dias J�nior
 */
public class JavaBeansUtil {

	/**
	 * Procura por um {@link Field} de uma {@link Class}, incluindo a hierarquia desta.
	 * @return <code>null</code>, se n�o encontrar o {@link Field}.
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
	 * @return <code>null</code>, se n�o encontrar o {@link Field} ou o {@link Method}.
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
	 * @return <code>null</code>, se n�o encontrar o {@link Method}.
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
	 * @return <code>null</code>, se n�o encontrar o {@link Field} ou o {@link Method}.
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
	 * @return <code>null</code>, se n�o encontrar o {@link Method}.
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
