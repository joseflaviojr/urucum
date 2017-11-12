
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

import com.joseflavio.urucum.texto.StringUtil;

import java.io.*;

/**
 * {@link TucuruiImpressora} padrão.
 * @author José Flávio de Souza Dias Júnior
 */
public class PadraoImpressora extends TucuruiImpressora {
    
    @Override
    public void imprimir( Tucurui tucurui, OutputStream saida, String codificacao ) throws IOException, TucuruiException {
        
        try( BufferedWriter w = new BufferedWriter( new OutputStreamWriter( saida, codificacao ) ) ){
            
            w.write( "# " + tucurui.getVersao() + " " + codificacao.toUpperCase() + "\n" );
            
            if( StringUtil.tamanho( tucurui.getModelo() ) > 0 ){
                w.write( "@ " + tucurui.getModelo() + "\n" );
            }
    
            w.write( '\n' );
    
            imprimirSubelementos( tucurui, 0, 0, w );
            
        }
	    
	}
    
    private int imprimir( Hierarquia mae, int filho, int nivel, Writer w ) throws IOException, TucuruiException {
    
        Elemento elemento = mae.get( filho );
        int impressos = 0;
	    
	    if( elemento instanceof Objeto ){
	        
            Objeto objeto = (Objeto) elemento;
        
            indentar( nivel, w );
            w.write( ( objeto.isPrivado() ? "-" : "" ) + objeto.getNome() );
            impressos++;
        
            int inicio;
            if( isValorEmbutido( objeto ) ){
                imprimir( objeto, 0, 0, w );
                inicio = 1;
            }else{
                w.write( '\n' );
                inicio = 0;
            }
        
            imprimirSubelementos( objeto, inicio, nivel + 1, w );
            
        }else if( elemento instanceof Valor ){
        
            Valor valor;
	        final int quantidade = contarValoresLivres( mae, filho );

	        if( quantidade > 1 ){
        
                indentar( nivel, w );
	            w.write( "---\n" );
	            
                for( int i = filho, fim = filho + quantidade - 1; i < fim; i++ ){
                    valor = (Valor) mae.get( i );
                    String texto = valor.getTexto();
                    indentar( nivel, w );
                    w.write( texto.substring( 0, texto.length() - 3 ) + "\n" );
                    impressos++;
                }
        
                valor = (Valor) mae.get( filho + quantidade - 1 );
                indentar( nivel, w );
                w.write( valor.getTexto() + "\n" );
                impressos++;
                
                indentar( nivel, w );
                w.write( "---\n" );
                
            }else{
	            
                valor = (Valor) elemento;
                indentar( nivel, w );
                w.write( ": " + valor.getTexto() + "\n" );
                impressos++;
                
            }
        
            imprimirSubelementos( valor, 0, nivel + 1, w );
        
        }else if( elemento instanceof Comentario ){
        
            indentar( nivel, w );
            w.write( "//" + ((Comentario)elemento).getTexto() + "\n" );
            impressos++;
        
            imprimirSubelementos( elemento, 0, nivel + 1, w );
            
        }
        
        return impressos;
        
    }
    
    private void imprimirSubelementos( Hierarquia mae, int inicio, int nivel, Writer w ) throws IOException, TucuruiException {
        final int total = mae.size();
        for( int i = inicio; i < total; ){
            i += imprimir( mae, i, nivel, w );
        }
    }
    
    private static void indentar( int nivel, Writer w ) throws IOException {
        for( int i = 0; i < nivel; i++ ){
            w.write( "    " );
        }
    }
    
    private static boolean isValorEmbutido( Objeto objeto ) {
	    
	    int elementos = objeto.size();
	    if( elementos == 0 ) return false;
	    
	    Elemento primeiro = objeto.get( 0 );
        if( !( primeiro instanceof Valor ) || primeiro.size() > 0 ) return false;
        
	    if( elementos > 1 && objeto.get( 1 ) instanceof Valor ) return false;
	    
	    return true;
	    
    }
    
    private static int contarValoresLivres( Hierarquia h, int inicio ) {
	    
	    int valoresLivres = 0;
	    
	    final int elementos = h.size();
        for( int i = inicio; i < elementos; i++ ){
            
            Elemento elemento = h.get( i );
            if( !( elemento instanceof Valor ) ) break;
    
            valoresLivres++;
            
            if( elemento.size() > 0 || ! ((Valor)elemento).getTexto().endsWith( "{n}" ) ){
                break;
            }
            
        }
        
        return valoresLivres;
    
    }
    
}
