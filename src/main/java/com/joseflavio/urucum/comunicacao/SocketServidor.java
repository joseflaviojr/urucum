
/*
 *  Copyright (C) 2016-2021 José Flávio de Souza Dias Júnior
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
 *  Direitos Autorais Reservados (C) 2016-2021 José Flávio de Souza Dias Júnior
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
import java.net.ServerSocket;

import javax.net.ssl.SSLServerSocket;

/**
 * {@link Servidor} baseado em {@link ServerSocket}.
 * @author José Flávio de Souza Dias Júnior
 * @see ServerSocket
 * @see SSLServerSocket
 */
public class SocketServidor implements Servidor {
	
	private int porta;
	
	private boolean segura;
	
	private boolean resiliente;
	
	private ServerSocket socket;
	
	private boolean fechado = false;
	
	/**
	 * {@link Servidor} baseado em {@link ServerSocket}.
	 * @param porta Veja {@link ServerSocket#getLocalPort()}
	 * @param segura Utilizar {@link SSLServerSocket}? Veja {@link ComunicacaoUtil#iniciarSSLContext()}.
	 * @param resiliente Ser resiliente a falhas?
	 */
	public SocketServidor( int porta, boolean segura, boolean resiliente ) throws IOException {
		
		this.porta = porta;
		this.segura = segura;
		this.resiliente = resiliente;
		
		try{
			iniciar();
		}catch( IOException e ){
			if( ! resiliente ) throw e;
		}
		
	}
	
	/**
	 * {@link SocketServidor#SocketServidor(int, boolean, boolean)} sem resiliência.
	 */
	public SocketServidor( int porta, boolean segura ) throws IOException {
		this( porta, segura, false );
	}
	
	private void iniciar() throws IOException {
		
		try{
			
			if( segura ){
				socket = ComunicacaoUtil
					.iniciarSSLContext()
					.getServerSocketFactory()
					.createServerSocket( porta );
			}else{
			    socket = new ServerSocket( porta );
			}
			
		}catch( IOException e ){
			throw e;
		}catch( Exception e ){
			throw new IOException( e );
		}
		
	}
	
	private void restaurar() throws InterruptedException {
		
		try{
			if( socket != null ) socket.close();
		}catch( Exception e ){
		}finally{
			socket = null;
		}
		
		while( socket == null ){
			try{
				iniciar();
			}catch( Exception e ){
				if( e instanceof InterruptedException ) throw (InterruptedException) e;
				Thread.sleep( 1000 );
			}
		}
		
	}

	@Override
	public Consumidor aceitar() throws IOException {
		
		while( true ){
			
			if( socket == null ){
				if( resiliente && ! fechado ){
					try{
						restaurar();
					}catch( InterruptedException e ){
						throw new IOException( e );
					}
				}else{
					throw new IOException( "ServerSocket fechado." );
				}
			}
			
			try{
				
				return new SocketConsumidor( socket.accept() );
				
			}catch( IOException e ){
				if( resiliente && ! fechado ){
					try{
						socket.close();
					}catch( IOException f ){
					}finally{
						socket = null;
					}
				}else{
					throw e;
				}
			}
			
		}
		
	}

	@Override
	public boolean isAberto() {
		if( resiliente ){
			return ! fechado;
		}else{
			return socket != null && ! socket.isClosed();
		}
	}

	@Override
	public void fechar() throws IOException {
		fechado = true;
		if( socket != null ){
			socket.close();
			socket = null;
		}
	}

}
