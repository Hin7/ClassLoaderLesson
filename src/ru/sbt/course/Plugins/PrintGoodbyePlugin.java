package ru.sbt.course.Plugins;
/**
 * @author Hin7
 */


public class PrintGoodbyePlugin implements Plugin{
    @Override
    public void doUsefull() {
        System.out.println("======================");
        System.out.println("Goodbye!!! From Plugin");
        //System.out.println("Loaded from file");
        //System.out.println("Loaded from !crypted! file");
        System.out.println("======================");
    }
}
