package com.eezo;


import java.util.List;

/**
 *
 * Created by Eezo on 19.02.2016.
 */
public class AlternativeSolution {
    private Matrix matrix;
    private int Z;
    private FuzzyMatrix.TriangularNumber fuzzyZ;
    private int sigma;
    private int lastASFound;

    public AlternativeSolution(int rowCount, int colCount) {
        matrix = new Matrix(rowCount, colCount);
        lastASFound = -1;
        Messaging.log("Created new Alternative Solution with "+rowCount+" rows and "+colCount+" columns.");
    }

    public int getASCount() {
        return matrix.getEmptyCellsNumber();
    }

    /**
     * Calculates costs.
     * @param tdMatrix table data matrix
     */
    public void calculateZ(int[][] tdMatrix) {
        Z = 0;
        for (int i = 0; i < tdMatrix.length; i++) {
            for (int j = 0; j < tdMatrix[i].length; j++) {
                Z += matrix.getElementValue(i, j) * tdMatrix[i][j]; // might be simplify to getElement().getValue()
            }
        }
    }

    public void calculateFuzzyZ(FuzzyMatrix.TriangularNumber[][] ftdMatrix){
        fuzzyZ = new FuzzyMatrix.TriangularNumber();
        for (int i = 0; i < ftdMatrix.length; i++) {
            for (int j = 0; j < ftdMatrix[i].length; j++) {
                fuzzyZ.addTNtoZ(ftdMatrix[i][j], matrix.getElementValue(i, j));
            }
        }
        Messaging.log("Fuzzy Z = "+fuzzyZ);
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
            Messaging.log("Can't find new AS: no empty cell position found.","warn");
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
            Messaging.log("Count of non zero cells must be equal rows number + columns number - 1","warn");
            return null;
        }

        List<Matrix.Cell> markedCells = matrix.findAS(matrix.getElementByOrderNumber(lastASFound));
        AlternativeSolution newAS = new AlternativeSolution(matrix.getRowsNumber(), matrix.getColsNumber());
        newAS.matrix = matrix.clone();
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
        //System.out.println(newAS.matrix.toString());
        return newAS;
    }


    public static AlternativeSolution northWestCorner(int[] S, int[] N) {
        if (S == null || N == null || S.length == 0 || N.length == 0) {
            Messaging.log("S or N array is zero length.","err");
            return null;
        }
        // check for condition sum S == sum N
        int sumOfS = 0;
        int sumOfN = 0;
        for (int value : S) {
            sumOfS += value;
        }
        for (int aN : N) {
            sumOfN += aN;
        }
        if (sumOfS != sumOfN) {
            Messaging.showMessageDialog("Расчёт перевозок методом северо-западного угла невозможен:\n" +
                    "Сумма поставок не совпадает с суммой заявок!", "err");
            return null;
        }

        // create an alternative solution
        AlternativeSolution as = new AlternativeSolution(N.length, S.length);
        int[] s = S.clone();
        int[] n = N.clone();
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

    public Matrix getMatrix(){
        return this.matrix;
    }

    public FuzzyMatrix.TriangularNumber getFuzzyZ() {
        return fuzzyZ;
    }

    /**
     * Recalculates cells values according to their statuses
     */
    public void recalculateValues(){
        matrix.recalculateMatrixValues();
    }

    /**
     * Converts fuzzy number (unary minus)
     */
    public void convertFuzzyNumbers(){
        fuzzyZ.convert();
    }

    public int getFuzzyMin(){
        return fuzzyZ.getMin();
    }

    public float getFuzzyEps(){
        return fuzzyZ.getEps();
    }

    public int getZ() {
        return Z;
    }

    public int getSigma() {
        return sigma;
    }

    public void resetSigma(){
        sigma = 0;
    }

    @Override
    public String toString() {
        return matrix.toString() + "\nZ="+(Z == 0 ? "not calculated " : Z)+" sigma="+sigma+"\n\n";
    }
}
