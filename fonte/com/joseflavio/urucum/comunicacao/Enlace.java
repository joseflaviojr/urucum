
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
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Enlace entre {@link OutputStream} e {@link InputStream}.
 * @author Jos� Fl�vio de Souza Dias J�nior
 */
class Enlace {
	
	private byte[] canal  = new byte[1024];
	private int    indice = 0;
	private int    total  = 0;
	
	private InputStream  entrada = new InputStreamImpl();
	private OutputStream saida   = new OutputStreamImpl();
	
	private ReentrantLock trava    = new ReentrantLock();
	private Condition     condicao = trava.newCondition();
	
	private int tempoEspera = 0;
	
	public InputStream getInputStream() {
		return entrada;
	}

	public OutputStream getOutputStream() {
		return saida;
	}

	@Override
	protected void finalize() throws Throwable {
		canal    = null;
		entrada  = null;
		saida    = null;
		trava    = null;
		condicao = null;
	}

	/**
	 * Tempo m�ximo de espera pela pr�xima {@link InputStream#read()}.
	 * @param ms Milissegundos. 0 = infinito.
	 */
	public void setTempoEspera( int ms ) {
		if( ms < 0 ) throw new IllegalArgumentException();
		this.tempoEspera = ms;
	}
	
	private class InputStreamImpl extends InputStream {
		
		@Override
		public int available() throws IOException {
			return total;
		}
		
		@Override
		public int read() throws IOException {
			trava.lock();
			try{
				if( total == 0 ){
					if( tempoEspera == 0 ){
						condicao.await();
					}else if( ! condicao.await( tempoEspera, TimeUnit.MILLISECONDS ) ){
						throw new IOException( "Tempo > " + tempoEspera + " ms" );
					}
				}
				total--;
				return canal[indice++] & 0xff;
			}catch( InterruptedException e ){
				throw new IOException( e );
			}finally{
				trava.unlock();
			}
		}
		
		@Override
		public int read( byte[] b, int off, int len ) throws IOException {
			int inicio = off;
			b[off++] = (byte) read();
			len--;
			while( len-- > 0 ){
				if( available() == 0 ) break;
				b[off++] = (byte) read();
			}
			return off - inicio;
		}
		
		@Override
		public long skip( long n ) throws IOException {
			long total = n;
			while( n-- > 0 ) read();
			return total;
		}
		
		@Override
		public void close() throws IOException {
		}
		
	}
	
	private class OutputStreamImpl extends OutputStream {
		
		@Override
		public void write( int b ) throws IOException {
			trava.lock();
			try{
				if( total == 0 ){
					canal[0] = (byte) b;
					total  = 1;
					indice = 0;
				}else if( (indice+total) < canal.length ){
					canal[indice+total] = (byte) b;
					total++;
				}else if( indice > 0 ){
					System.arraycopy( canal, indice, canal, 0, total );
					canal[total] = (byte) b;
					total++;
					indice = 0;
				}else{
					byte[] novo = new byte[ (int)( canal.length * 1.1f ) ];
					System.arraycopy( canal, indice, novo, 0, total );
					canal = novo;
					canal[total] = (byte) b;
					total++;
					indice = 0;
				}
				condicao.signalAll();
			}finally{
				trava.unlock();
			}
		}
		
		@Override
		public void write( byte[] b, int off, int len ) throws IOException {
			while( len-- > 0 ) write( b[off++] );
		}
		
		@Override
		public void write( byte[] b ) throws IOException {
			write( b, 0, b.length );
		}
		
		@Override
		public void flush() throws IOException {
		}
		
		@Override
		public void close() throws IOException {
		}
		
	}

}
