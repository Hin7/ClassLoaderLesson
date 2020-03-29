package ru.sbt.course.Plugins;
/**
 * @author Hin7
 */

public class Print123Plugin implements Plugin{
    @Override
    public void doUsefull() {
        System.out.println("************************");
        System.out.println("Jist 1, 2, 3 From Plugin");
        //System.out.println("Loaded from file");
        //System.out.println("Loaded from !crypted! file");
        System.out.println("************************");
    }
}
