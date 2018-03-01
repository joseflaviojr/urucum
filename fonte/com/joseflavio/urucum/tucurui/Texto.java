
/*
 *  Copyright (C) 2016-2018 Jos� Fl�vio de Souza Dias J�nior
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
 *  Direitos Autorais Reservados (C) 2016-2018 Jos� Fl�vio de Souza Dias J�nior
 * 
 *  Este arquivo � parte de Urucum - <http://joseflavio.com/urucum/>.
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

/**
 * {@link Elemento} textual.
 * @author Jos� Fl�vio de Souza Dias J�nior
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
     * Conte�do textual.<br>
	 * Observa��o: os codificadores de caracteres Unicode e os codificadores especiais
	 * n�o ser�o traduzidos para {@link Character}. Para tanto, utilizar {@link #getTextoReconhecido()}.
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
