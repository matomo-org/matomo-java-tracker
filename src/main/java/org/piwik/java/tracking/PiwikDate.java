/* 
 * Piwik Java Tracker
 * 
 * @link https://github.com/piwik/piwik-java-tracker
 * @license https://github.com/piwik/piwik-java-tracker/blob/master/LICENSE BSD-3 Clause
 */
package org.piwik.java.tracking;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * A datetime object that will return the datetime in the format {@code yyyy-MM-dd hh:mm:ss}.
 * 
 * @author brettcsorba
 */
public class PiwikDate extends Date{
    SimpleDateFormat format;
    
    /**
     * Allocates a Date object and initializes it so that it represents the time
     * at which it was allocated, measured to the nearest millisecond.
     */
    public PiwikDate(){
        super();
        format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    /**
     * Allocates a Date object and initializes it to represent the specified number
     * of milliseconds since the standard base time known as "the epoch", namely
     * January 1, 1970, 00:00:00 GMT.
     * @param date the milliseconds since January 1, 1970, 00:00:00 GMT.
     */
    public PiwikDate(long date){
        super(date);
        format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
    }
    
    /**
     * Sets the time zone of the String that will be returned by {@link #toString()}.
     * Defaults to UTC.
     * @param zone the TimeZone to set
     */
    public void setTimeZone(TimeZone zone){
        format.setTimeZone(zone);
    }
    
    /**
     * Converts this PiwikDate object to a String of the form:<br>
     * <br>
     * {@code yyyy-MM-dd hh:mm:ss}.
     * @return a string representation of this PiwikDate
     */
    @Override
    public String toString(){
        return format.format(this);
    }
}
