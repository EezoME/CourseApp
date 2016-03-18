package com.eezo;

import java.util.Arrays;
import java.util.StringTokenizer;

/**
 *
 * Created by Eezo on 13.03.2016.
 */
public class FuzzyMatrix {
    private TriangularNumber[] matrix;
    private int rowsNumber;
    private int colsNumber;

    public FuzzyMatrix(TriangularNumber[] matrix, int rowsNumber, int colsNumber) {
        this.matrix = matrix;
        this.rowsNumber = rowsNumber;
        this.colsNumber = colsNumber;
    }

    public static class TriangularNumber {
        private int a1;
        private int a0;
        private int a2;

        public TriangularNumber(){
            this.a1 = 0;
            this.a0 = 0;
            this.a2 = 0;
        }

        public TriangularNumber(int a1, int a0, int a2) {
            this.a1 = a1;
            this.a0 = a0;
            this.a2 = a2;
        }

        public TriangularNumber(String data){
            if (data == null || data.isEmpty()){
                return;
            }
            StringTokenizer st = new StringTokenizer(data, ",");
            this.a1 = Integer.parseInt(st.nextToken());
            this.a0 = Integer.parseInt(st.nextToken());
            this.a2 = Integer.parseInt(st.nextToken());
            if (a1 > a0 || a0 > a2){
                Messaging.showMessageDialog("Ошибка данных.\nПроверьте правильность ввода треугольных чисел: "+data);
            }
        }

        public void addTNtoZ(TriangularNumber tn, int cellValue){
            if (tn == null || tn.a1 > tn.a0 || tn.a0 > tn.a2){
                Messaging.log("Input triangular number is incorrect.","err");
                return;
            }
            this.a1 += tn.a1*cellValue;
            this.a0 += tn.a0*cellValue;
            this.a2 += tn.a2*cellValue;
        }

        public TriangularNumber substractTNs(TriangularNumber tn){
            if (tn == null || tn.a1 > tn.a0 || tn.a0 > tn.a2){
                Messaging.log("Input triangular number is incorrect.","err");
                return null;
            }
            TriangularNumber number = new TriangularNumber();
            number.a1 = this.a1 - tn.a1;
            number.a0 = this.a0 - tn.a0;
            number.a2 = this.a2 - tn.a2;
            return number;
        }

        public void convert(){
            this.a1 = -a1;
            this.a0 = -a0;
            this.a2 = -a2;
        }

        public int getMin(){
            if (a1 < a0 && a1 < a2){
                return a1;
            } else if (a0 < a1 && a0 < a2){
                return a0;
            } else {
                return a2;
            }
        }

        public int getMax(){
            if (a1 > a0 && a1 > a2){
                return a1;
            } else if (a0 > a1 && a0 > a2){
                return a0;
            } else {
                return a2;
            }
        }

        public TriangularNumber getMax(TriangularNumber tn){
            TriangularNumber number = new TriangularNumber();
            number.a1 = this.a1 > tn.a1 ? this.a1 : tn.a1;
            number.a0 = this.a0 > tn.a0 ? this.a0 : tn.a0;
            number.a2 = this.a2 > tn.a2 ? this.a2 : tn.a2;
            return number;
        }

        public float getEps(){
            return Math.abs((a1+2*a0+a2)/2);
        }

        @Override
        public String toString() {
            return "[" + a1 + "," + a0 + "," + a2 + "]";
        }
    }

    @Override
    public String toString() {
        return "FuzzyMatrix{ matrix[" + Arrays.toString(matrix) +
                "], rowsNumber=" + rowsNumber +
                ", colsNumber=" + colsNumber +
                '}';
    }
}
