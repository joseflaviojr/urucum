
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

package com.joseflavio.urucum.seguranca;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.security.Permission;
import java.security.Permissions;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * Utilitários para {@link SecurityManager}.
 * @author José Flávio de Souza Dias Júnior
 */
public class SecurityManagerUtil {
	
	/**
	 * Imprime {@link Permission permissões} em {@link Writer}, formatando-as conforme especificação.
	 * @param permissoes {@link Permission}s a imprimir.
	 * @param saida Destino da impressão textual.
	 * @param formatoArquivo Imprimir {@link Permission} no formato de <code>sun.security.provider.PolicyFile</code>.
	 * @param prefixo Prefixo a ser concatenado.
	 * @param sufixo Sufixo a ser concatenado.
	 */
	public static void imprimir( Collection<Permission> permissoes, Writer saida, boolean formatoArquivo, String prefixo, String sufixo ) throws IOException {
		Envelope[] lista = new Envelope[ permissoes.size() ];
		Iterator<Permission> it = permissoes.iterator();
		for( int i = 0; i < lista.length; i++ ){
			lista[i] = new Envelope( it.next() );
		}
		Arrays.sort( lista );
		for( int i = 0; i < lista.length; i++ ){
			lista[i].imprimir( saida, formatoArquivo, prefixo, sufixo );
		}
	}
	
	/**
	 * {@link #imprimir(Collection, Writer, boolean, String, String) Imprime} as
	 * {@link Permission}s em {@link System#out}.
	 * @see #imprimir(Collection, Writer, boolean, String, String)
	 */
	public static void imprimir( Collection<Permission> permissoes, boolean formatoArquivo, String prefixo, String sufixo ) throws IOException {
		imprimir( permissoes, new PrintWriter( System.out ), formatoArquivo, prefixo, sufixo );
	}
	
	/**
	 * {@link #imprimir(Collection, Writer, boolean, String, String) Imprime} as
	 * {@link Permission}s em {@link System#out}, sem prefixo e sem sufixo.
	 * @see #imprimir(Collection, Writer, boolean, String, String)
	 */
	public static void imprimir( Collection<Permission> permissoes, boolean formatoArquivo ) throws IOException {
		imprimir( permissoes, new PrintWriter( System.out ), formatoArquivo, null, null );
	}
	
	/**
	 * @see #imprimir(Collection, Writer, boolean, String, String)
	 */
	public static void imprimir( Permissions permissoes, Writer saida, boolean formatoArquivo, String prefixo, String sufixo ) throws IOException {
		imprimir( Collections.list( permissoes.elements() ), saida, formatoArquivo, prefixo, sufixo );
	}
	
	private static class Envelope implements Comparable<Envelope> {
		
		private Permission p;
		
		public Envelope( Permission p ) {
			this.p = p;
		}

		@Override
		public int compareTo( Envelope o ) {
			if( p.getClass() == o.p.getClass() ){
				int comp = comparar( p.getName(), o.p.getName() );
				if( comp != 0 ) return comp;
				return comparar( p.getActions(), o.p.getActions() );
			}else{
				return comparar( p.getClass().getName(), o.p.getClass().getName() );
			}
		}
		
		private int comparar( String a, String b ) {
			if( a == b ) return 0;
			if( a == null ) return -1;
			if( b == null ) return +1;
			return a.compareTo( b );
		}
		
		public void imprimir( Writer saida, boolean formatoArquivo, String prefixo, String sufixo ) throws IOException {
			
			String nome = p.getName();
			String acoes = p.getActions();
			
			if( prefixo != null ) saida.write( prefixo );
			
			if( formatoArquivo ){
				saida.write( "permission " + p.getClass().getName() );
				if( nome != null && nome.length() > 0 ){
					saida.write( " \"" + nome + "\"" );
					if( acoes != null && acoes.length() > 0 ){
						saida.write( ", \"" + acoes + "\"" );
					}
				}
				saida.write( ";" );
			}else{
				saida.write( p.getClass().getName() );
				saida.write( "(" );
				if( nome != null && nome.length() > 0 ){
					saida.write( " \"" + nome + "\"" );
					if( acoes != null && acoes.length() > 0 ){
						saida.write( ", \"" + acoes + "\"" );
					}
					saida.write( " " );
				}
				saida.write( ")" );
			}
			
			if( sufixo != null ) saida.write( sufixo );
			saida.write( "\n" );
			
		}
		
	}
	
}
