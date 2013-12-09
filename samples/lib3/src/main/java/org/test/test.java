package org.test;


/**
 * Created with IntelliJ IDEA.
 * User: ethan.wang
 * Date: 12/7/13
 * Time: 3:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class test {
    public static String exec(String cmd) {
        return org.test.Service.echo(cmd + " executing");
    }
    public static String answer(String what){
        return Service.echo2(what)+" answered";
    }
}
