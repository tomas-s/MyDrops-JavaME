package com.example.tomas.mydrops;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by tomas on 5/5/17.
 */
public class ShowDropsTest {
    @Before
    public void setUp() throws Exception {


    }

    @Test
    public void plus() throws Exception {
        ShowDrops showDrops = new ShowDrops();

        int result = showDrops.plus(5,3);
        assertEquals(8,result);

    }

}