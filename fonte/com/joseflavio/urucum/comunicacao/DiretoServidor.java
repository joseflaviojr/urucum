
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

package com.joseflavio.urucum.comunicacao;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * {@link Servidor} que estabelece enlace com seus {@link Consumidor}es atrav�s
 * da mem�ria local.
 * @author Jos� Fl�vio de Souza Dias J�nior
 */
public class DiretoServidor implements Servidor {
	
	private Queue<Consumidor> fila = new LinkedList<Consumidor>();
	
	@Override
	public synchronized Consumidor aceitar() throws IOException {
		if( ! isAberto() ) throw new IOException( "Fechado" );
		try{
			if( fila.isEmpty() ) wait();
			return fila.poll();
		}catch( InterruptedException e ){
			throw new IOException( e );
		}
	}
	
	public synchronized Consumidor novoConsumidor() throws IOException {
		if( ! isAberto() ) throw new IOException( "Fechado" );
		Enlace enlace1 = new Enlace();
		Enlace enlace2 = new Enlace();
		fila.offer( new DiretoConsumidor( this, enlace1, enlace2 ) );
		notifyAll();
		return new DiretoConsumidor( this, enlace2, enlace1 );
	}

	@Override
	public boolean isAberto() {
		return fila != null;
	}

	@Override
	public synchronized void fechar() throws IOException {
		fila.clear();
		fila = null;
	}

}
