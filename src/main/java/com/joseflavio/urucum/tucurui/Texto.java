
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

/**
 * {@link Elemento} textual.
 * @author José Flávio de Souza Dias Júnior
 * @see <a href="http://joseflavio.com/tucurui">http://joseflavio.com/tucurui</a>
 */
public abstract class Texto extends Elemento {

	private String texto;
	
	private transient String reconhecido;
	
	public Texto() {
		this( "" );
	}
	
	public Texto( String texto ) {
		setTexto( texto );
	}
	
	/**
     * Conteúdo textual.<br>
	 * Observação: os codificadores de caracteres Unicode e os codificadores especiais
	 * não serão traduzidos para {@link Character}. Para tanto, utilizar {@link #getTextoReconhecido()}.
	 * @see #getTextoReconhecido()
	 * @see TucuruiUtil#reconhecer(String)
     */
	public String getTexto() {
		return texto;
	}
	
	public Texto setTexto( String texto ) {
		if( texto == null ) throw new IllegalArgumentException();
		this.texto = texto.replace( "\n", "{n}" ).replace( "\t", "{t}" );
		this.reconhecido = null;
		return this;
	}
	
	/**
	 * Retorna o {@link #getTexto() texto} {@link TucuruiUtil#reconhecer(String) reconhecido}.
	 * @see TucuruiUtil#reconhecer(String)
	 */
	public String getTextoReconhecido() {
		if( reconhecido == null ){
			reconhecido = TucuruiUtil.reconhecer( texto );
		}
		return reconhecido;
	}
	
	@Override
	public String toString() {
		return texto;
	}
	
	/**
	 * O mesmo que {@link #getTextoReconhecido()}.
	 */
	public String texto() {
		return getTextoReconhecido();
	}
	
}
