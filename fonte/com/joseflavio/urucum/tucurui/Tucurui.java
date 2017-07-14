
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

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Documento Tucuru�.
 * @author Jos� Fl�vio de Souza Dias J�nior
 * @see <a href="http://joseflavio.com/tucurui">http://joseflavio.com/tucurui</a>
 */
public class Tucurui extends Hierarquia {
    
    private String versao;
    
    private String codificacao;
    
    private String modelo;
    
    public Tucurui() {
        this( "1.0", "UTF-8", null );
    }
    
    public Tucurui( String versao, String codificacao, String modelo ) {
        setVersao( versao );
        setCodificacao( codificacao );
        setModelo( modelo );
    }
    
    public String getVersao() {
        return versao;
    }
    
    public Tucurui setVersao( String versao ) {
        this.versao = versao == null ? "1.0" : versao;
        return this;
    }
    
    public String getCodificacao() {
        return codificacao;
    }
    
    public Tucurui setCodificacao( String codificacao ) {
        this.codificacao = codificacao == null ? "UTF-8" : codificacao;
        return this;
    }
    
    public String getModelo() {
        return modelo;
    }
    
    public Tucurui setModelo( String modelo ) {
        this.modelo = modelo;
        return this;
    }
    
    /**
     * Gera um {@link Document Documento} XML (DOM) correspondente a este {@link Tucurui}.
     * @throws TucuruiException Sem�ntica inconsistente.
     * @see TucuruiUtil#transformar(Tucurui, Transformer, Writer)
     */
    public Document gerarDOM() throws TucuruiException {
    
        try{
            
            Document xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        
            for( Elemento elemento : this ){
                try{
                    xml.appendChild( gerarNode( elemento, xml ) );
                }catch( DOMException e ){
                    throw new TucuruiException( TucuruiException.Erro.SEMANTICA_INCORRETA, elemento.getLinha(), e );
                }
            }
        
            return xml;
    
        }catch( ParserConfigurationException e ){
            throw new TucuruiException( e );
        }
    
    }
    
    /**
     * Transforma este documento {@link Tucurui} em XML.
     * @see TucuruiUtil#gerarXML(Tucurui, Writer)
     */
    public String gerarXML() throws TucuruiException, TransformerException {
        StringWriter saida = new StringWriter( 512 );
        TucuruiUtil.gerarXML( this, saida );
        return saida.toString();
    }
    
    /**
     * Transforma este documento {@link Tucurui} em HTML.
     * @see TucuruiUtil#gerarHTML(Tucurui, Writer)
     */
    public String gerarHTML() throws TucuruiException, TransformerException {
        StringWriter saida = new StringWriter( 512 );
        TucuruiUtil.gerarHTML( this, saida );
        return saida.toString();
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
        
            for( Elemento sub : elemento ){
                try{
                    Node subnode = gerarNode( sub, xml );
                    if( subnode instanceof Attr ) ((Element)node).setAttributeNode( (Attr) subnode );
                    else node.appendChild( subnode );
                }catch( DOMException e ){
                    throw new TucuruiException( TucuruiException.Erro.SEMANTICA_INCORRETA, sub.getLinha(), e );
                }
            }
        
            return node;
            
        }catch( DOMException e ){
            throw new TucuruiException( TucuruiException.Erro.SEMANTICA_INCORRETA, elemento.getLinha(), e );
        }
    
    }
    
}
