/*
 * Copyright (C) 2021 jean-jacquesponciano.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package info.pociano.lab.time_count;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author jean-jacquesponciano
 */
public class TimeTest {

    public TimeTest() {
    }

    /**
     * Test of main method, of class Time.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        Time.main(args);
        String now = Time.now();// ask the date 
        int v = Time.daysBetween(now, now);
        assertEquals(0, v);// check that the value is the value expected

        v = Time.monthsBetween("21-11-2021", "20-12-2021");
        assertEquals(1, v);
        
         v = Time.yearsBetween("21-12-2020", "20-12-2021");
        assertEquals(1, v);
        
        
         v = Time.weeksBetween("21-12-2021", "29-12-2021");
        assertEquals(1, v);
        
        
         v = Time.daysBetween("21-12-2021", "20-12-2021");
        assertEquals(1, v);
    }

}
