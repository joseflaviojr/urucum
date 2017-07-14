
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

package com.joseflavio.urucum.tucurui;

/**
 * Elemento da linguagem Tucuruí.
 * @author José Flávio de Souza Dias Júnior
 * @see <a href="http://joseflavio.com/tucurui">http://joseflavio.com/tucurui</a>
 */
public abstract class Elemento extends Hierarquia {
	
	private int nivel = 0;
	
	private int linha = 1;
	
	/**
	 * Nível hierárquico do {@link Elemento}, isto é, tamanho da indentação (quantidade de tabulações).
	 */
	public int getNivel() {
		return nivel;
	}
	
	public Elemento setNivel( int nivel ) {
		if( nivel < 0 ) throw new IllegalArgumentException();
		this.nivel = nivel;
		return this;
	}
	
	/**
	 * Número da linha na qual o {@link Elemento} foi declarado no documento {@link Tucurui}, a partir de 1.
	 */
	public int getLinha() {
		return linha;
	}
	
	public Elemento setLinha( int linha ) {
		if( linha <= 0 ) throw new IllegalArgumentException();
		this.linha = linha;
		return this;
	}
	
}
