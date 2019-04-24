
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author José Flávio de Souza Dias Júnior
 */
class DiretoConsumidor implements Consumidor {
	
	private DiretoServidor servidor;
	private Enlace leitura;
	private Enlace escrita;
	
	DiretoConsumidor( DiretoServidor servidor, Enlace leitura, Enlace escrita ) {
		this.servidor = servidor;
		this.leitura  = leitura;
		this.escrita  = escrita;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		if( ! isAberto() ) throw new IOException( "Fechado" );
		return leitura.getInputStream();
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		if( ! isAberto() ) throw new IOException( "Fechado" );
		return escrita.getOutputStream();
	}

	@Override
	public boolean isAberto() {
		return servidor != null;
	}
	
	@Override
	public void setTempoEspera( int ms ) throws IOException {
		if( ! isAberto() ) throw new IOException( "Fechado" );
		leitura.setTempoEspera( ms );
	}
	
	@Override
	public Consumidor novoConsumidor() throws IOException {
		return servidor.novoConsumidor();
	}

	@Override
	public void fechar() throws IOException {
		servidor = null;
		leitura  = null;
		escrita  = null;
	}
	
	@Override
	public void close() throws IOException {
		fechar();
	}
	
}
