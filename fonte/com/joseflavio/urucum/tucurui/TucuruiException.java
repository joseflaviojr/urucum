
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

package com.joseflavio.urucum.tucurui;

/**
 * {@link Exception} de {@link Tucurui}.
 * @author Jos� Fl�vio de Souza Dias J�nior
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
