
/*
 *  Copyright (C) 2016-2018 José Flávio de Souza Dias Júnior
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
 *  Direitos Autorais Reservados (C) 2016-2018 José Flávio de Souza Dias Júnior
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

import com.joseflavio.urucum.texto.StringUtil;

import java.util.*;
import java.util.function.UnaryOperator;

/**
 * Hierarquia de {@link Elemento}s.
 * @author José Flávio de Souza Dias Júnior
 * @see <a href="http://joseflavio.com/tucurui">http://joseflavio.com/tucurui</a>
 */
public abstract class Hierarquia implements List<Elemento> {
	
	private Hierarquia mae;
	
	private ArrayList<Elemento> lista;
	
	/**
	 * {@link Hierarquia} superior.
	 */
	public final Hierarquia getMae() {
		return mae;
	}
    
    final void setMae( Hierarquia mae ) {
        this.mae = mae;
    }
    
    private Elemento preparar( Elemento elemento ) {
		if( elemento == null ) throw new IllegalArgumentException();
		elemento.setMae( this );
		return elemento;
	}
	
	/**
	 * {@link #add(Elemento) Adiciona} um {@link Elemento}.
	 * @return this
	 */
	public Hierarquia mais( Elemento elemento ) {
		add( elemento );
		return this;
	}
	
	/**
	 * Retorna o primeiro {@link Objeto}, diretamente contido nesta {@link Hierarquia}, que
	 * possui um específico nome.
	 * @param nome {@link Objeto#getNome()}
	 * @return null, se inexistente.
	 * @see #valor()
	 */
	public Objeto obj( String nome ) {
		if( StringUtil.tamanho( nome ) == 0 ) throw new IllegalArgumentException();
		if( lista == null ) return null;
		for( Elemento ele : this ){
			if( ele instanceof Objeto && ((Objeto)ele).getNome().equals( nome ) ){
				return (Objeto) ele;
			}
		}
		return null;
	}
	
	/**
	 * Retorna o primeiro {@link Valor} diretamente contido nesta {@link Hierarquia}.
	 * @return null, se inexistente.
	 * @see #textoCompleto()
	 */
	public Valor valor() {
		if( lista == null ) return null;
		for( Elemento ele : this ){
			if( ele instanceof Valor ){
				return (Valor) ele;
			}
		}
		return null;
	}
	
	/**
	 * Acrescenta no {@link StringBuilder} o {@link Valor#texto()} de todos os {@link Valor}es
	 * contidos nesta {@link Hierarquia}.
	 * @param texto Destino do {@link Valor#texto()}.
	 * @param recursivo Acrescentar o {@link #textoCompleto(StringBuilder, boolean) texto completo} dos sub-{@link Elemento}s?
	 * @see #textoCompleto()
	 * @see #valor()
	 */
	public void textoCompleto( StringBuilder texto, boolean recursivo ) {
		if( lista == null ) return;
		for( Elemento ele : this ){
			if( ele instanceof Valor ) texto.append( ((Valor)ele).getTextoReconhecido() );
			if( recursivo ) ele.textoCompleto( texto, recursivo );
		}
	}
	
	/**
	 * Retorna o {@link Valor#texto()} de todos os {@link Valor}es
	 * contidos, direto ou indiretamente, nesta {@link Hierarquia}.
	 * @return "", se {@link Valor} vazio ou inexistente.
	 * @see #textoCompleto(StringBuilder, boolean)
	 * @see #valor()
	 */
	public String textoCompleto() {
		if( lista == null ) return "";
		StringBuilder texto = new StringBuilder( 100 );
		this.textoCompleto( texto, true );
		return texto.toString();
	}
	
	@Override
	public Elemento set( int index, Elemento elemento ) {
		if( lista == null ) lista = new ArrayList<>();
		return lista.set( index, preparar( elemento ) );
	}
	
	@Override
	public boolean add( Elemento elemento ) {
		if( lista == null ) lista = new ArrayList<>();
		return lista.add( preparar( elemento ) );
	}
	
	@Override
	public void add( int index, Elemento elemento ) {
		if( lista == null ) lista = new ArrayList<>();
		lista.add( index, preparar( elemento ) );
	}
	
	@Override
	public boolean addAll( Collection<? extends Elemento> colecao ) {
		if( lista == null ) lista = new ArrayList<>();
		for( Elemento elemento : colecao ) preparar( elemento );
		return lista.addAll( colecao );
	}
	
	@Override
	public boolean addAll( int index, Collection<? extends Elemento> colecao ) {
		if( lista == null ) lista = new ArrayList<>();
		for( Elemento elemento : colecao ) preparar( elemento );
		return lista.addAll( index, colecao );
	}
	
	@Override
	public void replaceAll( UnaryOperator<Elemento> operador ) {
		if( lista == null ) return;
		lista.replaceAll( ( Elemento elemento ) -> preparar( operador.apply( elemento ) ) );
	}
	
	@Override
	public void sort( Comparator<? super Elemento> c ) {
		if( lista == null ) return;
		lista.sort( c );
	}
	
	@Override
	public Spliterator<Elemento> spliterator() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public int size() {
		return lista != null ? lista.size() : 0;
	}
	
	@Override
	public boolean isEmpty() {
		return size() == 0;
	}
	
	@Override
	public boolean contains( Object o ) {
		return lista != null ? lista.contains( o ) : false;
	}
	
	@Override
	public Iterator<Elemento> iterator() {
		if( lista == null ) lista = new ArrayList<>();
		return lista.iterator();
	}
	
	@Override
	public Object[] toArray() {
		return lista != null ? lista.toArray() : new Object[0];
	}
	
	@Override
	public <T> T[] toArray( T[] a ) {
		return lista != null ? lista.toArray( a ) : a;
	}
	
	@Override
	public boolean remove( Object o ) {
		return lista != null ? lista.remove( o ) : false;
	}
	
	@Override
	public boolean containsAll( Collection<?> c ) {
		return lista != null ? lista.containsAll( c ) : false;
	}
	
	@Override
	public boolean removeAll( Collection<?> c ) {
		return lista != null ? lista.removeAll( c ) : false;
	}
	
	@Override
	public boolean retainAll( Collection<?> c ) {
		return lista != null ? lista.retainAll( c ) : false;
	}
	
	@Override
	public void clear() {
		if( lista != null ) lista.clear();
	}
	
	@Override
	public Elemento get( int index ) {
		if( lista == null ) throw new IndexOutOfBoundsException( "" + index );
		return lista.get( index );
	}
	
	@Override
	public Elemento remove( int index ) {
		if( lista == null ) throw new IndexOutOfBoundsException( "" + index );
		return lista.remove( index );
	}
	
	@Override
	public int indexOf( Object o ) {
		return lista != null ? lista.indexOf( o ) : -1;
	}
	
	@Override
	public int lastIndexOf( Object o ) {
		return lista != null ? lista.lastIndexOf( o ) : -1;
	}
	
	@Override
	public ListIterator<Elemento> listIterator() {
		return listIterator( 0 );
	}
	
	@Override
	public ListIterator<Elemento> listIterator( int index ) {
		
		if( lista == null ) lista = new ArrayList<>();
		
		return new ListIterator<Elemento>() {
			
			private ListIterator<Elemento> li = lista.listIterator( index );
			
			@Override
			public boolean hasNext() {
				return li.hasNext();
			}
			
			@Override
			public Elemento next() {
				return li.next();
			}
			
			@Override
			public boolean hasPrevious() {
				return li.hasPrevious();
			}
			
			@Override
			public Elemento previous() {
				return li.previous();
			}
			
			@Override
			public int nextIndex() {
				return li.nextIndex();
			}
			
			@Override
			public int previousIndex() {
				return li.previousIndex();
			}
			
			@Override
			public void remove() {
				li.remove();
			}
			
			@Override
			public void set( Elemento elemento ) {
				li.set( preparar( elemento ) );
			}
			
			@Override
			public void add( Elemento elemento ) {
				li.add( preparar( elemento ) );
			}
			
		};
		
	}
	
	@Override
	public List<Elemento> subList( int fromIndex, int toIndex ) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Posição desta na {@link Hierarquia} da sua {@link #getMae() mãe}. Valor >= 1.
	 * @return {@link #getMae()}.{@link #indexOf(Object) indexOf(this)}+1
	 */
	public int getPosicao() {
		return mae != null ? mae.indexOf( this ) + 1 : 1;
	}
	
	/**
	 * Pseudoendereço desta {@link Hierarquia} dentro do seu documento {@link Tucurui}.<br>
	 * Inclui {@link #getPosicao()} e {@link #toString()} parcial.
	 */
	public String getEndereco() {
		
		StringBuilder endereco = new StringBuilder();
		Hierarquia h = this;
		
		while( h != null && !( h instanceof Tucurui ) ){
			String descricao = h.toString();
			if( descricao.length() > 15 ) descricao = descricao.substring( 0, 12 ) + "...";
			endereco.insert( 0, "/[" + h.getPosicao() + "]" + descricao );
			h = h.mae;
		}
		
		return endereco.length() > 0 ? endereco.toString() : "/";
		
	}
	
}
