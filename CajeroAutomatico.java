import java.io.*;
import java.util.Scanner;

public class CajeroAutomatico {
    private static final String SALDO_ARCHIVO = "./saldo.dat"; 

    public static void main(String[] args) {
        resetearSaldoInicial();

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            limpiarConsola();
            mostrarMenu();
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    consultarSaldo();
                    pausa();
                    break;
                case 2:
                    System.out.print("Ingrese la cantidad a retirar: ");
                    double cantidad = scanner.nextDouble();
                    retirarDinero(cantidad);
                    pausa();
                    break;
                case 3:
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
                    pausa();
            }
        } while (opcion != 3);

        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n=== Cajero Automático ===");
        System.out.println("1. Consultar saldo");
        System.out.println("2. Retirar dinero");
        System.out.println("3. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private static void resetearSaldoInicial() {
        System.out.println("Restableciendo saldo a Q1000...");
        actualizarSaldo(1000.00);
    }
    private static void actualizarSaldo(double nuevoSaldo) {
        try (DataOutputStream salida = new DataOutputStream(new FileOutputStream(SALDO_ARCHIVO))) {
            salida.writeDouble(nuevoSaldo);
        } catch (IOException e) {
            System.out.println("Error al actualizar el saldo: " + e.getMessage());
        }
    }
    

    private static double leerSaldo() {
        try (DataInputStream entrada = new DataInputStream(new FileInputStream(SALDO_ARCHIVO))) {
            return entrada.readDouble();
        } catch (IOException e) {
            System.out.println("Error al leer el saldo. Puede que el archivo no exista aún.");
            return 0.0;
        }
    }

    private static void consultarSaldo() {
        double saldo = leerSaldo();
        System.out.printf("Saldo disponible: Q%.2f\n", saldo);
    }

    private static void retirarDinero(double cantidad) {
        double saldo = leerSaldo();

        if (cantidad <= 0) {
            System.out.println("Ingrese una cantidad válida.");
            return;
        }

        if (cantidad > saldo) {
            System.out.println("Saldo insuficiente.");
        } else {
            saldo -= cantidad;
            actualizarSaldo(saldo);
            System.out.printf("Retiro exitoso. Nuevo saldo: Q%.2f\n", saldo);
        }
    }
    private static void limpiarConsola() {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.println("Error al limpiar la consola: " + e.getMessage());
        }
    }

    private static void pausa() {
        System.out.println("\nPresione Enter para continuar...");
        try {
            System.in.read();
        } catch (IOException e) {
        }
    }
}
