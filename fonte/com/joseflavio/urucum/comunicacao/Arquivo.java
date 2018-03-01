
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

package com.joseflavio.urucum.comunicacao;

import java.io.File;
import java.io.Serializable;
import java.net.URL;

/**
 * Formato de {@link File} para intercâmbio. 
 * @author José Flávio de Souza Dias Júnior
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
	 * {@link File#getName() Nome} desejável para o arquivo.
	 */
	public String getNome() {
		return nome;
	}

	public void setNome( String nome ) {
		this.nome = nome;
	}

	/**
	 * Endereço do arquivo, normalmente {@link URL} ou {@link File#getPath()},
	 * através do qual se pode obter o seu {@link #getConteudo()}.
	 */
	public String getEndereco() {
		return endereco;
	}

	public void setEndereco( String endereco ) {
		this.endereco = endereco;
	}
	
	/**
	 * Descrição breve do arquivo.
	 */
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao( String descricao ) {
		this.descricao = descricao;
	}
	
	/**
	 * Conteúdo direto, podendo ou não corresponder ao conteúdo original do arquivo,
	 * disponível em {@link #getEndereco()}.
	 */
	public byte[] getConteudo() {
		return conteudo;
	}
	
	public void setConteudo( byte[] conteudo ) {
		this.conteudo = conteudo;
	}
	
}
