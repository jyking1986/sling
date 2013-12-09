package org.test;

import org.test2.Helper;

/**
 * Created with IntelliJ IDEA.
 * User: ethan.wang
 * Date: 12/7/13
 * Time: 3:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class Service {
    public static String echo(String input) {
        return String.valueOf(Calculator.add(1, 2)) + input;
    }

    public static String echo2(String input){
       return Helper.answer(input);
    }
}
