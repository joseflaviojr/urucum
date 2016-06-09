
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

package com.joseflavio.urucum.comunicacao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.joseflavio.urucum.comunicacao.Mensagem.Tipo;

/**
 * Padr�o de retorno de servi�os.
 * @author Jos� Fl�vio de Souza Dias J�nior
 */
public class Resposta <T extends Serializable> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private boolean exito;
	
	private T resultado;
	
	private List<Mensagem> mensagens;

	/**
	 * @param exito Veja {@link #isExito()}
	 * @param resultado Veja {@link #getResultado()}
	 * @param mensagens Veja {@link #setMensagens(List)}
	 */
	public Resposta( boolean exito, T resultado, List<Mensagem> mensagens ) {
		this.exito = exito;
		this.resultado = resultado;
		this.mensagens = mensagens != null ? mensagens : new ArrayList<Mensagem>();
	}
	
	public Resposta( boolean exito, T resultado, Mensagem mensagem, Mensagem... outras ) {
		this( exito, resultado, null );
		mais( mensagem, outras );
	}
	
	public Resposta( boolean exito, T resultado ) {
		this( exito, resultado, null );
	}
	
	public Resposta( T resultado ) {
		this( true, resultado, null );
	}
	
	public Resposta() {
		this( true, null, null );
	}
	
	/**
	 * @see #getMensagens()
	 */
	public Resposta<T> mais( Mensagem mensagem, Mensagem... outras ) {
		if( mensagem == null ) throw new IllegalArgumentException();
		mensagens.add( mensagem );
		if( outras != null ){
			for( Mensagem m : outras ){
				if( m == null ) throw new IllegalArgumentException();
				mensagens.add( m );
			}
		}
		return this;
	}
	
	/**
	 * @see #getMensagens()
	 */
	public Resposta<T> mais( Collection<Mensagem> mensagens ) {
		if( mensagens == null ) throw new IllegalArgumentException();
		for( Mensagem m : mensagens ) mais( m );
		return this;
	}
	
	/**
	 * @see Mensagem
	 * @see #mais(Mensagem, Mensagem...)
	 */
	public Resposta<T> mais( Tipo tipo, String referencia, Serializable argumento ) {
		return mais( new Mensagem( tipo, referencia, argumento ) );
	}

	/**
	 * A a��o que resultou nesta {@link Resposta} terminou com sucesso?
	 */
	public boolean isExito() {
		return exito;
	}
	
	public Resposta<T> setExito( boolean exito ) {
		this.exito = exito;
		return this;
	}

	/**
	 * Objeto resultante, podendo ser <code>null</code>.
	 */
	public T getResultado() {
		return resultado;
	}

	public Resposta<T> setResultado( T resultado ) {
		this.resultado = resultado;
		return this;
	}

	/**
	 * Lista de {@link Mensagem mensagens}, podendo estar {@link List#isEmpty() vazia}, mas nunca <code>null</code>.
	 */
	public List<Mensagem> getMensagens() {
		return mensagens;
	}

	/**
	 * Lista de {@link Mensagem mensagens}.<br>
	 * Se <code>null</code>, ser� atribu�da uma {@link List lista} vazia.
	 */
	public Resposta<T> setMensagens( List<Mensagem> mensagens ) {
		this.mensagens = mensagens != null ? mensagens : new ArrayList<Mensagem>();
		return this;
	}
	
}
