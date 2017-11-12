
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

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Utilitários para {@link Tucurui}.
 * @author José Flávio de Souza Dias Júnior
 */
public class TucuruiUtil {
    
    /**
     * Cria um documento {@link Tucurui} com base no conteúdo de um {@link File}.
     * @see #abrir(URL)
     */
    public static Tucurui abrir( File arquivo ) throws IOException, TucuruiException {
    
        Tucurui tucurui = null;
        String codificacao = null;
        
        try( InputStream entrada = new FileInputStream( arquivo ) ){
            codificacao = identificarCodificacao( entrada );
        }
    
        try( Reader entrada = new InputStreamReader( new FileInputStream( arquivo ), codificacao ) ){
            tucurui = abrir( entrada );
        }
        
        return tucurui;
        
    }
    
    /**
     * Cria um documento {@link Tucurui} com base no conteúdo de um {@link File}.
     * @see #abrir(File)
     */
    public static Tucurui abrir( String arquivo ) throws IOException, TucuruiException {
        return abrir( new File( arquivo ) );
    }
    
    /**
     * Cria um documento {@link Tucurui} com base no conteúdo de uma {@link URL}.
     * @see #abrir(File)
     */
    public static Tucurui abrir( URL url ) throws IOException, TucuruiException {
    
        Tucurui tucurui = null;
        String codificacao = null;
    
        try( InputStream entrada = url.openStream() ){
            codificacao = identificarCodificacao( entrada );
        }
    
        try( Reader entrada = new InputStreamReader( url.openStream(), codificacao ) ){
            tucurui = abrir( entrada );
        }
    
        return tucurui;
        
    }
    
    /**
     * Cria um documento {@link Tucurui} com base no conteúdo de um {@link Reader}.
     */
    public static Tucurui abrir( Reader conteudo ) throws IOException, TucuruiException {
        
        // Entrada de texto
        
        BufferedReader br =
                conteudo instanceof BufferedReader ?
                (BufferedReader) conteudo :
                new BufferedReader( conteudo );
    
        // Controle do Tucuruí
        
        Tucurui tucurui = new Tucurui();
        Hierarquia mae = tucurui;
        
        // Controle de linha
        
        String linha  = null;
        int    nlinha = 0;
        
        // Controle de cabeçalho
        
        boolean obterVersao = true;
        boolean obterModelo = true;
        
        // Interpretação das linhas
        
        while( ( linha = br.readLine() ) != null ){
    
            nlinha++;
    
            int tamanho = linha.length();
            if( tamanho == 0 ) continue;
            
            // Versão, Codificação e Modelo
            
            if( obterVersao || obterModelo ){
                
                char ch = linha.charAt( 0 );
                
                if( ch == '#' ){
                    if( ! obterVersao ) throw novaTucuruiException( TucuruiException.Erro.CABECALHO_INVALIDO, nlinha );
                    String[] p = linha.split( " " );
                    if( p.length != 3 ) throw novaTucuruiException( TucuruiException.Erro.CABECALHO_INVALIDO, nlinha );
                    tucurui.setVersao( p[1] );
                    obterVersao = false;
                    continue;
        
                }else if( ch == '@' ){
                    if( ! obterModelo ) throw novaTucuruiException( TucuruiException.Erro.CABECALHO_INVALIDO, nlinha );
                    if( tamanho < 3 ) throw novaTucuruiException( TucuruiException.Erro.CABECALHO_INVALIDO, nlinha );
                    tucurui.setModelo( linha.substring( 2 ) );
                    obterModelo = false;
                    continue;
                
                }else if( linha.trim().length() > 0 ){
                    obterVersao = false;
                    obterModelo = false;
                }
                
            }
            
            // Indentação e Hierarquia
            
            int esp = 0;
            int tab = 0;
            
            for( int i = 0; i < tamanho; i++ ){
                char ch = linha.charAt( i );
                if( ch == ' ' ) esp++;
                else if( ch == '\t' ) tab++;
                else break;
            }
    
            linha = linha.substring( esp + tab );
    
            tamanho = linha.length();
            if( tamanho == 0 ) continue;
            
            if( ( esp % 4 ) != 0 ) throw novaTucuruiException( TucuruiException.Erro.INDENTACAO_INCORRETA, nlinha );
    
            int nivel = esp / 4 + tab;
            while( nivel <= getNivel( mae ) ) mae = mae.getMae();
            
            // Elementos
            
            if( linha.startsWith( "//" ) ){ //Comentário
                
                Comentario comentario = new Comentario( linha.substring( 2 ) );
                
                mae.add( comentario );
                mae = comentario;
                
                
            }else if( linha.equals( "---" ) ){ //Valor livre
                
                Valor valor = null;
                
                while( ( linha = br.readLine() ) != null ){
                    
                    nlinha++;
                    
                    if( ! linha.isEmpty() ){
                        
                        if( linha.length() < ( esp + tab ) ) throw novaTucuruiException( TucuruiException.Erro.INDENTACAO_INCORRETA, nlinha );
                        if( linha.substring( 0, esp + tab ).trim().length() != 0 ) throw novaTucuruiException( TucuruiException.Erro.INDENTACAO_INCORRETA, nlinha );
                        
                        linha = linha.substring( esp + tab );
    
                        if( linha.equals( "---" ) ){ //Fechamento
                            
                            if( valor == null ){
                                valor = new Valor();
                                mae.add( valor );
                            }else{
                                String texto = valor.getTexto();
                                valor.setTexto( texto.substring( 0, texto.length() - 3 ) );
                            }
                            
                            mae = valor;
                            valor = null;
                            break;
                            
                        }
                        
                    }
                    
                    valor = new Valor( linha + "{n}" );
                    mae.add( valor );
                    
                }
                
                if( valor != null ) throw novaTucuruiException( TucuruiException.Erro.VALOR_LIVRE_NAO_FECHADO, nlinha );
                
                
            }else if( linha.charAt( 0 ) == ':' ){ //Objeto anônimo (valor não livre)
    
                if( tamanho == 1 || linha.charAt( 1 ) != ' ' ) throw novaTucuruiException( TucuruiException.Erro.ESPACO_ESPERADO, nlinha );
                
                Valor valor = new Valor( linha.substring( 2 ) );

                mae.add( valor );
                mae = valor;
                
                
            }else{ //Objeto
                
                String nome;
                String valor = null;
                int separador = linha.indexOf( ':' );
                
                if( separador != -1 ){
                    nome = linha.substring( 0, separador );
                    if( separador < ( tamanho - 1 ) ){
                        if( linha.charAt( separador + 1 ) != ' ' ) throw novaTucuruiException( TucuruiException.Erro.ESPACO_ESPERADO, nlinha );
                        valor = linha.substring( separador + 2 );
                    }
                }else{
                    nome = linha;
                }
    
                boolean privado = nome.charAt( 0 ) == '-';
                
                if( privado ){
                    if( nome.length() == 1 ) throw novaTucuruiException( TucuruiException.Erro.NOME_INVALIDO, nlinha );
                    nome = nome.substring( 1 );
                }
                
                Objeto objeto = new Objeto( nome, privado );
                
                if( valor != null ){
                    objeto.add( new Valor( valor ) );
                }
    
                mae.add( objeto );
                mae = objeto;
                
            }
    
        }
        
        return tucurui;
        
    }
    
    /**
     * Converte um documento XML em {@link Tucurui}.
     */
    public static Tucurui converterXML( File arquivo ) throws IOException, SAXException {
        return converterXML0( arquivo );
    }
    
    /**
     * Converte um documento XML em {@link Tucurui}.
     */
    public static Tucurui converterXML( URL url ) throws IOException, SAXException {
        return converterXML0( url.toExternalForm() );
    }
    
    /**
     * Converte um documento XML em {@link Tucurui}.
     */
    public static Tucurui converterXML( InputStream is ) throws IOException, SAXException {
        return converterXML0( is );
    }
    
    /**
     * Converte um documento XML em {@link Tucurui}.
     */
    private static Tucurui converterXML0( Object arquivo ) throws IOException, SAXException {
        
        try{
            
            ConversorXMLTucurui conversor = new ConversorXMLTucurui();
            
            SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            xmlReader.setProperty( "http://xml.org/sax/properties/lexical-handler", conversor );
            
            if     ( arquivo instanceof File        ) saxParser.parse( (File)        arquivo, conversor );
            else if( arquivo instanceof String      ) saxParser.parse( (String)      arquivo, conversor );
            else if( arquivo instanceof InputStream ) saxParser.parse( (InputStream) arquivo, conversor );
            
            return conversor.getTucurui();
            
        }catch( ParserConfigurationException e ){
            throw new SAXException( e );
        }
        
    }
    
    /**
     * {@link Transformer#transform(Source, Result) Transforma} um documento {@link Tucurui} em XML, conforme um {@link Transformer}.
     * @param tucurui {@link Tucurui} a ser transformado.
     * @param transformador {@link Transformer}.
     * @param destino Destino do resultado da transformação.
     * @param codificacao {@link Charset}.
     * @see #gerarXML(Tucurui, OutputStream, String)
     * @see #gerarHTML(Tucurui, OutputStream, String)
     * @see Tucurui#gerarDOM()
     */
    public static void transformar( Tucurui tucurui, Transformer transformador, OutputStream destino, String codificacao ) throws TucuruiException, TransformerException {
        try{
            transformador.transform( new DOMSource( tucurui.gerarDOM() ), new StreamResult( new OutputStreamWriter( destino, codificacao ) ) );
        }catch( UnsupportedEncodingException e ){
            throw new TransformerException( e );
        }
    }
    
    /**
     * Transforma um documento {@link Tucurui} em XML.
     * @param tucurui {@link Tucurui} a ser transformado.
     * @param destino Destino do resultado da transformação.
     * @param codificacao {@link Charset}.
     * @see #transformar(Tucurui, Transformer, OutputStream, String)
     */
    public static void gerarXML( Tucurui tucurui, OutputStream destino, String codificacao ) throws TucuruiException, TransformerException {
    
        Transformer transformador = TransformerFactory.newInstance().newTransformer();
        
        transformador.setOutputProperty( OutputKeys.METHOD, "xml" );
        transformador.setOutputProperty( OutputKeys.OMIT_XML_DECLARATION, "no" );
        transformador.setOutputProperty( OutputKeys.ENCODING, codificacao );
        transformador.setOutputProperty( OutputKeys.INDENT, "yes" );
        
        transformar( tucurui, transformador, destino, codificacao );
        
    }
    
    /**
     * Transforma um documento {@link Tucurui} em HTML.
     * @param tucurui {@link Tucurui} a ser transformado.
     * @param destino Destino do resultado da transformação.
     * @param codificacao {@link Charset}.
     * @see #transformar(Tucurui, Transformer, OutputStream, String)
     */
    public static void gerarHTML( Tucurui tucurui, OutputStream destino, String codificacao ) throws TucuruiException, TransformerException {
        
        Transformer transformador = TransformerFactory.newInstance().newTransformer();
        
        transformador.setOutputProperty( OutputKeys.METHOD, "html" );
        transformador.setOutputProperty( OutputKeys.ENCODING, codificacao );
        transformador.setOutputProperty( OutputKeys.INDENT, "yes" );
    
        transformar( tucurui, transformador, destino, codificacao );
        
    }
    
    /**
     * {@link TucuruiImpressora#imprimir(Tucurui, OutputStream) Imprime} um documento {@link Tucurui} de acordo com a {@link PadraoImpressora}.
     */
    public static void imprimir( Tucurui tucurui, OutputStream saida ) throws IOException, TucuruiException {
        new PadraoImpressora().imprimir( tucurui, saida );
    }
    
    /**
     * Formata adequadamente uma {@link String}, reconhecendo e convertendo os
     * codificadores de caracteres Unicode e os codificadores especiais.
     */
    public static String reconhecer( String texto ) {
        
        StringBuilder sbFinal = new StringBuilder( texto.length() );
        StringBuilder sbTemp  = new StringBuilder();
        
        boolean reconhecendo = false;
        char    especial     = 0;
        
        for( char ch : texto.toCharArray() ){
    
            if( especial != 0 ){
    
                if( ch == '}' ){
                    switch( especial ){
                        case 'n':
                            sbFinal.append( '\n' );
                            break;
                        case 't':
                            sbFinal.append( '\t' );
                            break;
                    }
                }else{
                    sbFinal.append( new char[]{ '{', especial, ch } );
                }
    
                reconhecendo = false;
                especial     = 0;
                
            }else if( reconhecendo ){
                
                if( ch == '}' ){
    
                    String unicode = sbTemp.toString();
                    sbTemp.delete( 0, sbTemp.length() );
    
                    try{
                        sbFinal.append( Character.toChars( Integer.parseInt( unicode, 16 ) ) );
                    }catch( Exception e ){
                        sbFinal.append( '{' + unicode + '}' );
                    }
    
                    reconhecendo = false;
    
                }else if( ch == 'n' || ch == 't' ){
                    
                    especial = ch;
    
                }else if( ( ch >= '0' && ch <= '9' ) || ( ch >= 'A' && ch <= 'F' ) || ( ch >= 'a' && ch <= 'f' ) ){
    
                    sbTemp.append( ch );
                    
                }else{
    
                    sbFinal.append( '{' + sbTemp.toString() + ch );
                    sbTemp.delete( 0, sbTemp.length() );
                    reconhecendo = false;
                    
                }
                
            }else if( ch == '{' ){
                
                reconhecendo = true;
                
            }else{
    
                sbFinal.append( ch );
                
            }
            
        }
        
        return sbFinal.toString();
        
    }
    
    private static String identificarCodificacao( InputStream is ) throws IOException {
        
        BufferedReader br = new BufferedReader( new InputStreamReader( is, "UTF-8" ) );
        
        String linha;
        while( ( linha = br.readLine() ) != null ){
            
            if( linha.isEmpty() ){
                continue;
                
            }else if( linha.charAt( 0 ) == '#' ){
                String[] p = linha.split( " " );
                if( p.length == 3 ) return p[2];
                break;
                
            }else if( linha.trim().length() > 0 ){
                break;
            }
            
        }
        
        return "UTF-8";
        
    }
    
    private static int getNivel( Hierarquia hierarquia ) {
        return hierarquia instanceof Elemento ? ((Elemento)hierarquia).getNivel() : -1;
    }
    
    private static TucuruiException novaTucuruiException( TucuruiException.Erro erro, int linha ) {
        return new TucuruiException( erro, null, erro.toString() + " (linha " + linha + ")" );
    }
    
}
