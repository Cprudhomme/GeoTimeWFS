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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
    int years;

    public void inwork() {
        System.out.println(input);
        //Defines each variable in a unit of one e.g 1 minute is equivalent to 60 secondes
        secondes = 1000;
        minutes = 60 * secondes;
        hours = 60 * minutes;
        days = 24 * hours;
        weeks = 7 * days;
        months = 4 * weeks;
        years = 12 * months;

       DateTimeFormatter Formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu");
        
        String firstDate = "31/08/2021";
        
        String secondDate = "01/10/2023";
        
        System.out.println(Integer.parseInt(firstDate));
        
        System.out.println(Integer.parseInt(secondDate));
       
        LocalDate date1 = LocalDate.parse(firstDate, Formatter);
         
        LocalDate date2 = LocalDate.parse(secondDate, Formatter);
    }
    final String input;

    public Adate(String input) {
        this.input = input;

        System.currentTimeMillis();
    }

    public abstract int getSecondes();
    public abstract int getMinutes();
    public abstract int getHours();
    public abstract int getDays();
    public abstract int getWeeks();
    public abstract int getMonths();
    public abstract int getYears();
}
