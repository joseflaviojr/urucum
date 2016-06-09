
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

package com.joseflavio.urucum.texto;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Utilitários para {@link String}.
 * @author José Flávio de Souza Dias Júnior
 */
public class StringUtil {

	/**
	 * {@link MessageFormat Formata} uma mensagem textual, obtendo-a, se necessário e se possível, de uma {@link ResourceBundle fonte} sensível a {@link Locale}.
	 * @param fonte {@link ResourceBundle} que contém as mensagens textuais chaveadas. Opcional.
	 * @param mensagem Mensagem ou chave de mensagem desejada. Toda chave deve iniciar com "$" e será buscada na {@link ResourceBundle}.
	 * @param parametros Parâmetros para {@link MessageFormat}. Podem iniciar com "$".
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
