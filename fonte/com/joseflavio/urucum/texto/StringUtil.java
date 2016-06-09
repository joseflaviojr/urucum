
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
	 * {@link MessageFormat Formata} uma mensagem textual, obtendo-a, se necess�rio e se poss�vel, de uma {@link ResourceBundle fonte} sens�vel a {@link Locale}.
	 * @param fonte {@link ResourceBundle} que cont�m as mensagens textuais chaveadas. Opcional.
	 * @param mensagem Mensagem ou chave de mensagem desejada. Toda chave deve iniciar com "$" e ser� buscada na {@link ResourceBundle}.
	 * @param parametros Par�metros para {@link MessageFormat}. Podem iniciar com "$".
	 * @see MessageFormat
	 * @see ResourceBundle
	 */
	public static String formatarMensagem( ResourceBundle fonte, String mensagem, Object... parametros ) throws MissingResourceException {
		
		if( mensagem == null ){
			mensagem = "";
		}else if( mensagem.startsWith( "$" ) ){
			if( fonte != null ) mensagem = fonte.getString( mensagem.substring( 1 ) );
			else mensagem = mensagem.substring( 1 );
		}
		
		if( parametros != null && parametros.length > 0 ){
			
			for( int i = 0; i < parametros.length; i++ ){
				Object p = parametros[i];
				if( p instanceof String && ((String)p).startsWith( "$" ) ){
					if( fonte != null ) parametros[i] = fonte.getString( ((String)p).substring( 1 ) );
					else parametros[i] = ((String)p).substring( 1 );
				}
			}
			
			return new MessageFormat( mensagem ).format( parametros );
			
		}else{
			
			return mensagem;
			
		}
		
	}
	
}
