
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
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;

/**
 * {@link Consumidor} baseado em {@link Socket}.
 * @author José Flávio de Souza Dias Júnior
 * @see Socket
 * @see SSLSocket
 */
public class SocketConsumidor implements Consumidor {
	
	private String endereco;
	private int porta;
	private boolean segura;
	private boolean ignorarCertificado;
	
	private Socket socket;
	private InputStream entrada;
	private OutputStream saida;
	
    /**
     * {@link Consumidor} baseado em {@link Socket}.<br>
     * Caso se opte por comunicação segura (TLS/SSL) e o {@link Certificate} do {@link SocketServidor} seja
     * autoassinado, deve-se especificar as {@link System#setProperty(String, String) propriedades}
     * "javax.net.ssl.trustStore" e "javax.net.ssl.trustStorePassword" referentes ao {@link KeyStore} local
     * que contém o {@link Certificate} do {@link Servidor}.
     * @param endereco Veja {@link InetAddress}
     * @param porta Veja {@link InetSocketAddress#getPort()}
     * @param segura Utilizar {@link SSLSocket}?
     * @param ignorarCertificado Ignorar {@link Certificate}s não reconhecidos? Caso <code>true</code>, "javax.net.ssl.trustStore" será desconsiderado.
     */
	public SocketConsumidor( String endereco, int porta, boolean segura, boolean ignorarCertificado ) throws IOException {
		
		try{
			
			this.endereco = endereco;
			this.porta = porta;
			this.segura = segura;
			this.ignorarCertificado = ignorarCertificado;
			
			if( segura ){

				SSLContext contexto = SSLContext.getInstance( "TLS" );
				
				if( ignorarCertificado ){
			        
			        contexto.init(
						null,
						new TrustManager[]{ new InseguroX509TrustManager() },
						new SecureRandom()
					);
			        
				}else{
					
					contexto.init(
						null,
						ComunicacaoUtil.iniciarTrustManagerFactory().getTrustManagers(),
						new SecureRandom()
					);
					
				}
				
				this.socket = contexto.getSocketFactory().createSocket( endereco, porta );
				
			}else{
				
			    this.socket = new Socket( endereco, porta );
			    
			}
			
		}catch( IOException e ){
			throw e;
		}catch( Exception e ){
			throw new IOException( e );
		}
		
	}
	
	/**
	 * {@link SocketConsumidor} sem TLS/SSL.
	 * @see #SocketConsumidor(String, int, boolean, boolean)
	 */
	public SocketConsumidor( String endereco, int porta ) throws IOException {
		this( endereco, porta, false, true );
	}
	
	/**
	 * @see #SocketConsumidor(String, int, boolean, boolean)
	 */
	SocketConsumidor( Socket socket ) {
		this.socket = socket;
		this.endereco = socket.getInetAddress().getHostAddress();
		this.porta = socket.getPort();
		this.segura = socket instanceof SSLSocket;
		this.ignorarCertificado = false;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		if( socket == null ) throw new IOException( "Socket fechado." );
		if( entrada == null ) entrada = new InputStreamImpl();
		return entrada;
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		if( socket == null ) throw new IOException( "Socket fechado." );
		if( saida == null ) saida = new OutputStreamImpl();
		return saida;
	}

	@Override
	public boolean isAberto() {
		return socket != null && ! socket.isClosed();
	}

	@Override
	public void setTempoEspera( int ms ) throws IOException {
		if( socket == null ) throw new IOException( "Socket fechado." );
		socket.setSoTimeout( ms );
	}
	
	@Override
	public Consumidor novoConsumidor() throws IOException {
		return new SocketConsumidor( endereco, porta, segura, ignorarCertificado );
	}
	
	@Override
	public void fechar() throws IOException {
		if( socket == null ) throw new IOException( "Socket fechado." );
		socket.close();
		socket  = null;
		entrada = null;
		saida   = null;
	}
	
	@Override
	public void close() throws IOException {
		if( socket != null ){
			socket.close();
			socket  = null;
			entrada = null;
			saida   = null;
		}
	}
	
	@Override
	public String toString() {
		return socket == null ? "" : socket.getInetAddress().getHostName() + ":" + socket.getPort();
	}
	
	private class InputStreamImpl extends InputStream {
		
		private InputStream is;
		
		public InputStreamImpl() throws IOException {
			this.is = socket.getInputStream();
		}

		@Override
		public int available() throws IOException {
			try{
				return is.available();
			}catch( IOException e ){
				try{ fechar(); }catch( Exception f ){}
				throw e;
			}
		}
		
		@Override
		public int read() throws IOException {
			try{
				return is.read();
			}catch( IOException e ){
				try{ fechar(); }catch( Exception f ){}
				throw e;
			}
		}
		
		@Override
		public int read( byte[] b, int off, int len ) throws IOException {
			try{
				return is.read( b, off, len );
			}catch( IOException e ){
				try{ fechar(); }catch( Exception f ){}
				throw e;
			}
		}
		
		@Override
		public int read( byte[] b ) throws IOException {
			try{
				return is.read( b );
			}catch( IOException e ){
				try{ fechar(); }catch( Exception f ){}
				throw e;
			}
		}
		
		@Override
		public long skip( long n ) throws IOException {
			try{
				return is.skip( n );
			}catch( IOException e ){
				try{ fechar(); }catch( Exception f ){}
				throw e;
			}
		}
		
		@Override
		public boolean markSupported() {
			return is.markSupported();
		}
		
		@Override
		public synchronized void mark( int readlimit ) {
			is.mark( readlimit );
		}
		
		@Override
		public synchronized void reset() throws IOException {
			try{
				is.reset();
			}catch( IOException e ){
				try{ fechar(); }catch( Exception f ){}
				throw e;
			}
		}
		
		@Override
		public void close() throws IOException {
			try{
				is.close();
			}catch( IOException e ){
				try{ fechar(); }catch( Exception f ){}
				throw e;
			}
		}
		
	}
	
	private class OutputStreamImpl extends OutputStream {
		
		private OutputStream os;
		
		public OutputStreamImpl() throws IOException {
			this.os = socket.getOutputStream();
		}

		@Override
		public void write( int b ) throws IOException {
			try{
				os.write( b );
			}catch( IOException e ){
				try{ fechar(); }catch( Exception f ){}
				throw e;
			}
		}
		
		@Override
		public void write( byte[] b, int off, int len ) throws IOException {
			try{
				os.write( b, off, len );
			}catch( IOException e ){
				try{ fechar(); }catch( Exception f ){}
				throw e;
			}
		}
		
		@Override
		public void write( byte[] b ) throws IOException {
			try{
				os.write( b );
			}catch( IOException e ){
				try{ fechar(); }catch( Exception f ){}
				throw e;
			}
		}
		
		@Override
		public void flush() throws IOException {
			try{
				os.flush();
			}catch( IOException e ){
				try{ fechar(); }catch( Exception f ){}
				throw e;
			}
		}
		
		@Override
		public void close() throws IOException {
			try{
				os.close();
			}catch( IOException e ){
				try{ fechar(); }catch( Exception f ){}
				throw e;
			}
		}
		
	}
	
}
