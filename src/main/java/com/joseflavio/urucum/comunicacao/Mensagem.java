
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

package com.joseflavio.urucum.comunicacao;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

/**
 * Mensagem de {@link Resposta}.
 * @author José Flávio de Souza Dias Júnior
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
	 * @param tipo O {@link Tipo} não deve ser <code>null</code>.
	 */
	public Mensagem setTipo( Tipo tipo ) {
		if( tipo == null ) throw new IllegalArgumentException( "tipo" );
		this.tipo = tipo;
		return this;
	}
	
	/**
	 * Identificação do objeto/função ao qual esta {@link Mensagem} se refere, podendo ser <code>null</code>.
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
		 * {@link String Texto} que ratifica uma atividade finalizada com êxito.
		 * @see Resposta#isExito()
		 */
		EXITO,
		
		/**
		 * {@link String Texto} informativo.
		 */
		INFORMACAO,
		
		/**
		 * {@link String Texto} que alerta sobre algo que ainda não derivou um {@link Tipo#ERRO erro}.
		 */
		ATENCAO,
		
		/**
		 * {@link String Texto} sobre uma ocorrência de erro, normalmente impeditiva de {@link Resposta#isExito() êxito}.
		 */
		ERRO,
		
		/**
		 * Ação da qual se deseja a execução. Consiste normalmente na chamada de um método/função,
		 * especificado por {@link Mensagem#getReferencia()}, para o qual
		 * se repassa o {@link Mensagem#getArgumento()}.
		 */
		ACAO
		
	}
	
}
