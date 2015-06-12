/*
 * Copyright (c) 2015 General Electric Company. All rights reserved.
 * 
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
package com.ge.corporate.piwik.tracking;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A datetime object that will return the datetime in the format {@code yyyy-MM-dd hh:mm:ss }.
 * 
 * @author brettcsorba
 */
public class PiwikDate extends Date{
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    
    /**
     * Allocates a Date object and initializes it so that it represents the time
     * at which it was allocated, measured to the nearest millisecond.
     */
    public PiwikDate(){
        super();
    }

    /**
     * Allocates a Date object and initializes it to represent the specified number
     * of milliseconds since the standard base time known as "the epoch", namely
     * January 1, 1970, 00:00:00 GMT.
     * @param date the milliseconds since January 1, 1970, 00:00:00 GMT.
     */
    public PiwikDate(long date){
        super(date);
    }
    /**
     * Converts this PiwikDate object to a String of the form:<br>
     * <br>
     * {@code yyyy-MM-dd hh:mm:ss }.
     * @return a string representation of this PiwikDate
     */
    @Override
    public String toString(){
        return format.format(this);
    }
}
