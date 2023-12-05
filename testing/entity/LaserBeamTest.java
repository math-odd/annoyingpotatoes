package entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LaserBeamTest {
    LaserBeam laserBeam = new LaserBeam(0,0);


    @Test
    void getState() {
        assertEquals(laserBeam.getState(), LaserBeam.State.Warning);
    }
}