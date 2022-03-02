package com.joseflavio.urucum.texto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.PropertyResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StringUtilTest {

    private final PropertyResourceBundle resourceBundle;

    StringUtilTest() {
        resourceBundle = mock(PropertyResourceBundle.class);
        when(resourceBundle.handleGetObject("chave0")).thenReturn("valor");
        when(resourceBundle.handleGetObject("chave1")).thenReturn("Mensagem {0} {1,choice,0#aaa|1#bbb}.");
        when(resourceBundle.handleGetObject("chave2")).thenReturn("ccc");
    }

    @Test
    void formatarMensagem() {
        assertEquals("Mensagem texto aaa.", StringUtil.formatarMensagem(resourceBundle, "$chave1", "texto", 0));
        assertEquals("Mensagem ccc bbb.", StringUtil.formatarMensagem(resourceBundle, "$chave1", "$chave2", 1));
        assertEquals("Mensagem chave2 bbb.", StringUtil.formatarMensagem(resourceBundle, "$chave1", "chave2", 1));
        assertEquals("chave1", StringUtil.formatarMensagem(resourceBundle, "chave1", "$chave2", 1));
        assertEquals("chave1", StringUtil.formatarMensagem(null, "$chave1"));
        assertEquals("chave1", StringUtil.formatarMensagem(null, "chave1"));
        assertEquals("", StringUtil.formatarMensagem(null, null));
        assertEquals("", StringUtil.formatarMensagem(resourceBundle, null));
    }

    @Test
    void formatar() {
        assertEquals("Mensagem texto aaa.", StringUtil.formatar(resourceBundle, "chave1", "texto", 0));
        assertEquals("Mensagem ccc bbb.", StringUtil.formatar(resourceBundle, "chave1", "chave2", 1));
        assertEquals("Mensagem ccc bbb.", StringUtil.formatar(resourceBundle, "chave1", "$chave2", 1));
        assertEquals("Mensagem ccc bbb.", StringUtil.formatar(resourceBundle, "$chave1", "$chave2", 1));
        assertEquals("$chave1", StringUtil.formatar(null, "$chave1"));
        assertEquals("chave1", StringUtil.formatar(null, "chave1"));
        assertEquals("", StringUtil.formatar(null, null));
        assertEquals("", StringUtil.formatar(resourceBundle, null));
    }

    @Test
    void getMensagem() {
        assertEquals("valor", StringUtil.getMensagem(resourceBundle, "chave0"));
        assertEquals("valor", StringUtil.getMensagem(resourceBundle, "$chave0"));
        assertEquals("chave.desconhecida", StringUtil.getMensagem(resourceBundle, "chave.desconhecida"));
        assertEquals("$chave0", StringUtil.getMensagem(null, "$chave0"));
        assertNull(StringUtil.getMensagem(resourceBundle, null));
        assertNull(StringUtil.getMensagem(null, null));
    }

    @Test
    void tamanho() {
        assertEquals(0, StringUtil.tamanho(null));
        assertEquals(0, StringUtil.tamanho(""));
        assertEquals(3, StringUtil.tamanho("abc"));
    }

}
