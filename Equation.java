package com.forthproject.something5.lesson5.myPractice;

import lesson13.LFore;

public class Equation {

    double[][] A = { { 11, -6, 8}, { -8, 5, 5}, { -190, 7, 236} };    //double[][] L;, {}
    double[][] L = new double[A.length][A[0].length];
    double[][] U = new double[A.length][A[0].length];
    double[] y = new double[A.length];
    double[] b = {2, 3, 8};     // , 1
    double[] x = new double[A.length];
    //double[][] U;


    public double countSum(int i, int j) {
        double sum = 0;

        for (int k = 0; k < i; k++) {       // i - 1
            //System.out.println("K: " + k);
            //System.out.println("I: " + i);
            sum += (L[i][k] * U[k][i]);
        }
        return sum;
    }

    public void OneOneOne() {
        for (int i = 0, j = 0; i < A.length; i++, j++) {
            U[i][j] = 1;
        }
    }

    public void main() {    // form matrix L and U // name - formLandU
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                for (int k = 0; k < 1; k++) {    // A[0].length * A.length
                    if (i >= j) {
                        //System.out.println("i: " + i + " , j: " + j + "   // Count L // ");
                        L[i][j] = A[i][j] - countSum(i, j);
                    } else {
                        //System.out.println("i: " + i + " , j: " + j + "   // Count U // ");
                        U[i][j] = (A[i][j] - countSum(i, j)) / L[i][i];
                    }
                }
            }
        }

        OneOneOne();    // one horizontally in U

        System.out.println("L: ");      // output L
        for (int i = 0; i < L.length; i++) {
            for (int j = 0; j < L[0].length; j++) {
                try {
                    System.out.println("[" + i + "]," + " [" + j + "]" + L[i][j]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("error");
                    break;
                }
            }
        }
        System.out.println("U: ");      // output U
        for (int i = 0; i < U.length; i++) {
            for (int j = 0; j < U[0].length; j++) {
                try {
                    System.out.println("[" + i + "]," + " [" + j + "]" + U[i][j]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("error");
                    break;
                }
            }
        }
        formY();    // form and output Y
        multiplication();   // print miltiplacation (check mistakes)
    }

    public double countSumY(int i) {
        double sum = 0;
        for (int k = 0; k < i; k++) {
            sum += (L[i][k] * y[k]);
        }
        return sum;
    }

    public void formY() {
        for (int i = 0; i < A.length; i++) {
            y[i] = ( b[i] - countSumY(i) ) / L[i][i];
        }

        System.out.println("Y: ");  // show Y
        for (double el : y) {
            System.out.println(el);
        }
        formX();
    }

    public double countSumX(int i){
        double sum = 0;
        for (int k = i + 1; k != A.length; k++ ){
            if ( k < A.length){
                sum += (U[i][k] * x[k]);
            }
        }
        return sum;
    }

    public void formX(){
        for (int i = A.length - 1; i > -1; i--){
            x[i] = y[i] - countSumX(i);
        }
        System.out.println("X: ");  // show x
        for (int i = 0; i < x.length; i+=2){
            System.out.println( i / 2 +  ": " + x[i] + "; " + x[i + 1]);
        }
    }

    public void multiplication(){
        double multiplac = 0;
        for (int i = 0; i < A.length ; i++){
            multiplac += (L[A.length - 1][i] * U[i][A.length - 1]);
        }
        System.out.println("Multiplacation equals: " + multiplac);
        checkAnswer();
    }

    public void checkAnswer(){
        double answer = 0;
        for (int i = 0; i < A.length ; i++){
            answer += A[0][i] * x[i];
        }
        System.out.println("//X// It's " + answer + " , and correct answer is " + b[0]);

        double xanswer = 0;
        for (int i = 0; i < A.length ; i++){
            xanswer += L[A.length - 1][i] * y[i];
        }
        System.out.println("//Y// It's " + xanswer + " , and correct answer is " + b[A.length - 1]);
    }
}
