
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

package com.joseflavio.urucum.tucurui;

import com.joseflavio.urucum.texto.StringUtil;

/**
 * {@link Exception} de {@link Tucurui}.
 * @author Jos� Fl�vio de Souza Dias J�nior
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
