
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

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.security.KeyStore;
import java.security.SecureRandom;

/**
 * {@link Servidor} baseado em {@link ServerSocket}.
 * @author Jos� Fl�vio de Souza Dias J�nior
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
	 * {@link Servidor} baseado em {@link ServerSocket}.<br>
	 * Caso se opte por comunica��o segura (TLS/SSL), deve-se especificar
	 * as {@link System#setProperty(String, String) propriedades} "javax.net.ssl.keyStore"
	 * e "javax.net.ssl.keyStorePassword".
	 * @param porta Veja {@link ServerSocket#getLocalPort()}
	 * @param segura Utilizar {@link SSLServerSocket}?
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
	 * {@link SocketServidor#SocketServidor(int, boolean, boolean)} sem resili�ncia.
	 */
	public SocketServidor( int porta, boolean segura ) throws IOException {
		this( porta, segura, false );
	}
	
	private void iniciar() throws IOException {
		
		try{
			
			if( segura ){
				
				String ksArquivo = System.getProperty( "javax.net.ssl.keyStore" );
				String ksSenha   = System.getProperty( "javax.net.ssl.keyStorePassword" );
				String ksTipo    = System.getProperty( "javax.net.ssl.keyStoreType" );
				
				if( ksArquivo != null && ! new File( ksArquivo ).exists() ) ksArquivo = null;
				if( ksSenha == null ) ksSenha = "";
				if( ksTipo == null ) ksTipo = "jks";
				
				KeyStore ks = KeyStore.getInstance( ksTipo );
				FileInputStream ksArquivo_is = ksArquivo != null ? new FileInputStream( ksArquivo ) : null;
				ks.load( ksArquivo_is, ksSenha.toCharArray() );
				if( ksArquivo_is != null ) ksArquivo_is.close();
				
				KeyManagerFactory kmf = KeyManagerFactory.getInstance( KeyManagerFactory.getDefaultAlgorithm() );
				kmf.init( ks, ksSenha.toCharArray() );

				SSLContext contexto = SSLContext.getInstance( "TLS" );
				contexto.init( kmf.getKeyManagers(), null, new SecureRandom() );
				socket = contexto.getServerSocketFactory().createServerSocket( porta );
				
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
