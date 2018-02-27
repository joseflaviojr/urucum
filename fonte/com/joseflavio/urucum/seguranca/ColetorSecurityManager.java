
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

package com.joseflavio.urucum.seguranca;

import java.io.Writer;
import java.security.Permission;
import java.security.Policy;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * {@link SecurityManager} que coleta todas as {@link Permission}s
 * {@link SecurityManager#checkPermission(Permission) exigidas} pela aplica��o.<br>
 * Sua finalidade consiste em descobrir as {@link Permission}s envolvidas e, com base nelas,
 * auxiliar a especifica��o da {@link Policy}.<br>
 * Exemplo de uso:<br>
 * <pre>
 * {@link System#setSecurityManager(SecurityManager) System.setSecurityManager}(new {@link ColetorSecurityManager}());
 * ...
 * {@link SecurityManagerUtil#imprimir(java.util.Collection, Writer, boolean, String, String) SecurityManagerUtil.imprimir}(
 *     (({@link ColetorSecurityManager}){@link System#getSecurityManager()}).{@link #getColeta()},
 *     true
 * );
 * </pre>
 * @author Jos� Fl�vio de Souza Dias J�nior
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
