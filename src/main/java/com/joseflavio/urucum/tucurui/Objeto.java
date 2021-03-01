
/*
 *  Copyright (C) 2016-2021 José Flávio de Souza Dias Júnior
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
 *  Direitos Autorais Reservados (C) 2016-2021 José Flávio de Souza Dias Júnior
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

package com.joseflavio.urucum.tucurui;

import com.joseflavio.urucum.texto.StringUtil;

/**
 * Objeto Tucuruí.
 * @author José Flávio de Souza Dias Júnior
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
	 * {@link Objeto} privado é objeto com nome prefixado com "-".<br>
	 * O prefixo é retirado no {@link #getNome()}.
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
