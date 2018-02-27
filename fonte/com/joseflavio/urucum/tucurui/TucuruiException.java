
/*
 *  Copyright (C) 2016-2018 José Flávio de Souza Dias Júnior
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
 *  Direitos Autorais Reservados (C) 2016-2018 José Flávio de Souza Dias Júnior
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

/**
 * {@link Exception} de {@link Tucurui}.
 * @author José Flávio de Souza Dias Júnior
 * @see <a href="http://joseflavio.com/tucurui">http://joseflavio.com/tucurui</a>
 */
public class TucuruiException extends Exception {
	
	private Erro erro;
	
	private String endereco;
	
	/**
	 * @param erro {@link Erro}. {@code null} == {@link Erro#DESCONHECIDO}.
	 * @param endereco {@link Hierarquia#getEndereco()} ou {@code null}.
	 * @param mensagem {@link Exception#getMessage()} ou {@code null}.
	 * @param causa {@link Exception#getCause()} ou {@code null}.
	 */
	public TucuruiException( Erro erro, String endereco, String mensagem, Throwable causa ) {
		super( formatarMensagem( erro, endereco, mensagem, causa ), causa );
		this.erro = erro != null ? erro : Erro.DESCONHECIDO;
		this.endereco = endereco;
	}
	
	/**
     * @param erro {@link Erro}. {@code null} == {@link Erro#DESCONHECIDO}.
     * @param endereco {@link Hierarquia#getEndereco()} ou {@code null}.
	 * @param mensagem {@link Exception#getMessage()} ou {@code null}.
	 */
	public TucuruiException( Erro erro, String endereco, String mensagem ) {
		this( erro, endereco, mensagem, null );
	}
	
	/**
     * @param erro {@link Erro}. {@code null} == {@link Erro#DESCONHECIDO}.
     * @param endereco {@link Hierarquia#getEndereco()} ou {@code null}.
	 * @param causa {@link Exception#getCause()} ou {@code null}.
	 */
	public TucuruiException( Erro erro, String endereco, Throwable causa ) {
		this( erro, endereco, null, causa );
	}
	
	/**
     * @param erro {@link Erro}. {@code null} == {@link Erro#DESCONHECIDO}.
     * @param endereco {@link Hierarquia#getEndereco()} ou {@code null}.
	 */
	public TucuruiException( Erro erro, String endereco ) {
		this( erro, endereco, (Throwable) null );
	}
	
	/**
	 * @param causa {@link Exception#getCause()} ou {@code null}.
	 */
	public TucuruiException( Throwable causa ) {
		this( Erro.DESCONHECIDO, null, causa );
	}
	
	public TucuruiException() {
		this( null );
	}
    
    /**
     * {@link Erro}, jamais {@code null}.
     */
	public Erro getErro() {
		return erro;
	}
    
    /**
     * {@link Hierarquia#getEndereco()} ou {@code null}.
     */
	public String getEndereco() {
		return endereco;
	}
	
	private static String formatarMensagem( Erro erro, String endereco, String mensagem, Throwable causa ) {
	    mensagem = erro.toString() + ( StringUtil.tamanho( mensagem ) > 0 ? ": " + mensagem : "" );
        if( StringUtil.tamanho( endereco ) > 0 ) mensagem += " ("+ endereco + ")";
        return mensagem;
    }
	
	public enum Erro {
		DESCONHECIDO,
		CABECALHO_INVALIDO,
		INDENTACAO_INCORRETA,
		VALOR_LIVRE_NAO_FECHADO,
		ESPACO_ESPERADO,
		NOME_INVALIDO,
		SEMANTICA_INCORRETA
	}
	
}
