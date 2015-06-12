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

import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonValue;

/**
 * Object representing a JSON array required by some Piwik query parameters.
 * 
 * @author brettcsorba
 */
public class PiwikJsonArray{
    List<JsonValue> list = new ArrayList<>();
    
    /**
     * Get the value stored at the specified index.
     * @param index the index of the value to return
     * @return the value stored at the specified index
     */
    public JsonValue get(int index){
        return list.get(index);
    }
    
    /**
     * Add a value to the end of this array.
     * @param value value to add to the end of the array
     */
    public void add(JsonValue value){
        list.add(value);
    }
    
    /**
     * Set the value at the specified index to the specified value.
     * @param index the index of the value to set
     * @param value the value to set at the specified index
     */
    public void set(int index, JsonValue value){
        list.set(index, value);
    }
    
    /**
     * Returns a JSON encoded array string representing this object.
     * @return returns the current array as a JSON encode string
     */
    @Override
    public String toString(){
        JsonArrayBuilder ab = Json.createArrayBuilder();
        
        for (int x = 0; x < list.size(); ++x){
            ab.add(list.get(x));
        }
        
        return ab.build().toString();
    }
}
