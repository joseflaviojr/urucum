
/*
 *  Copyright (C) 2016-2018 Jos� Fl�vio de Souza Dias J�nior
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
 *  Direitos Autorais Reservados (C) 2016-2018 Jos� Fl�vio de Souza Dias J�nior
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

package com.joseflavio.urucum.texto;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Utilit�rios para {@link String}.
 * @author Jos� Fl�vio de Souza Dias J�nior
 */
public class StringUtil {

	/**
	 * {@link MessageFormat Formata} uma mensagem textual, obtendo-a, se necess�rio e se poss�vel, de uma {@link ResourceBundle fonte} sens�vel � {@link Locale}.
	 * @param fonte Fonte de mensagens traduzidas para uma {@link Locale}. Opcional.
	 * @param mensagem Mensagem ou chave de mensagem desejada. Toda chave deve iniciar com "$" e ser� buscada na {@link ResourceBundle}.
	 * @param argumentos Argumentos para {@link MessageFormat#format(String, Object...)}. Podem iniciar com "$", caracterizando chaves.
	 * @see MessageFormat
	 * @see ResourceBundle
	 */
	public static String formatarMensagem( ResourceBundle fonte, String mensagem, Object... argumentos ) throws MissingResourceException {
		
		if( mensagem == null ){
			mensagem = "";
		}else if( mensagem.startsWith( "$" ) ){
			if( fonte != null ) mensagem = fonte.getString( mensagem.substring( 1 ) );
			else mensagem = mensagem.substring( 1 );
		}
		
		if( argumentos != null && argumentos.length > 0 ){
			
			Object[] args = new Object[ argumentos.length ];
			
			for( int i = 0; i < argumentos.length; i++ ){
				Object obj = argumentos[i];
				if( obj instanceof String && ((String)obj).startsWith( "$" ) ){
					if( fonte != null ) args[i] = fonte.getString( ((String)obj).substring( 1 ) );
					else args[i] = ((String)obj).substring( 1 );
				}else{
					args[i] = argumentos[i];
				}
			}
			
			return MessageFormat.format( mensagem, args );
			
		}else{
			
			return mensagem;
			
		}
		
	}
	
	/**
	 * {@link MessageFormat Formata} uma mensagem de acordo com uma {@link ResourceBundle}.
	 * @param fonte Fonte de mensagens traduzidas para uma {@link Locale}. Opcional.
	 * @param mensagem Chave da mensagem a ser buscada na {@link ResourceBundle}, conforme {@link #getMensagem(ResourceBundle, String)}.
	 * @param argumentos Argumentos para {@link MessageFormat#format(String, Object...)}. {@link String}s ser�o buscadas na {@link ResourceBundle}, conforme {@link #getMensagem(ResourceBundle, String)}.
	 * @see MessageFormat
	 * @see ResourceBundle
	 */
	public static String formatar( ResourceBundle fonte, String mensagem, Object... argumentos ) {
		
		mensagem = mensagem != null ? getMensagem( fonte, mensagem ) : "";
		
		if( argumentos != null && argumentos.length > 0 ){
			
			Object[] args = new Object[ argumentos.length ];
			
			for( int i = 0; i < argumentos.length; i++ ){
				Object obj = argumentos[i];
				args[i] = obj instanceof String ? getMensagem( fonte, (String) obj ) : obj;
			}
			
			return MessageFormat.format( mensagem, args );
			
		}else{
			
			return mensagem;
			
		}
		
	}
	
	/**
	 * Obt�m uma mensagem de uma {@link ResourceBundle}.
	 * @param fonte Fonte de mensagens traduzidas para uma {@link Locale}. Opcional.
	 * @param chave Chave da mensagem desejada. O primeiro {@link String#charAt(int) caractere} da chave ser� desconsiderado se ele for igual a '$'.
	 * @return o valor original da chave, caso a mensagem n�o seja encontrada.
	 */
	public static String getMensagem( ResourceBundle fonte, String chave ) {
		try{
			String k = chave != null && chave.length() > 0 && chave.charAt( 0 ) == '$' ? chave.substring( 1 ) : chave;
			return fonte != null ? fonte.getString( k ) : chave;
		}catch( Exception e ){
			return chave;
		}
	}
	
	/**
	 * {@link String#length()}
	 * @return 0, se <code>null</code>
	 */
	public static int tamanho( String texto ) {
		return texto != null ? texto.length() : 0;
	}
	
}
