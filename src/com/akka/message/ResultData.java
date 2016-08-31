package com.akka.message;

/**
 * Description: ResultData to hold the results
 *
 * @version: 1.0
 * @author: sthitaprajna
 * 
 */

import java.util.HashMap;
import java.util.Map;


public class ResultData
{
    private Map<String,Integer> results = new HashMap<String,Integer>();

    public ResultData()
    {
    }

    public Map<String,Integer> getResults()
    {
        return results;
    }
}