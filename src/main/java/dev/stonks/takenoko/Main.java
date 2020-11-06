package dev.stonks.takenoko;

public class Main {


    public static String hello() {
        return "Hello World!";
    }

    public static void main(String... args) {
        System.out.println(hello());


        ObjectivesMaker test = new ObjectivesMaker();

        Objectives a1 = test.addAnObjectivies(1,2,5);
        Objectives a2 = test.addAnObjectivies(1,8,7);
        Objectives a3 = test.addAnObjectivies(2,8,7);

        System.out.println(a3);}


}
