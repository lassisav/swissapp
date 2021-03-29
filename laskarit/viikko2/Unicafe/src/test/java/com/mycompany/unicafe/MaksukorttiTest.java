package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MaksukorttiTest {

    Maksukortti kortti;

    @Before
    public void setUp() {
        kortti = new Maksukortti(10);
    }

    @Test
    public void luotuKorttiOlemassa() {
        assertTrue(kortti!=null);      
    }
    @Test
    public void kortinSaldoAlussaOikein() {
        assertEquals(10, kortti.saldo());
    }
    @Test
    public void lataaRahaaToimiiOikein() {
        kortti.lataaRahaa(100);
        assertEquals("saldo: 1.10", kortti.toString());
    }
    @Test
    public void saldoVaheneeOikein() {
        kortti.otaRahaa(7);
        assertEquals(3, kortti.saldo());
    }
    @Test
    public void saldoEiVaheneYlisummalla() {
        kortti.otaRahaa(100);
        assertEquals(10, kortti.saldo());
    }
    @Test
    public void otaRahaaPalauttaaTruenOnnistuessaan() {
        assertTrue(kortti.otaRahaa(7));
    }
    @Test
    public void otaRahaaPalauttaaFalsenEpaonnistuessaan() {
        assertFalse(kortti.otaRahaa(70));
    }
}
