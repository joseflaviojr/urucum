
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

package com.joseflavio.urucum.tucurui;

/**
 * {@link Exception} de {@link Tucurui}.
 * @author José Flávio de Souza Dias Júnior
 * @see <a href="http://joseflavio.com/tucurui">http://joseflavio.com/tucurui</a>
 */
public class TucuruiException extends Exception {
	
	private Erro erro;
	
	private int linha;
	
	public TucuruiException( Erro erro, int linha, String mensagem, Throwable causa ) {
		super( mensagem, causa );
		this.erro = erro;
		this.linha = linha;
	}
	
	public TucuruiException( Erro erro, int linha, String mensagem ) {
		this( erro, linha, mensagem, null );
	}
	
	public TucuruiException( Erro erro, int linha, Throwable causa ) {
		this( erro, linha, linha + ": " + erro.toString(), causa );
	}
	
	public TucuruiException( Erro erro, int linha ) {
		this( erro, linha, (Throwable) null );
	}
	
	public TucuruiException( Throwable causa ) {
		this( Erro.DESCONHECIDO, 0, causa );
	}
	
	public TucuruiException() {
		this( null );
	}
	
	public Erro getErro() {
		return erro;
	}
	
	public void setErro( Erro erro ) {
		this.erro = erro;
	}
	
	public int getLinha() {
		return linha;
	}
	
	public void setLinha( int linha ) {
		this.linha = linha;
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
