
/*
 *  Copyright (C) 2016-2020 José Flávio de Souza Dias Júnior
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
 *  Direitos Autorais Reservados (C) 2016-2020 José Flávio de Souza Dias Júnior
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

package com.joseflavio.urucum.seguranca;

import java.security.AllPermission;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Policy;
import java.security.ProtectionDomain;
import java.security.SecurityPermission;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link Policy} simplificada, a qual não depende de
 * arquivos externos (<code>sun.security.provider.PolicyFile</code>).
 * @author José Flávio de Souza Dias Júnior
 * @see #adicionar(Permission)
 */
public final class SimplesPolicy extends Policy {

	private Map<Class<?>,List<Permission>> permissoes;
	
	private boolean permissaoTotal = false;
	
	public SimplesPolicy() {
		this.permissoes = Collections.synchronizedMap( new HashMap<Class<?>,List<Permission>>() );
	}
	
	/**
	 * Adiciona uma {@link Permission}.<br>
	 * Exige-se <code>SecurityPermission("setPolicy")</code>.
	 * @throws SecurityException caso {@link Policy#setPolicy(Policy)} protegida.
	 */
	public void adicionar( Permission permission ) throws SecurityException {
		
		SecurityManager sm = System.getSecurityManager();
		if( sm != null ) sm.checkPermission( new SecurityPermission( "setPolicy" ) );
		
		Class<?> classe = permission.getClass();
		List<Permission> lista = permissoes.get( classe );
		if( lista == null ) permissoes.put( classe, lista = new ArrayList<Permission>() );
		lista.add( permission );
		
		if( classe == AllPermission.class ) permissaoTotal = true;
		
	}
	
	@Override
	public PermissionCollection getPermissions( CodeSource codesource ) {
		return new PermissionCollectionImpl();
	}
	
	@Override
	public boolean implies( ProtectionDomain domain, Permission permission ) {
		
		if( permissaoTotal ) return true;
			
		Class<?> classe = permission.getClass();
		List<Permission> lista = permissoes.get( classe );
		
		if( lista != null ){
			for( Permission p : lista ){
				if( p.implies( permission ) ) return true;
			}
		}
		
		return false;
		
	}
	
	@Override
	public void refresh() {
	}
	
	private class PermissionCollectionImpl extends PermissionCollection {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void add( Permission permission ) {
			if( isReadOnly() ) throw new SecurityException( "Apenas leitura." );
			adicionar( permission );
		}
		
		@Override
		public Enumeration<Permission> elements() {
			List<Permission> todas = new ArrayList<Permission>();
			for( List<Permission> lista : permissoes.values() ){
				todas.addAll( lista );
			}
			return Collections.enumeration( todas );
		}
		
		@Override
		public boolean implies( Permission permission ) {
			return SimplesPolicy.this.implies( null, permission );
		}
		
	}
	
}
