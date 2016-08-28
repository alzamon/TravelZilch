package com.example.asgeir.travel10k;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

public class GameTest {
    @Test
    public void testCalculatePoints(){
        Map<Integer, Integer> pointsCounter = new HashMap<>();
        pointsCounter.put(1, 3);
        pointsCounter.put(2, 0);
        pointsCounter.put(3, 0);
        pointsCounter.put(4, 0);
        pointsCounter.put(5, 2);
        pointsCounter.put(6, 1);
        assertEquals(1100, new DicePool().calculatePoints(pointsCounter));
    }

}