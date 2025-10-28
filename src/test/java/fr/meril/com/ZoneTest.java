package fr.meril.com;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ZoneTest {

    @Test
    void zoneParDefaut(){
        Zone c = new Zone(); assertEquals(TypeZone.PLAINE, c.type);
        assertEquals(0,
                c.nbTresors); }
}
