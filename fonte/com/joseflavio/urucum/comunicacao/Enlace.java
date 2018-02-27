
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

package com.joseflavio.urucum.comunicacao;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Enlace entre {@link OutputStream} e {@link InputStream}.
 * @author José Flávio de Souza Dias Júnior
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
	 * Tempo máximo de espera pela próxima {@link InputStream#read()}.
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
