
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * {@link Transferidor} padr�o.<br>
 * Copia o conte�do de todos as propriedades, efetivamente transfer�veis, do objeto de origem para o de destino, com ou sem {@link #clone()}.<br>
 * Caso haja incompatibilidade de tipos entre a origem e o destino, tentar-se-� convers�o com {@link Transformador}.
 * @author Jos� Fl�vio de Souza Dias J�nior
 */
public class PadraoTransferidor implements Transferidor {

	private Transformador transformador;
	
	private boolean usarClone = false;
	
	private boolean considerarTransient = false;
	
	private boolean considerarVolatile = false;
	
	/**
	 * @param transformador Veja {@link #setTransformador(Transformador)}
	 * @param usarClone Veja {@link #setUsarClone(boolean)}
	 */
	public PadraoTransferidor( Transformador transformador, boolean usarClone ) {
		this.transformador = transformador;
		this.usarClone = usarClone;
	}
	
	public PadraoTransferidor( Transformador transformador ) {
		this( transformador, false );
	}
	
	public PadraoTransferidor() {
		this( null, false );
	}

	@Override
	public void transferir( Object origem, Object destino ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		
		if( origem == null || destino == null ) throw new IllegalArgumentException( "null" );
		
		Class<?> oclasse = origem.getClass();
		Class<?> dclasse = destino.getClass();
		
		while( oclasse != null ){
			
			for( Field ocampo : oclasse.getDeclaredFields() ){
				
				String nome = ocampo.getName();
				int mod = ocampo.getModifiers();
				
				if( Modifier.isTransient( mod ) && ! considerarTransient ) continue;
				if( Modifier.isVolatile( mod ) && ! considerarVolatile ) continue;
				
				Method ometodo = JavaBeansUtil.procurarGet( ocampo );
				if( ometodo == null ) continue;
				
				Method dmetodo = JavaBeansUtil.procurarSet( dclasse, nome );
				if( dmetodo == null ) continue;
				
				Object   valor = ometodo.invoke( origem );
				Class<?> otipo = ometodo.getReturnType();
				Class<?> dtipo = dmetodo.getParameterTypes()[0];
				
				if( ! dtipo.isAssignableFrom( otipo ) ){
					if( transformador != null ){
						valor = transformador.transformar( valor, dtipo );
					}else{
						throw new IllegalArgumentException(
							"Incompatibilidade entre " +
							otipo.getName() +
							" e " +
							dtipo.getName()
						);
					}
				}
				
				if( usarClone && valor != null && ! valor.getClass().isPrimitive() ){
					try{
						valor = valor.getClass().getMethod( "clone" ).invoke( valor );
					}catch( NoSuchMethodException e ){
						throw new IllegalArgumentException( "N�o clon�vel: " + valor );
					}
				}
				
				dmetodo.invoke( destino, valor );
				
			}
			
			oclasse = oclasse.getSuperclass();
			
		}
		
	}
	
	/**
	 * @see #setTransformador(Transformador)
	 */
	public Transformador getTransformador() {
		return transformador;
	}
	
	/**
	 * {@link Transformador} auxiliar, para converter propriedades incompat�veis. Opcional.
	 */
	public void setTransformador( Transformador transformador ) {
		this.transformador = transformador;
	}
	
	/**
	 * @see #setUsarClone(boolean)
	 */
	public boolean isUsarClone() {
		return usarClone;
	}
	
	/**
	 * Determina o uso de {@link #clone()} durante a transfer�ncia dos atributos.
	 */
	public void setUsarClone( boolean usarClone ) {
		this.usarClone = usarClone;
	}
	
	public boolean isConsiderarTransient() {
		return considerarTransient;
	}
	
	public void setConsiderarTransient( boolean considerarTransient ) {
		this.considerarTransient = considerarTransient;
	}
	
	public boolean isConsiderarVolatile() {
		return considerarVolatile;
	}
	
	public void setConsiderarVolatile( boolean considerarVolatile ) {
		this.considerarVolatile = considerarVolatile;
	}

}
