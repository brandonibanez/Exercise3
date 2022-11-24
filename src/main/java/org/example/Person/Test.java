package org.example.Person;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Test {

    private static boolean checkPrime(int num) {
        if(num == 0 || num == 1)
            return true;
        else {
            for(int i=2; i<=num; i++)
                if(num % i == 0 && num != i)
                    return false;
        }
        return true;
    }

    private static void findMinAndMaxOfPrimes(List<Integer> list) {
        list = list.stream().sorted().collect(Collectors.toList());
        int min=list.get(0), max=0, temp;
        for(int num: list) {
            if(checkPrime(num)) {
                if(num < min)
                    min = num;
                if(num > max)
                    max = num;
            }
        }
        System.out.println("Min: " + min + "\tMax: " + max);
        checkPrimesBetweenTwoNumbers(min, max);
    }

    private static void checkPrimesBetweenTwoNumbers(int min, int max) {
        for(int i=min; i<=max; i++) {
            for(int j=min; j<=i; j++)
                if(i == 0 || i == 1) {
                    System.out.println(i + " is prime!");
                    break;
                }
                else{
                    if(i % j == 0 && j != i) {
                        System.out.println(i + " is not prime!");
                        break;
                    }
                    else if(i == j)
                        System.out.println(i + " is prime!");
                }
        }
    }

    private static void fizzBuzz() {
        int num = 100;
        for(int i=1; i<=num; i++) {
            if(i % 3 == 0)
                System.out.print("fizz, ");
            else if(i % 5 == 0)
                System.out.print("buzz, ");
            else
                System.out.print(i+", ");
        }
    }

    public static void main(String[] args) {
        Integer[] numbers = {5,4,3,2,6,7,11,-1};
        List<Integer> list = List.of(5,4,3,2,6,7,11,-1);
        findMinAndMaxOfPrimes(Arrays.asList(numbers));
    }

}
