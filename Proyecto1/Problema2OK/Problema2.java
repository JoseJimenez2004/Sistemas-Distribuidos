//Escriba un Programa que solicite los valores de a,b,c y d (como numeros enteros) y calcule el valor
// de la multiplicacion y lo demuestre en pantall en forma de numero con decimales y en forma de fraccion
// ejemplo: 17/33


import java.util.Scanner;
import java.text.DecimalFormat;
import java.math.BigInteger;

public class Problema2 {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el valor de a:");
        int a = scanner.nextInt();
        System.out.println("Ingrese el valor de b:");
        int b = scanner.nextInt();
        System.out.println("Ingrese el valor de c:");
        int c = scanner.nextInt();
        System.out.println("Ingrese el valor de d:");
        int d = scanner.nextInt();

        BigInteger numerador = BigInteger.valueOf(a).multiply(BigInteger.valueOf(c));
        BigInteger denominador = BigInteger.valueOf(b).multiply(BigInteger.valueOf(d));

        // Mostrar como decimal
        double resultadoDecimal = numerador.doubleValue() / denominador.doubleValue();
        DecimalFormat df = new DecimalFormat("#.######");
        System.out.println("Resultado decimal: " + df.format(resultadoDecimal));

        // Mostrar como fracción simplificada
        BigInteger gcd = numerador.gcd(denominador);
        BigInteger numSimplificado = numerador.divide(gcd);
        BigInteger denSimplificado = denominador.divide(gcd);
        System.out.println("Resultado fracción: " + numSimplificado + "/" + denSimplificado);

    }

}