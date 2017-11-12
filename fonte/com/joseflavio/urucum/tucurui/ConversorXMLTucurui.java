
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

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Conversor de XML para {@link Tucurui} atrav�s de {@link DefaultHandler SAX2}.
 * @author Jos� Fl�vio de Souza Dias J�nior
 */
class ConversorXMLTucurui extends DefaultHandler implements LexicalHandler {

	private Tucurui tucurui;
	
	private Hierarquia ultimo;
	
	private StringBuilder valor;
	
	private void adicionarValor() {
		ultimo.add( new Valor( valor.toString() ) );
		valor = null;
	}
	
	@Override
	public void startDocument() throws SAXException {
		tucurui = new Tucurui();
		ultimo = tucurui;
	}
	
	@Override
	public void startElement( String uri, String localName, String qName, Attributes attributes ) throws SAXException {
		
		if( valor != null ) adicionarValor();
	    
		Objeto objeto = new Objeto( qName );
		
		ultimo.add( objeto );
		ultimo = objeto;
        
		int total = attributes.getLength();
        for( int i = 0; i < total; i++ ){
            String nome  = attributes.getQName( i );
            String valor = attributes.getValue( i );
            ultimo.add( new Objeto( nome, true, valor ) );
        }
        
    }
	
	@Override
	public void endElement( String uri, String localName, String qName ) throws SAXException {
		
		if( valor != null ) adicionarValor();
		
		ultimo = ultimo.getMae();
		
	}
	
	@Override
	public void characters( char[] ch, int start, int length ) throws SAXException {
		
	    if( valor == null ) valor = new StringBuilder( length );
        
        for( int i = start, fim = start + length; i < fim; i++ ){
            char c = ch[i];
            switch( c ){
                case '\n':
                    valor.append( "{n}" );
                    break;
                case '\t':
                    valor.append( "{t}" );
                    break;
                default:
                    valor.append( c );
					break;
            }
        }
        
	}
	
	@Override
	public void comment( char[] ch, int start, int length ) throws SAXException {
		
		if( valor != null ) adicionarValor();
		
        ultimo.add( new Comentario( new String( ch, start, length ) ) );
		
	}
	
	@Override
	public void startDTD( String name, String publicId, String systemId ) throws SAXException {
		
	}
	
	@Override
	public void endDTD() throws SAXException {
		
	}
	
	@Override
	public void startEntity( String name ) throws SAXException {
		
	}
	
	@Override
	public void endEntity( String name ) throws SAXException {
		
	}
	
	@Override
	public void startCDATA() throws SAXException {
		
	}
	
	@Override
	public void endCDATA() throws SAXException {
		
	}
	
	/**
	 * {@link Tucurui} resultante.
	 */
	public Tucurui getTucurui() {
		if( tucurui == null ) tucurui = new Tucurui();
		return tucurui;
	}
	
}
