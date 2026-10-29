package main;

import controlador.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    private final static String MENSAJE_ERROR = "ERROR! VALOR NO VÁLIDO"; // constante para evitar duplicar los sysos de
    // error

    private static Ejercicio1 ej1;
    private static Ejercicio2 ej2;
    private static Ejercicio3 ej3;
    public static void main(String[] args) {
        

    }

    public static void menuEjercicios(Scanner sc) {
        boolean salir = false;

        do {
            try {
                System.out.println("<====GESTOR EJERCICIOS=====>");
                System.out.println("<=====1) Ejercicio1===========>");
                System.out.println("<=====2).Ejercicio2===========>");
                System.out.println("<=====3) Ejercicio3===========>");
                System.out.println("<=====4) Ejercicio4===========>");
                System.out.println("<=====5) Salir================>");
                int opcion;
                try {
                    opcion = Integer.parseInt(sc.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.err.println(MENSAJE_ERROR);
                    opcion=0;
                }

                switch (opcion) {
                    case 1:
                        System.out.println("");
                        break;
                    case 2:
                        System.out.println("");
                        break;
                    case 3:
                        System.out.println("");
                        break;
                    case 4:
                        System.out.println("");
                        break;
                    case 5:
                        System.out.println("Saliendo del programa...");
                        salir = true;
                        break;
                    default:
                        System.err.println("Opción no disponible..");
                        break;
                }
            } catch (NoSuchElementException e) {
                System.err.println("Error! has intentado acceder al final del archivo");
                salir = true;
            }
        } while (!salir);

    }
}