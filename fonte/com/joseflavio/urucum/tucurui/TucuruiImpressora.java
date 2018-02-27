
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

package com.joseflavio.urucum.tucurui;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Impressora de {@link Tucurui}, ou seja, gerador de representa��o textual de {@link Tucurui}.
 * @author Jos� Fl�vio de Souza Dias J�nior
 */
public abstract class TucuruiImpressora {
	
	/**
	 * Imprime o documento {@link Tucurui}.
	 * @param tucurui {@link Tucurui} a ser impresso.
	 * @param saida Destino da representa��o textual.
	 * @param codificacao {@link Charset}
	 */
	public abstract void imprimir( Tucurui tucurui, OutputStream saida, String codificacao ) throws IOException, TucuruiException;
	
	/**
	 * {@link #imprimir(Tucurui, OutputStream, String)} com {@link Charset} "UTF-8".
	 */
	public final void imprimir( Tucurui tucurui, OutputStream saida ) throws IOException, TucuruiException {
		imprimir( tucurui, saida, "UTF-8" );
	}
	
}
