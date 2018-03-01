
/*
 *  Copyright (C) 2016-2018 José Flávio de Souza Dias Júnior
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
 *  Direitos Autorais Reservados (C) 2016-2018 José Flávio de Souza Dias Júnior
 * 
 *  Este arquivo é parte de Urucum - <http://joseflavio.com/urucum/>.
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

package com.joseflavio.urucum.validacao;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * Validador de conteúdo/valor de {@link Field}.
 * @author José Flávio de Souza Dias Júnior
 */
public interface Validador {

	/**
	 * Valida o conteúdo/valor de um {@link Field campo}.<br>
	 * Todos os parâmetros são obrigatórios, com exceção do conteúdo/valor, que pode ser <code>null</code>.
	 * @param conteudo Conteúdo/valor a ser validado.
	 * @param classe {@link Class} que contém, diretamente ou por herança, o campo que será validado.
	 * @param campo {@link Field#getName()}
	 * @param objeto Objeto que agrega o conteúdo/valor.
	 * @param args Argumentos para este processo de validação.
	 * @param mensagemArgs Argumentos extras a serem repassados para {@link Validacao#mensagem()}.
	 * @return <code>true</code>, se <code>conteudo</code> válido.
	 */
	boolean validar( Object conteudo, Class<?> classe, String campo, Object objeto, Map<String,Object> args, List<Object> mensagemArgs );
	
}
