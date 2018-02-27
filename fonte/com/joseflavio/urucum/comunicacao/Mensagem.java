
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

package com.joseflavio.urucum.comunicacao;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

/**
 * Mensagem de {@link Resposta}.
 * @author Jos� Fl�vio de Souza Dias J�nior
 */
public class Mensagem implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Tipo tipo;
	
	private String referencia;
	
	@JsonTypeInfo(use=Id.CLASS, include=As.PROPERTY, property="@classe")
	private Serializable argumento;
	
	/**
	 * @param tipo Veja {@link #getTipo()}
	 * @param referencia Veja {@link #getReferencia()}
	 * @param argumento Veja {@link #getArgumento()}
	 */
	public Mensagem( Tipo tipo, String referencia, Serializable argumento ) {
		if( tipo == null ) throw new IllegalArgumentException( "tipo" );
		this.tipo = tipo;
		this.referencia = referencia;
		this.argumento = argumento;
	}
	
	public Mensagem( Tipo tipo, Serializable argumento ) {
		this( tipo, null, argumento );
	}
	
	public Mensagem( String referencia, Serializable argumento ) {
		this( Tipo.ERRO, referencia, argumento );
	}
	
	public Mensagem( Serializable argumento ) {
		this( Tipo.ERRO, null, argumento );
	}
	
	public Mensagem() {
		this( Tipo.ERRO, null, null );
	}
	
	/**
	 * {@link Tipo} da {@link Mensagem}, nunca <code>null</code>.
	 */
	public Tipo getTipo() {
		return tipo;
	}
	
	/**
	 * @param tipo O {@link Tipo} n�o deve ser <code>null</code>.
	 */
	public Mensagem setTipo( Tipo tipo ) {
		if( tipo == null ) throw new IllegalArgumentException( "tipo" );
		this.tipo = tipo;
		return this;
	}
	
	/**
	 * Identifica��o do objeto/fun��o ao qual esta {@link Mensagem} se refere, podendo ser <code>null</code>.
	 */
	public String getReferencia() {
		return referencia;
	}

	public Mensagem setReferencia( String referencia ) {
		this.referencia = referencia;
		return this;
	}

	/**
	 * Argumento que especifica a {@link Mensagem}, podendo ser <code>null</code>.<br>
	 * Normalmente uma {@link String}.
	 */
	public Serializable getArgumento() {
		return argumento;
	}
	
	public Mensagem setArgumento( Serializable argumento ) {
		this.argumento = argumento;
		return this;
	}

	/**
	 * Tipo de {@link Mensagem}.
	 */
	public static enum Tipo {
		
		/**
		 * {@link String Texto} que ratifica uma atividade finalizada com �xito.
		 * @see Resposta#isExito()
		 */
		EXITO,
		
		/**
		 * {@link String Texto} informativo.
		 */
		INFORMACAO,
		
		/**
		 * {@link String Texto} que alerta sobre algo que ainda n�o derivou um {@link Tipo#ERRO erro}.
		 */
		ATENCAO,
		
		/**
		 * {@link String Texto} sobre uma ocorr�ncia de erro, normalmente impeditiva de {@link Resposta#isExito() �xito}.
		 */
		ERRO,
		
		/**
		 * A��o da qual se deseja a execu��o. Consiste normalmente na chamada de um m�todo/fun��o,
		 * especificado por {@link Mensagem#getReferencia()}, para o qual
		 * se repassa o {@link Mensagem#getArgumento()}.
		 */
		ACAO
		
	}
	
}
