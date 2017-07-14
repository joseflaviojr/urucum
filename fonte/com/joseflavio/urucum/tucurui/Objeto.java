
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

package com.joseflavio.urucum.tucurui;

import com.joseflavio.urucum.texto.StringUtil;

/**
 * Objeto Tucuru�.
 * @author Jos� Fl�vio de Souza Dias J�nior
 * @see <a href="http://joseflavio.com/tucurui">http://joseflavio.com/tucurui</a>
 */
public class Objeto extends Elemento {

	private String nome;
	
	private boolean privado;
	
	public Objeto() {
		this( "objeto" );
	}
	
	public Objeto( String nome ) {
		this( nome, false, null );
	}
	
	public Objeto( String nome, String valor ) {
		this( nome, false, valor );
	}
	
	public Objeto( String nome, boolean privado ) {
		this( nome, privado, null );
	}
	
	public Objeto( String nome, boolean privado, String valor ) {
		setNome( nome );
		setPrivado( privado );
		if( valor != null ) mais( new Valor( valor ) );
	}
	
	/**
	 * Nome do {@link Objeto}.
	 */
	public String getNome() {
		return nome;
	}
	
	public Objeto setNome( String nome ) {
		if( StringUtil.tamanho( nome ) == 0 ) throw new IllegalArgumentException();
		this.nome = nome;
		return this;
	}
	
	/**
	 * {@link Objeto} privado � objeto com nome prefixado com "-".<br>
	 * O prefixo � retirado no {@link #getNome()}.
	 */
	public boolean isPrivado() {
		return privado;
	}
	
	public Objeto setPrivado( boolean privado ) {
		this.privado = privado;
		return this;
	}
	
	@Override
	public String toString() {
		return ( privado ? "-" : "" ) + nome;
	}
	
}
