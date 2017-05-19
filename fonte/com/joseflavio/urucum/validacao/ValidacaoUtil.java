
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

package com.joseflavio.urucum.validacao;

import com.joseflavio.urucum.aparencia.Nome;
import com.joseflavio.urucum.array.ArrayUtil;
import com.joseflavio.urucum.comunicacao.Mensagem;
import com.joseflavio.urucum.comunicacao.Mensagem.Tipo;
import com.joseflavio.urucum.javabeans.JavaBeansUtil;
import com.joseflavio.urucum.texto.StringUtil;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Utilitários relacionados à {@link Validacao}.
 * @author José Flávio de Souza Dias Júnior
 */
public class ValidacaoUtil {
	
	/**
	 * Valida um objeto conforme as regras de seus {@link Field campos}.<br>
	 * Regras possíveis:<br>
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
	 * @param desconsiderar {@link Escopo}s a desconsiderar. Opcional.
	 * @return <code>true</code>, se objeto totalmente válido.
	 */
	public static boolean validar(
			Object objeto, List<Mensagem> mensagens, boolean recursivamente,
			Map<String,Object> args, ResourceBundle fonte, Set<String> desconsiderar )
			throws IllegalAccessException, InstantiationException, InvocationTargetException {
		return validar0( objeto, mensagens, recursivamente, args, fonte, desconsiderar, new HashSet<Object>() );
	}
	
	/**
	 * @see #validar(Object, List, boolean, Map, ResourceBundle, Set)
	 */
	public static boolean validar(
			Object objeto, List<Mensagem> mensagens, boolean recursivamente,
			Map<String,Object> args, ResourceBundle fonte, String... desconsiderar )
			throws IllegalAccessException, InstantiationException, InvocationTargetException {
		Set<String> conjunto = new HashSet<String>();
		for( String escopo : desconsiderar ) conjunto.add( escopo );
		return validar( objeto, mensagens, recursivamente, args, fonte, conjunto );
	}
	
	/**
	 * @see #validar(Object, List, boolean, Map, ResourceBundle, Set)
	 */
	public static boolean validar(
			Object objeto, List<Mensagem> mensagens, ResourceBundle fonte, String... desconsiderar )
			throws IllegalAccessException, InstantiationException, InvocationTargetException {
		return validar( objeto, mensagens, true, null, fonte, desconsiderar );
	}
	
	private static boolean validar0(
			Object objeto, List<Mensagem> mensagens, boolean recursivamente,
			Map<String,Object> args, ResourceBundle fonte, Set<String> desconsiderar, Set<Object> validados )
			throws IllegalAccessException, InstantiationException, InvocationTargetException {
		
		validados.add( objeto );
		
		boolean      objetoValido = true;
		Class<?>     classe       = objeto.getClass();
		List<Object> msgArgs      = null;
		
		for( Field campo : JavaBeansUtil.getCampos( classe ) ){
			
			Regras regrasAnot = campo.getAnnotation( Regras.class );
			Field regras = regrasAnot != null ? JavaBeansUtil.procurarCampo( regrasAnot.classe(), regrasAnot.campo() ) : campo;
			if( regras == null ) throw new IllegalArgumentException( "Campo desconhecido: " + regrasAnot.classe() + "." + regrasAnot.campo() );
			
			if( desconsiderar != null && ! desconsiderar.isEmpty() ){
				Escopo escopo = campo.getAnnotation( Escopo.class );
				if( escopo != null && desconsiderar.contains( escopo.value() ) ){
					continue;
				}
			}
			
			NaoNulo        valNaoNulo   = regras.getAnnotation( NaoNulo.class );
			NaoVazio       valNaoVazio  = regras.getAnnotation( NaoVazio.class );
			Tamanho        valTamanho   = regras.getAnnotation( Tamanho.class );
			LimiteNumerico valLimNum    = regras.getAnnotation( LimiteNumerico.class );
			Formato        valFormato   = regras.getAnnotation( Formato.class );
			Validacao      valValidacao = regras.getAnnotation( Validacao.class );
			
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
			}else if( StringUtil.tamanho( nomeAnot.masculino() ) > 0 ){
				nome   = nomeAnot.masculino();
				genero = 0;
				numero = 0;
			}else if( StringUtil.tamanho( nomeAnot.feminino() ) > 0 ){
				nome   = nomeAnot.feminino();
				genero = 1;
				numero = 0;
			}else if( StringUtil.tamanho( nomeAnot.masculinoPlural() ) > 0 ){
				nome   = nomeAnot.masculinoPlural();
				genero = 0;
				numero = 1;
			}else if( StringUtil.tamanho( nomeAnot.femininoPlural() ) > 0 ){
				nome   = nomeAnot.femininoPlural();
				genero = 1;
				numero = 1;
			}
			
			if( valNaoNulo != null && valNaoNulo.criticidade() != Criticidade.IRRELEVANTE ) {
				if( valor == null ){
					objetoValido = false;
					String mensagem = StringUtil.formatar(
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
					String mensagem = StringUtil.formatar(
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
				//TODO com.joseflavio.urucum.comunicacao.Arquivo
				else if( valor instanceof Collection<?> ) tamanho = ((Collection<?>)valor).size();
				else if( valor instanceof Map<?,?> ) tamanho = ((Map<?,?>)valor).size();
				else if( valor.getClass().isArray() ) tamanho = Array.getLength( valor );
				else tamanho = valor.toString().length();
				if( tamanho < tamanhoMin || tamanho > tamanhoMax ){
					objetoValido = false;
					String mensagem = StringUtil.formatar(
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
					String mensagem = StringUtil.formatar(
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
					String mensagem = StringUtil.formatar(
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
					String mensagem = StringUtil.formatar(
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
				! tipoNome.startsWith( "java." ) && ! tipoNome.startsWith( "javax." ) &&
				! validados.contains( valor ) ){
				if( ! validar0( valor, mensagens, recursivamente, args, fonte, desconsiderar, validados ) ){
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
