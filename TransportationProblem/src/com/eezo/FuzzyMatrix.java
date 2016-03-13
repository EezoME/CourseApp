package com.eezo;

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

        public TriangularNumber(String data){
            if (data == null || data.isEmpty()){
                return;
            }
            StringTokenizer st = new StringTokenizer(data, ",");
            this.a1 = Integer.parseInt(st.nextToken());
            this.a0 = Integer.parseInt(st.nextToken());
            this.a2 = Integer.parseInt(st.nextToken());
        }

        @Override
        public String toString() {
            return a1 + "," + a0 + "," + a2;
        }
    }
}
