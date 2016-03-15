package com.eezo;


import java.util.List;

/**
 *
 * Created by Eezo on 19.02.2016.
 */
public class AlternativeSolution {
    private Matrix matrix;
    private int Z;
    private int sigma;
    private int lastASFound;

    public AlternativeSolution(int rowCount, int colCount) {
        matrix = new Matrix(rowCount, colCount);
        lastASFound = -1;
    }

    public int getASCount() {
        return matrix.getEmptyCellsNumber();
    }

    public void calculateZ(int[][] tdMatrix) {
        Z = 0;
        for (int i = 0; i < tdMatrix.length; i++) {
            for (int j = 0; j < tdMatrix[i].length; j++) {
                Z += matrix.getElementValue(i, j) * tdMatrix[i][j]; // might be simplify to getElement().getValue()
            }
        }
    }

    public void calculateSigma(int[][] tdMatrix) {
        sigma = 0;
        for (int i = 0; i < matrix.getRowsNumber(); i++) {
            for (int j = 0; j < matrix.getColsNumber(); j++) {
                sigma += tdMatrix[i][j] * matrix.getElementStatus(i, j);
            }
        }
    }

    public AlternativeSolution findNewAS() {
        lastASFound = matrix.getEmptyCellPosition(lastASFound);
        if (lastASFound == -1){
            return null;
        }
        int counter = 0;
        for (int i = 0; i < matrix.getRowsNumber(); i++) {
            for (int j = 0; j < matrix.getColsNumber(); j++) {
                if (matrix.getElementValue(i, j) != 0) {
                    counter++;
                }
            }
        }
        if (counter != matrix.getRowsNumber() + matrix.getColsNumber() - 1) {
            // because it has to be
            return null;
        }

        List<Matrix.Cell> markedCells = matrix.findAS(matrix.getElementByOrderNumber(lastASFound));
        AlternativeSolution newAS = new AlternativeSolution(matrix.getRowsNumber(), matrix.getColsNumber());
        newAS.matrix = matrix;
        boolean flag = true;
        for (int i = 0; i < markedCells.size(); i++) {
            Matrix.Cell foundedCell = newAS.matrix.matrixContainsCell(markedCells.get(i));
            if (foundedCell != null){
                foundedCell.setStatus(flag);
                flag = !flag;
            }
        }
        /*for (int i = 0; i < markedCells.size(); i++) {
            System.out.println(markedCells.get(i).getX()+", "+markedCells.get(i).getY());
        }*/
        System.out.println(newAS.matrix.toString());
        return newAS;
    }


    public static AlternativeSolution northWestCorner(int[] S, int[] N) {
        if (S == null || N == null || S.length == 0 || N.length == 0) {
            return null;
        }
        AlternativeSolution as = new AlternativeSolution(N.length, S.length);
        int[] s = S.clone();
        int[] n = N.clone();
        System.out.println(as.matrix.getRowsNumber()+" - rows, "+as.matrix.getColsNumber()+" - cols");
        for (int i = 0; i < as.matrix.getRowsNumber(); i++) {
            for (int j = 0; j < as.matrix.getColsNumber(); j++) {
                if (n[i] < s[j]) {
                    as.matrix.getElement(i, j).setValue(n[i]);
                    s[j] -= n[i];
                    n[i] = 0;
                    break;
                } else {
                    as.matrix.getElement(i, j).setValue(s[j]);
                    n[i] -= s[j];
                    s[j] = 0;
                }
            }
        }
        return as;
    }

}
