/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.unicafe;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author lassisav
 */
public class KassapaateTest {
    
    Kassapaate kassa;
    Maksukortti kortti;
    
    @Before
    public void setUp() {
        kassa = new Kassapaate();
    }
    @Test
    public void kassassaOikeaMaaraRahaa() {
        assertTrue(kassa.kassassaRahaa() == 100000);
    }
    @Test
    public void syoEdullisestiToimiiOikeinRiittavallaMaksulla() {
        int vaihto = kassa.syoEdullisesti(1000);
        assertTrue(kassa.kassassaRahaa() == 100240);
        assertTrue(760 == vaihto);
        assertTrue(kassa.edullisiaLounaitaMyyty() == 1);
    }
    @Test
    public void syoMaukkaastiToimiiOikeinRiittavallaMaksulla() {
        int vaihto = kassa.syoMaukkaasti(1000);
        assertTrue(kassa.kassassaRahaa() == 100400);
        assertTrue(600 == vaihto);
        assertTrue(kassa.maukkaitaLounaitaMyyty() == 1);
    }
    @Test
    public void syoEdullisestiToimiiOikeinLiianPienellaMaksulla() {
        int vaihto = kassa.syoEdullisesti(100);
        assertTrue(vaihto == 100);
        assertTrue(kassa.kassassaRahaa() == 100000);
        assertTrue(kassa.edullisiaLounaitaMyyty() == 0);
    }
    @Test
    public void syoMaukkaastiToimiiOikeinLiianPienellaMaksulla() {
        int vaihto = kassa.syoMaukkaasti(100);
        assertTrue(vaihto == 100);
        assertTrue(kassa.kassassaRahaa() == 100000);
        assertTrue(kassa.maukkaitaLounaitaMyyty() == 0);
    }
    @Test
    public void syoEdullisestiToimiiOikeinRiittavallaSaldolla() {
        kortti = new Maksukortti(1000);
        assertTrue(kassa.syoEdullisesti(kortti));
        assertTrue(kortti.saldo() == 760);
        assertTrue(kassa.edullisiaLounaitaMyyty() == 1);
    }
    @Test
    public void syoMaukkaastiToimiiOikeinRiittavallaSaldolla() {
        kortti = new Maksukortti(1000);
        assertTrue(kassa.syoMaukkaasti(kortti));
        assertTrue(kortti.saldo() == 600);
        assertTrue(kassa.maukkaitaLounaitaMyyty() == 1);
    }
    @Test
    public void liianPientaMaksuaEiOtetaVastaan() {
        kortti = new Maksukortti(7);
        assertFalse(kassa.syoEdullisesti(kortti));
        assertFalse(kassa.syoMaukkaasti(kortti));
        assertTrue(kortti.saldo() == 7);
        assertTrue(kassa.maukkaitaLounaitaMyyty() == 0 && kassa.edullisiaLounaitaMyyty() == 0);
    }
    @Test
    public void kassanRahamaaraEiMuutuKortillaOstaessa(){
        kortti = new Maksukortti(1000);
        kassa.syoEdullisesti(kortti);
        kassa.syoMaukkaasti(kortti);
        assertTrue(kassa.kassassaRahaa() == 100000);
    }
    @Test
    public void rahanLatausKortilleToimiiOikein(){
        kortti = new Maksukortti(0);
        kassa.lataaRahaaKortille(kortti, 1000);
        assertTrue(kassa.kassassaRahaa() == 101000 && kortti.saldo() == 1000);
    }
    @Test
    public void korttiEiLataaNegatiivistaSummaa() {
        kortti = new Maksukortti(1000);
        kassa.lataaRahaaKortille(kortti, -100);
        assertTrue(kortti.saldo() == 1000);
    }
}
