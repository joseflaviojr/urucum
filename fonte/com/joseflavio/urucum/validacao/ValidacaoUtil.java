
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

package com.joseflavio.urucum.validacao;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.joseflavio.urucum.aparencia.Nome;
import com.joseflavio.urucum.array.ArrayUtil;
import com.joseflavio.urucum.comunicacao.Mensagem;
import com.joseflavio.urucum.comunicacao.Mensagem.Tipo;
import com.joseflavio.urucum.javabeans.JavaBeansUtil;
import com.joseflavio.urucum.texto.StringUtil;

/**
 * Utilit�rios relacionados � {@link Validacao}.
 * @author Jos� Fl�vio de Souza Dias J�nior
 */
public class ValidacaoUtil {
	
	/**
	 * Valida um objeto conforme as regras de seus {@link Field campos}.<br>
	 * Regras poss�veis:<br>
	 * <ul>
	 * <li>{@link NaoNulo}</li>
	 * <li>{@link NaoVazio}</li>
	 * <li>{@link Tamanho}</li>
	 * <li>{@link LimiteNumerico}</li>
	 * <li>{@link Formato}</li>
	 * <li>{@link Validacao}</li>
	 * </ul>
	 * @param objeto Objeto a ser validado.
	 * @param mensagens Destino das {@link Mensagem} geradas.
	 * @param recursivamente Validar recursivamente os objetos internos?
	 * @param args Argumentos para {@link Validador}. Opcional.
	 * @param fonte {@link ResourceBundle} com as mensagens textuais. Opcional.
	 * @return <code>true</code>, se objeto totalmente v�lido.
	 */
	public static boolean validar(
			Object objeto, List<Mensagem> mensagens, boolean recursivamente,
			Map<String,Object> args, ResourceBundle fonte )
			throws IllegalAccessException, InstantiationException, InvocationTargetException {
		
		boolean objetoValido = true;
		Class<?> classe      = objeto.getClass();
		List<Object> msgArgs = null;
		
		for( Field campo : JavaBeansUtil.getCampos( classe ) ){
			
			NaoNulo        valNaoNulo   = campo.getAnnotation( NaoNulo.class );
			NaoVazio       valNaoVazio  = campo.getAnnotation( NaoVazio.class );
			Tamanho        valTamanho   = campo.getAnnotation( Tamanho.class );
			LimiteNumerico valLimNum    = campo.getAnnotation( LimiteNumerico.class );
			Formato        valFormato   = campo.getAnnotation( Formato.class );
			Validacao      valValidacao = campo.getAnnotation( Validacao.class );
			
			if( ArrayUtil.isTodosNulos(
					valNaoNulo, valNaoVazio, valTamanho,
					valLimNum, valFormato, valValidacao
			) ) continue;
			
			String   id    = campo.getName();
			Class<?> tipo  = campo.getType();
			Method   get   = JavaBeansUtil.procurarGet( campo );
			Object   valor = get != null ? get.invoke( objeto ) : campo.get( objeto );
			
			Nome   nomeAnot = campo.getAnnotation( Nome.class );
			String nome     = null;
			int    genero   = 0;
			int    numero   = 0;
			
			if( nomeAnot == null ){
				nome   = id;
				genero = 0;
				numero = 0;
			}else if( nomeAnot.masculino() != null ){
				nome   = nomeAnot.masculino();
				genero = 0;
				numero = 0;
			}else if( nomeAnot.feminino() != null ){
				nome   = nomeAnot.feminino();
				genero = 1;
				numero = 0;
			}else if( nomeAnot.masculinoPlural() != null ){
				nome   = nomeAnot.masculinoPlural();
				genero = 0;
				numero = 1;
			}else if( nomeAnot.femininoPlural() != null ){
				nome   = nomeAnot.femininoPlural();
				genero = 1;
				numero = 1;
			}
			
			if( valNaoNulo != null && valNaoNulo.criticidade() != Criticidade.IRRELEVANTE ) {
				if( valor == null ){
					objetoValido = false;
					String mensagem = StringUtil.formatarMensagem(
						fonte,
						valNaoNulo.mensagem(),
						nome,
						genero,
						numero
					);
					mensagens.add( novaMensagem( valNaoNulo.criticidade(), id, mensagem ) );
				}
			}
			
			if( valNaoVazio != null && valNaoVazio.criticidade() != Criticidade.IRRELEVANTE && valor != null ) {
				if( valor.toString().isEmpty() ){
					objetoValido = false;
					String mensagem = StringUtil.formatarMensagem(
						fonte,
						valNaoVazio.mensagem(),
						nome,
						genero,
						numero
					);
					mensagens.add( novaMensagem( valNaoVazio.criticidade(), id, mensagem ) );
				}
			}
			
			if( valTamanho != null && valTamanho.criticidade() != Criticidade.IRRELEVANTE && valor != null ) {
				long tamanhoMin = valTamanho.min();
				long tamanhoMax = valTamanho.max();
				long tamanho    = 0;
				if( valor instanceof String ) tamanho = ((String)valor).length();
				else if( valor instanceof File ) tamanho = ((File)valor).length();
				else if( valor instanceof Collection<?> ) tamanho = ((Collection<?>)valor).size();
				else if( valor instanceof Map<?,?> ) tamanho = ((Map<?,?>)valor).size();
				else if( valor.getClass().isArray() ) tamanho = Array.getLength( valor );
				else tamanho = valor.toString().length();
				if( tamanho < tamanhoMin || tamanho > tamanhoMax ){
					objetoValido = false;
					String mensagem = StringUtil.formatarMensagem(
						fonte,
						valTamanho.mensagem(),
						nome,
						genero,
						numero,
						tamanho,
						tamanhoMin,
						tamanhoMax
					);
					mensagens.add( novaMensagem( valTamanho.criticidade(), id, mensagem ) );
				}
			}
			
			if( valLimNum != null && valLimNum.criticidade() != Criticidade.IRRELEVANTE && valor != null ) {
				double numMin = valLimNum.min();
				double numMax = valLimNum.max();
				double num    = 0;
				if( valor instanceof Number ) num = ((Number)valor).doubleValue();
				else num = Double.parseDouble( valor.toString() );
				if( num < numMin || num > numMax ){
					objetoValido = false;
					String mensagem = StringUtil.formatarMensagem(
						fonte,
						valLimNum.mensagem(),
						nome,
						genero,
						numero,
						num,
						numMin,
						numMax
					);
					mensagens.add( novaMensagem( valLimNum.criticidade(), id, mensagem ) );
				}
			}
			
			if( valFormato != null && valFormato.criticidade() != Criticidade.IRRELEVANTE && valor != null ) {
				String texto  = valor.toString();
				String padrao = valFormato.padrao();
				if( ! texto.isEmpty() && ! texto.matches( padrao ) ){
					objetoValido = false;
					String mensagem = StringUtil.formatarMensagem(
						fonte,
						valFormato.mensagem(),
						nome,
						genero,
						numero,
						texto,
						padrao
					);
					mensagens.add( novaMensagem( valFormato.criticidade(), id, mensagem ) );
				}
			}
			
			if( valValidacao != null && valValidacao.criticidade() != Criticidade.IRRELEVANTE ) {
				Validador validador = valValidacao.classe().newInstance();
				if( args    == null ) args    = Collections.unmodifiableMap( new HashMap<String,Object>() );
				if( msgArgs == null ) msgArgs = new ArrayList<Object>();
				msgArgs.clear();
				if( ! validador.validar( valor, classe, id, objeto, args, msgArgs ) ){
					objetoValido = false;
					String mensagem = StringUtil.formatarMensagem(
						fonte,
						valValidacao.mensagem(),
						nome,
						genero,
						numero,
						valor,
						msgArgs.toArray()
					);
					mensagens.add( novaMensagem( valValidacao.criticidade(), id, mensagem ) );
				}
			}
			
			String tipoNome = tipo.getName();
			if( recursivamente && valor != null &&
				! tipo.isArray() && ! tipo.isPrimitive() &&
				! tipoNome.startsWith( "java." ) && ! tipoNome.startsWith( "javax." ) ){
				if( ! validar( valor, mensagens, recursivamente, args, fonte ) ){
					objetoValido = false;
				}
			}
			
		}
		
		return objetoValido;
		
	}
	
	private static Mensagem novaMensagem( Criticidade criticidade, String referencia, Serializable argumento ) {
		switch( criticidade ){
			case MEDIA :
			case ALTA  :
				return new Mensagem( Tipo.ERRO, referencia, argumento );
			default :
				return new Mensagem( Tipo.ATENCAO, referencia, argumento );
		}
	}

}
