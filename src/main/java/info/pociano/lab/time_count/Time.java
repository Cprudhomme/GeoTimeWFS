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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Formatter;

/**
 *
 * @author jean-jacques ponciano
 */
public class Time {

    /**
     *
     * @param args
     */
    

    public static void main(String[] args) {
        String now = Time.now();
        System.out.println(now);
        int days = Time.daysBetween("31-12-2021", "12-12-2012");
        System.out.println(days);
        int months = Time.monthsBetween("31-12-2021", "12-12-2012");
        System.out.println(months);
        int years = Time.yearsBetween("31-12-2021", "12-12-2012");
        System.out.println(years);
         int weeks = Time.weeksBetween("31-12-2021", "12-12-2012");
        System.out.println(weeks);
    }

    public static int daysBetween (String d1, String d2)  {
            DateTimeFormatter Formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu");
           
       
        
        
        LocalDate ld1  = LocalDate.parse(d1, Formatter);
         LocalDate ld2 = LocalDate.parse(d2, Formatter);
         
          long daysBetween = ChronoUnit.DAYS.between(ld1,ld2);
         
         System.out.println(daysBetween);
        return (int) daysBetween;
              
    }
     
    public static int yearsBetween(String d1, String d2) {
        DateTimeFormatter Formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu");
    
        
        LocalDate ld1 = LocalDate.parse(d1, Formatter);
         LocalDate ld2 = LocalDate.parse(d2, Formatter);
         
         long yearsBetween = ChronoUnit.YEARS.between(ld1,ld2);
         
         System.out.println(yearsBetween);
        return  (int) yearsBetween;
                
     
    }

    public static int monthsBetween(String d1, String d2) {
         DateTimeFormatter Formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu");
    
       
        LocalDate ld1 = LocalDate.parse(d1, Formatter);
         LocalDate ld2 = LocalDate.parse(d2, Formatter);
         
         long monthsBetween = ChronoUnit.MONTHS.between(ld1,ld2);
         
         System.out.println(monthsBetween);
        return (int) monthsBetween;
                
    }

    public static String now() {
     
    }

    public static int weeksBetween(String d1, String d2) {
        DateTimeFormatter Formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu");
        
        LocalDate ld1 = LocalDate.parse(d1, Formatter);
          LocalDate ld2 = LocalDate.parse(d2, Formatter);
          
          long weeksBetween = ChronoUnit.WEEKS.between(ld1,ld2);
          
          System.out.println(weeksBetween);
        return (int) weeksBetween ;
    }
}
