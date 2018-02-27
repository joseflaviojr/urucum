
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
import org.w3c.dom.*;
import org.w3c.dom.CharacterData;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * Documento Tucuruí.
 * @author José Flávio de Souza Dias Júnior
 * @see <a href="http://joseflavio.com/tucurui">http://joseflavio.com/tucurui</a>
 */
public class Tucurui extends Hierarquia {
    
    private String versao;
    
    private String modelo;
    
    public Tucurui() {
        this( "1.0", null );
    }
    
    public Tucurui( String versao, String modelo ) {
        setVersao( versao );
        setModelo( modelo );
    }
    
    public String getVersao() {
        return versao;
    }
    
    public Tucurui setVersao( String versao ) {
        this.versao = StringUtil.tamanho( versao ) == 0 ? "1.0" : versao;
        return this;
    }
    
    public String getModelo() {
        return modelo;
    }
    
    public Tucurui setModelo( String modelo ) {
        this.modelo = modelo;
        return this;
    }
    
    @Override
    public String toString() {
        try{
            ByteArrayOutputStream saida = new ByteArrayOutputStream( 512 );
            TucuruiUtil.imprimir( this, saida );
            return new String( saida.toByteArray(), "UTF-8" );
        }catch( Exception e ){
            throw new IllegalStateException( e );
        }
    }
    
    /**
     * Gera um {@link Document Documento} XML (DOM) correspondente a este {@link Tucurui}.
     * @throws TucuruiException Semântica inconsistente.
     * @see TucuruiUtil#transformar(Tucurui, Transformer, OutputStream, String)
     */
    public Document gerarDOM() throws TucuruiException {
    
        try{
            
            Document xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        
            for( Elemento elemento : this ){
                try{
                    xml.appendChild( gerarNode( elemento, xml ) );
                }catch( DOMException e ){
                    throw new TucuruiException( TucuruiException.Erro.SEMANTICA_INCORRETA, elemento.getEndereco(), e );
                }
            }
        
            return xml;
    
        }catch( ParserConfigurationException e ){
            throw new TucuruiException( e );
        }
    
    }
    
    /**
     * Transforma este documento {@link Tucurui} em XML.
     * @see TucuruiUtil#gerarXML(Tucurui, OutputStream, String)
     */
    public String gerarXML() throws TucuruiException, TransformerException {
        try{
            ByteArrayOutputStream destino = new ByteArrayOutputStream( 512 );
            TucuruiUtil.gerarXML( this, destino, "UTF-8" );
            return new String( destino.toByteArray(), "UTF-8" );
        }catch( UnsupportedEncodingException e ){
            throw new TransformerException( e );
        }
    }
    
    /**
     * Transforma este documento {@link Tucurui} em HTML.
     * @see TucuruiUtil#gerarHTML(Tucurui, OutputStream, String)
     */
    public String gerarHTML() throws TucuruiException, TransformerException {
        try{
            ByteArrayOutputStream destino = new ByteArrayOutputStream( 512 );
            TucuruiUtil.gerarHTML( this, destino, "UTF-8" );
            return new String( destino.toByteArray(), "UTF-8" );
        }catch( UnsupportedEncodingException e ){
            throw new TransformerException( e );
        }
    }
    
    private static Node gerarNode( Elemento elemento, Document xml ) throws TucuruiException {
    
        try{
            
            Node node = null;
        
            if( elemento instanceof Objeto ){
                Objeto objeto = (Objeto) elemento;
                if( objeto.isPrivado() ) node = xml.createAttribute( objeto.getNome() );
                else node = xml.createElement( objeto.getNome() );
    
            }else if( elemento instanceof Valor ){
                node = xml.createTextNode( ((Valor)elemento).getTextoReconhecido() );
        
            }else if( elemento instanceof Comentario ){
                node = xml.createComment( ((Comentario)elemento).getTextoReconhecido() );
            }
    
            if( node instanceof CharacterData && elemento.size() > 0 ){
                throw new TucuruiException( TucuruiException.Erro.SEMANTICA_INCORRETA, elemento.get( 0 ).getEndereco() );
            }
            
            for( Elemento sub : elemento ){
                try{
                    Node subnode = gerarNode( sub, xml );
                    if( subnode instanceof Attr ) ((Element)node).setAttributeNode( (Attr) subnode );
                    else node.appendChild( subnode );
                }catch( DOMException e ){
                    throw new TucuruiException( TucuruiException.Erro.SEMANTICA_INCORRETA, sub.getEndereco(), e );
                }
            }
        
            return node;
            
        }catch( DOMException e ){
            throw new TucuruiException( TucuruiException.Erro.SEMANTICA_INCORRETA, elemento.getEndereco(), e );
        }
    
    }
    
}
