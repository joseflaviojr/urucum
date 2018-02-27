
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

package com.joseflavio.urucum.aparencia;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.joseflavio.urucum.texto.StringUtil;

/**
 * Denomina��o.
 * @author Jos� Fl�vio de Souza Dias J�nior
 */
@Target({
	ElementType.PACKAGE,
	ElementType.TYPE,
	ElementType.ANNOTATION_TYPE,
	ElementType.FIELD,
	ElementType.CONSTRUCTOR,
	ElementType.METHOD
})
@Retention(RetentionPolicy.RUNTIME)
public @interface Nome {
	
	/**
	 * Nome masculino no singular.
	 * @see StringUtil#formatar(java.util.ResourceBundle, String, Object...)
	 */
	String masculino() default "";
	
	/**
	 * Nome masculino no plural.
	 * @see StringUtil#formatar(java.util.ResourceBundle, String, Object...)
	 */
	String masculinoPlural() default "";
	
	/**
	 * Nome feminino no singular.
	 * @see StringUtil#formatar(java.util.ResourceBundle, String, Object...)
	 */
	String feminino() default "";
	
	/**
	 * Nome feminino no plural.
	 * @see StringUtil#formatar(java.util.ResourceBundle, String, Object...)
	 */
	String femininoPlural() default "";
	
}
