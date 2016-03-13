package com.eezo;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that represents a matrix of costs
 * Created by Eezo on 03.03.2016.
 */
class Matrix {
    private Cell[] matrix;
    private int rowsNumber;
    private int colsNumber;

    public Matrix(int rows, int cols) {
        this.matrix = new Cell[rows*cols];
        this.rowsNumber = rows;
        this.colsNumber = cols;
    }

    public int getEmptyCellsNumber(){
        int counter = 0;
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i].getValue() == 0){
                counter++;
            }
        }
        return counter;
    }

    public Cell getElement(int row, int col){
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i].getY() == row && matrix[i].getX() == col){
                return matrix[i];
            }
        }
        return null;
    }

    public Cell getElementByOrderNumber(int number){
        return number > 0 && matrix != null ? matrix[number] : null;
    }

    public int getElementValue(int row, int col){
        return getElement(row, col).getValue();
    }

    public int getElementStatus(int row, int col){
        // empty - 0, 1 - true, -1 - false
        return getElement(row, col).getStatus() == null ? 0 : getElement(row, col).getStatus() ? 1 : -1;
    }

    public List getDecisionTree(int startCellPos){
        Cell startCell = getElementByOrderNumber(startCellPos);
        if (startCell == null){
            return null;
        }
        List<List<Cell>> decisionTree = new ArrayList<>();
        decisionTree.add(new ArrayList<Cell>());
        decisionTree.get(0).add(startCell);

        //

        return decisionTree;
    }

    public void setMatrix(Cell[] matrix) {
        this.matrix = matrix;
    }

    public int getRowsNumber() {
        return rowsNumber;
    }

    public void setRowsNumber(int rowsNumber) {
        this.rowsNumber = rowsNumber;
    }

    public int getColsNumber() {
        return colsNumber;
    }

    public void setColsNumber(int colsNumber) {
        this.colsNumber = colsNumber;
    }

    class Cell {
        private int x;
        private int y;
        private int value;
        private Boolean status;

        public Cell(int x, int y, int value) {
            this.x = x;
            this.y = y;
            this.value = value;
            this.status = null;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            if (x < 0){
                return;
            }
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            if (y < 0){
                return;
            }
            this.y = y;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }
    }
}