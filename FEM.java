package com.forthproject.something5.lesson5.myPractice;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class FEM  implements Area{      // extends Equation
    public double A;

    double t = 2;

    public double[][] B = new double[3][6];
    public double BT[][] = new double[6][3];
    //public double D[][] = new double[3][3];
    public double k[][] = new double[6][6];

    int all[][] = new int[6][11];

    int NE = 100;
    int ME[][] = new int[NE][3];

    int NP = 66;
    double K[][] = new double[2*NP][2*NP];

    int NPE = 3;

    public double factorb;
    public double factord;

    public double E = 6 *  pwr(10, 6);
    public double m = 0.25;

    public double Yi = 0;
    public double Yj = 0;
    public double Yk = 3;

    public double Xi = 0;
    public double Xj = 2;
    public double Xk = 1;

    public double bi;
    public double bj;
    public double bk;

    public double ci;
    public double cj;
    public double ck;

    @Override
    public double area(double[] XE, double[] YE) {

        // return (x * y) / 2;
        //double tpz =  3 * length / 2 * height;     // trapezoid area (3 triangle)
        //return tpz / 3;

        // x
        Xi = XE[0];
        Xj = XE[1];
        Xk = XE[2];
        // y
        Yi = YE[0];
        Yj = YE[1];
        Yk = YE[2];

        double ai = Xj*Yk - Xk*Yj;
        double aj = Xk*Yi - Yk*Xi;
        double ak = Xi*Yj - Xj*Yi;

        double area = (ai + aj + ak) / 2;

        System.out.println("area: " + area);
        return area;

    }

    public double pwr(double number, int row){
        double result = number;
        for(int i = 1; i < row; i++ ){
            result *= number;
        }
        return result;
    }

    private void stf(double[] XE, double[] YE, double E, double m, double t, int el){
        // count b
        bi = Yj - Yk;
        bj = Yk - Yi;
        bk = Yi - Yj;

        // count c
        ci = Xk - Xj;
        cj = Xi - Xk;
        ck = Xj - Xi;

        // output results
        System.out.println("bi: " + bi + "\n" +
                "bj: " + bj + "\n" +
                "bk: " + bk + "\n" +
                "ci: " + ci + "\n" +
                "cj: " + cj + "\n" +
                "ck: " + ck
                );

        A = area(XE, YE);

        // check area
        if (A <= 0){
            System.out.println("Ошибка. Площадь равна " + A + " [i] = " + el);
            System.exit(0);
        }

        factorb = 1 / ( 2*A );
        System.out.println("factor b: " + factorb);

        /*for (int i = 0; i < 6; i ++){
            if (i % 2 == 0 & i != 0){
                B[1][i] = 0;
            }if (i ==0 ){
                B[1][0] = 0;
            }if (i % 2 == 1){
                B[0][i] = 0;
            }
        }*/

        // first line
        B[0][0] = bi * factorb;
        B[0][2] = bj * factorb;
        B[0][4] = bk * factorb;

        // second line
        B[1][1] = ci * factorb;
        B[1][3] = cj * factorb;
        B[1][5] = ck * factorb;

        // third line
        B[2][0] = ci * factorb;
        B[2][1] = bi * factorb;
        B[2][2] = cj * factorb;
        B[2][3] = bj * factorb;
        B[2][4] = ck * factorb;
        B[2][5] = bk * factorb;

        factord = E / (1 - m * m);
        System.out.println("factor d: " + factord);

        // zeroing D
        double D[][] = new double[3][3];

        // first line
        D[0][0] = 1 * factord;
        D[0][1] = m  * factord;
        D[0][2] = 0  * factord;

        // second line
        D[1][0] = m  * factord;
        D[1][1] = 1  * factord;
        D[1][2] = 0  * factord;


        // third line
        D[2][0] = 0  * factord;
        D[2][1] = 0  * factord;
        D[2][2] = ( (double) 1 - m ) /2  * factord;

        double some[][] = new double[6][3];

        for (int i = 0; i < 6; i ++){
            for (int j = 0; j < 3; j++){
                BT[i][j] = B[j][i];
            }
        }

        // multiplication Bt and D
        for (int s = 0; s < 6; s++) {
            for (int j = 0; j < 3; j++) {    // 6
                double r = 0;
                for (int i = 0; i < 3; i++) {
                    //////////////////////////////////////// k[j][i] = B[j][i] * D[i][j] * t * A;
                    r += BT[s][i] * D[i][j]  ;       // B[i][j] k[i][j]
                }some[s][j] = r;
            }
        }

        // multiplication (Bt and D), B, t and A
        for (int s = 0; s < 6; s++) {
            for (int j = 0; j < 6; j++) {    // 6
                double r = 0;
                for (int i = 0; i < 3; i++) {
                    // k[j][i] = B[j][i] * D[i][j] * t * A;
                    r += some[s][i] * B[i][j];       // B[i][j] k[i][j]
                }k[s][j] = r * t * A ;
            }
        }

        /* // some (BT * D)
        System.out.println("some: ");
        for (int i = 0; i < 6; i++){
            for (int j = 0; j < 3; j++){
                System.out.println("[" + i + "]" + "[" + j + "]" + ": " + some[i][j] );
            }
        }
        // B
        System.out.println("B:");
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 6; j++){
                System.out.println("[" + i + "]" + "[" + j + "]" + ": " + B[i][j] );
            }
        }

        // K
        System.out.println("K: ");
        for (int i = 0; i < 6; i++){
            for (int j = 0; j < 6; j++){
                System.out.println("[" + i + "]" + "[" + j + "]" + ": " + k[i][j] );
            }
        }*/
    }


    private void fullall(){
        int k = 0;
        for (int i = 5; i > -1; i--){
            for (int j = 0; j < 11; j++, k++){
                all[i][j] = k;
            }
        }

        /*
        // output
        System.out.println("all: ");
        for (int i = 0; i < 6; i++){
            for (int j = 0; j < 11; j++){
                System.out.println("[" + i + "]" + "[" + j + "]" + ": " + all[i][j]);
            }
        }*/

        fullME();
    }


    private void fullME(){
        /*for (int m = 0; m < 100; m+=2){
            for (int k = 5; k > 0; k--){
                for (int el = 0; el < 10; el++){
                    // first part of triangle
                    ME[m][0] = all[k][el];
                    ME[m][1] = all[k][el + 1];
                    ME[m][2] = all[k - 1][el];
                    // second part of triangle
                    ME[m + 1][0] = all[k][el + 1];
                    ME[m+ 1][1] = all[k - 1][el + 1];
                    ME[m + 1][2] = all[k - 1][el];
                }
            }
        }*/

        String line = "";       // 1 11 0

        int count = 0;

        try( BufferedReader br =
                     new BufferedReader(new FileReader("C:\\Java\\FEM\\ME.txt")) ){

            while((line = br.readLine()) != null){
                System.out.println("line: " + line);

                // numbers
                char[] a = line.toCharArray();

                String n = "";
                String[] numbers = new String[3];


                for (int i = 0; i < 3; ){
                    for (int j = 0; j < a.length; j++){
                        //System.out.println(a[j]);
                        System.out.println("J: " + j);
                        char el = a[j];
                        System.out.println("el: " + el);

                        char ss  =' ';

                        if (el != ' ' & el != 'x'){
                            ss = '!';
                        }if (j == a.length - 1){
                            ss = 'j';
                        }

                        System.out.println("SS: " + ss);

                        switch (ss){
                            case '!':
                                n += el;
                                a[j] = 'x';
                                break;

                            case ' ':
                                System.out.println("num: " + numbers[0] + " " + numbers[1] + " " + numbers[2]);
                                numbers[i] = n;
                                i++;
                                n = "";
                                break;

                            case 'j':
                                n += el;
                                numbers[2] = n;
                                j = a.length;
                                i = 3;
                                break;
                        }
                    }
                }

                for (String el: numbers){
                    System.out.print("numbers: " + el + " ");
                }

                for (int i = 0; i < 3; i++){
                    try {
                        ME[count][i] = Integer.parseInt(numbers[i]);
                    }catch (java.lang.NumberFormatException exc){
                        System.out.println("ATTENTION: " + "<" + numbers[i] + ">");
                    }
                }


                count++;

            }

        }catch (IOException exc){
            System.out.println(exc);
        }

        // output
        System.out.println("\n");
        for(int i = 0; i < NE; i++){
            System.out.print(i + ": ");
            for (int j = 0; j < 3; j++){
                System.out.print(ME[i][j] + " ");
            }System.out.println();
        }

        // check correcting
        if (count < NE){
            System.out.println("Количество чисел в файле недостаточно");
        }if (count > NE){
            System.out.println("Количество чисел в файле слишком много");
        }if (count == NE){
            System.out.println("Количество чисел в норме");
        }


        // output
        System.out.println("ME: ");
        for (int i = 0; i < 100; i++){
            for (int j = 0; j < 3; j++){
                System.out.println("[" + i + "]" + "[" + j + "]" + ": " + ME[i][j]);
            }
        }

        //fullK();
        xy();
    }

    double x[] = new double[NE];
    double y[] = new double[NE];

    private void xy(){



        for (int i = 0; i < NE; i ++){
            if (i < 11){
                x[i] = 0;
            }if(i > 10 & i < 22){
                x[i] = 1;
            }if(i > 21 & i < 33){
                x[i] = 2;
            }if(i > 32 & i < 44){
                x[i] = 3;
            }if(i > 43 & i < 55){
                x[i] = 4;
            }if(i > 54 & i < 66){
                x[i] = 5;
            }
        }

        // y
        for (int i = 0; i < NE; i ++){
            if (i == 0 | i == 11 | i == 22 | i == 33 | i == 44| i == 55 ){
                y[i] = 10;
            }if(i == 1 | i == 12 | i == 23 | i == 34 | i == 45| i == 56 ){
                y[i] = 9;
            }if(i == 2 | i == 13 | i == 24 | i == 35 | i == 46| i == 57 ){
                y[i] = 8;
            }if(i == 3 | i == 14 | i == 25 | i == 36 | i == 47| i == 58 ){
                y[i] = 7;
            }if(i == 4 | i == 15 | i == 26 | i == 37 | i == 48| i == 59 ){
                y[i] = 6;
            }if(i == 5 | i == 16 | i == 27 | i == 38 | i == 49| i == 60 ){
                y[i] = 5;
            }if (i == 6 | i == 17 | i == 28 | i == 39 | i == 50| i == 61 ){
                y[i] = 4;
            }if(i == 7 | i == 18 | i == 29 | i == 40 | i == 51| i == 62 ){
                y[i] = 3;
            }if(i == 8 | i == 19 | i == 30 | i == 41 | i == 52| i == 63 ){
                y[i] = 2;
            }if(i == 9 | i == 20 | i == 31 | i == 42 | i == 53| i == 64 ){
                y[i] = 1;
            }if(i == 10 | i == 21 | i == 32 | i == 43 | i == 54| i == 65 ){
                y[i] = 0;
            }
        }

        fullK();
    }



    private void fullK(){
        gran();

        double XE[] = new double[3];
        double YE[] = new double[3];

        for (int i = 0; i < NE; i++) {        //NE
            // x

            XE[0] = x[ ME[i][0] ];
            XE[1] = x[ ME[i][1] ];
            XE[2] = x[ ME[i][2] ];
            // y
            YE[0] = y[ ME[i][0] ];
            YE[1] = y[ ME[i][1] ];
            YE[2] = y[ ME[i][2] ];


            /*XE[0] = 0;
            XE[1] = 2;
            XE[2] = 1;
            // y
            YE[0] = 0;
            YE[1] = 0;
            YE[2] = 3;*/

            stf(XE, YE, E, m, t, i );

            for (int ip = 0; ip < 3; ip++) {
                for (int id = 0; id < 2; id++) {
                    for (int jp = 0; jp < 3; jp++) {
                        for (int jd = 0; jd < 2; jd++) {
                            int il = ip * 2 + id;        // local
                            int jl = jp * 2 + jd;         // local

                            int ig = ME[i][ip] * 2 + id ;     // global (-1)
                            int jg = ME[i][jp] * 2 + jd ;     // global

                            K[ig][jg] = K[ig][jg] + k[il][jl];
                            //System.exit(0);

                            }
                        }
                    }
                }
            }



        System.out.println("K: ");
        for (int ii = 0; ii < 132; ii++){
            System.out.print(ii + " ");
            for (int j = 0; j < 132; j++){
                System.out.print(K[ii][j] + " ");
                //System.exit(0);
            }
            System.out.println();
        }

        fullZ();
    }


    public void gran(){
        for (int i = 0; i < 21; i++){
            K[i][i] = pwr(10, 20);
        }
    }


    double[] Z = new double[2 * NP];

    public void fullZ(){

        /*Z[110] = 100;
        Z[130] = 100;
        for (int i = 112; i < 129; i += 2){
            Z[i] = 200;
        }*/

        Z[121] = 200;

        countU();
    }

    public void countU(){
        System.out.println("###################################################################");
        /*
        double[][] A = { { 11, -6}, { -8, 5} };    //double[][] L;, {}
        double[][] L = new double[A.length][A[0].length];
        double[][] U = new double[A.length][A[0].length];
        double[] y = new double[A.length];
        double[] b = {2, 3};     // , 1
        double[] x = new double[A.length];*/

        Equation eq = new Equation();

        eq.A = K;
        eq.L = new double[K.length][K[0].length];
        eq.U = new double[K.length][K[0].length];
        eq.y = new double[K.length];
        eq.b = Z;
        eq.x = new double[K.length];

        eq.main();
    }

    public void main(){
        fullall();
        // "1 11 0".chars();
    }
}
