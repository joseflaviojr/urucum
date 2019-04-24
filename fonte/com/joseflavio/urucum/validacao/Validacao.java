
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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import com.joseflavio.urucum.aparencia.Nome;
import com.joseflavio.urucum.texto.StringUtil;

/**
 * {@link Validador} de {@link Field}.
 * @author José Flávio de Souza Dias Júnior
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Validacao {
	
	/**
	 * @see Validador
	 */
	Class<? extends Validador> classe();
	
	Criticidade criticidade() default Criticidade.ALTA;
	
	/**
	 * Mensagem de erro, conforme {@link StringUtil#formatar(java.util.ResourceBundle, String, Object...)}.<br>
	 * Parâmetros:
	 * <pre>
	 * {0} = {@link String}  = {@link Nome} da variável
	 * {1} = {@link Integer} = gênero do nome: 0 (masculino) ou 1 (feminino)
	 * {2} = {@link Integer} = número do nome: 0 (singular) ou 1 (plural)
	 * {3} = {@link Object}  = valor atual
	 * {N} = {@link String}  = outros argumentos, especificados pelo {@link Validador#validar(Object, Class, String, Object, java.util.Map, java.util.List) validador}
	 * </pre>
	 */
	String mensagem() default "Erro_Validacao";
	
}
