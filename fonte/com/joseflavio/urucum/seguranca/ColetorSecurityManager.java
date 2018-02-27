
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

package com.joseflavio.urucum.seguranca;

import java.io.Writer;
import java.security.Permission;
import java.security.Policy;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * {@link SecurityManager} que coleta todas as {@link Permission}s
 * {@link SecurityManager#checkPermission(Permission) exigidas} pela aplicação.<br>
 * Sua finalidade consiste em descobrir as {@link Permission}s envolvidas e, com base nelas,
 * auxiliar a especificação da {@link Policy}.<br>
 * Exemplo de uso:<br>
 * <pre>
 * {@link System#setSecurityManager(SecurityManager) System.setSecurityManager}(new {@link ColetorSecurityManager}());
 * ...
 * {@link SecurityManagerUtil#imprimir(java.util.Collection, Writer, boolean, String, String) SecurityManagerUtil.imprimir}(
 *     (({@link ColetorSecurityManager}){@link System#getSecurityManager()}).{@link #getColeta()},
 *     true
 * );
 * </pre>
 * @author José Flávio de Souza Dias Júnior
 */
public final class ColetorSecurityManager extends SecurityManager {
	
	private Set<Permission> coleta = Collections.synchronizedSet( new HashSet<Permission>( 100 ) );
	
	@Override
	public void checkPermission( Permission perm ) {
		coleta.add( perm );
	}
	
	@Override
	public void checkPermission( Permission perm, Object context ) {
		coleta.add( perm );
	}
	
	public Set<Permission> getColeta() {
		return coleta;
	}
	
}
