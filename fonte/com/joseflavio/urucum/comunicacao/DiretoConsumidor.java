
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

package com.joseflavio.urucum.comunicacao;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Jos� Fl�vio de Souza Dias J�nior
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

}