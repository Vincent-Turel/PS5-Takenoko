package dev.stonks.takenoko;

public class Main {


    public static String hello() {
        return "Hello World!";
    }

    public static void main(String... args) {
        System.out.println(hello());


        ObjectivesMaker test = new ObjectivesMaker();

        test.addAnObjectives(1,2,5);
        test.addAnObjectives(1,8,7);
        test.addAnObjectives(2,8,7);

        }


}
