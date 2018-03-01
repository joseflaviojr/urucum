
/*
 *  Copyright (C) 2016-2018 Jos� Fl�vio de Souza Dias J�nior
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
 *  Direitos Autorais Reservados (C) 2016-2018 Jos� Fl�vio de Souza Dias J�nior
 * 
 *  Este arquivo � parte de Urucum - <http://joseflavio.com/urucum/>.
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

package com.joseflavio.urucum.validacao;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * Validador de conte�do/valor de {@link Field}.
 * @author Jos� Fl�vio de Souza Dias J�nior
 */
public interface Validador {

	/**
	 * Valida o conte�do/valor de um {@link Field campo}.<br>
	 * Todos os par�metros s�o obrigat�rios, com exce��o do conte�do/valor, que pode ser <code>null</code>.
	 * @param conteudo Conte�do/valor a ser validado.
	 * @param classe {@link Class} que cont�m, diretamente ou por heran�a, o campo que ser� validado.
	 * @param campo {@link Field#getName()}
	 * @param objeto Objeto que agrega o conte�do/valor.
	 * @param args Argumentos para este processo de valida��o.
	 * @param mensagemArgs Argumentos extras a serem repassados para {@link Validacao#mensagem()}.
	 * @return <code>true</code>, se <code>conteudo</code> v�lido.
	 */
	boolean validar( Object conteudo, Class<?> classe, String campo, Object objeto, Map<String,Object> args, List<Object> mensagemArgs );
	
}
