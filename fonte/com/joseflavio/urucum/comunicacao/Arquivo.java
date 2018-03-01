
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

package com.joseflavio.urucum.comunicacao;

import java.io.File;
import java.io.Serializable;
import java.net.URL;

/**
 * Formato de {@link File} para interc�mbio. 
 * @author Jos� Fl�vio de Souza Dias J�nior
 */
public class Arquivo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String nome;
	
	private String endereco;
	
	private String descricao;
	
	private byte[] conteudo;
	
	public Arquivo() {
	}

	public Arquivo( String nome, String endereco ) {
		this.nome = nome;
		this.endereco = endereco;
	}

	/**
	 * {@link File#getName() Nome} desej�vel para o arquivo.
	 */
	public String getNome() {
		return nome;
	}

	public void setNome( String nome ) {
		this.nome = nome;
	}

	/**
	 * Endere�o do arquivo, normalmente {@link URL} ou {@link File#getPath()},
	 * atrav�s do qual se pode obter o seu {@link #getConteudo()}.
	 */
	public String getEndereco() {
		return endereco;
	}

	public void setEndereco( String endereco ) {
		this.endereco = endereco;
	}
	
	/**
	 * Descri��o breve do arquivo.
	 */
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao( String descricao ) {
		this.descricao = descricao;
	}
	
	/**
	 * Conte�do direto, podendo ou n�o corresponder ao conte�do original do arquivo,
	 * dispon�vel em {@link #getEndereco()}.
	 */
	public byte[] getConteudo() {
		return conteudo;
	}
	
	public void setConteudo( byte[] conteudo ) {
		this.conteudo = conteudo;
	}
	
}
