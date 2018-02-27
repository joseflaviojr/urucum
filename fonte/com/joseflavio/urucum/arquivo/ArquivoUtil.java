
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

package com.joseflavio.urucum.arquivo;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;

/**
 * Utilit�rios para {@link File}s.
 * @author Jos� Fl�vio de Souza Dias J�nior
 */
public class ArquivoUtil {
    
    private static final MimetypesFileTypeMap mimetypes = new MimetypesFileTypeMap();
    
    /**
     * https://www.iana.org/assignments/media-types/media-types.xhtml
     * @see MimetypesFileTypeMap#getContentType(String)
     */
    public static String getTipo( String arquivo ) {
        return mimetypes.getContentType( arquivo );
    }
    
    /**
     * https://www.iana.org/assignments/media-types/media-types.xhtml
     * @see MimetypesFileTypeMap#getContentType(File)
     */
    public static String getTipo( File arquivo ) {
        return mimetypes.getContentType( arquivo );
    }
    
}
