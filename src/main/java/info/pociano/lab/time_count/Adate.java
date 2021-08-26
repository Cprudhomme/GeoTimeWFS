/*
 * Copyright (C) 2021 pc-asus.
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

/**
 *
 * @author pc-asus
 */
public abstract class Adate {
    int secondes;
    int minutes;
    int hours;
    int days;
    int weeks;
    int months;
    int years; {
    
  
   1 secondes = 1000 millisecondes;
   1 minutes = 60 secondes;
   1 hours = 60 minutes;
   1 days  = 24 hours;
   1 weeks = 7 days;
   1 months = 4 weeks;
   1years = 12 months;  
}
       final String input;   

    public Adate(String input) {
        this.input = input;
        
        System.currentTimeMillis();
    }

    public abstract int getSecondes(); 
      
    
    
}
