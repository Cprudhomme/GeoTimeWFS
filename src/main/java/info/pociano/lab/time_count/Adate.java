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

import java.util.*;

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

        Date d1 = new Date();
        Date d2 = new Date(); // Current date 
        Date d3 = new Date();

        boolean a = d3.after(d1);

        System.out.println(
                "Date d3 comes after" + "Date d2: " + a);

        boolean b = d3.before(d2);

        System.out.println(
                "Date d3 comes before" + "Date d2: " + b);

        int c = d1.compareTo(d2);

        System.out.println(c);

        System.out.println("Miliseconds from jan1" + "1970 tp date d1 is" + d1.getTime());

        System.out.println("Before settings" + d2);
        d2.setTime(
                204587433443L);
        System.out.println(
                "After setting" + d2);
    }
    final String input;

    public Adate(String input) {
        this.input = input;

        System.currentTimeMillis();
    }

    public abstract int getSecondes();
}
