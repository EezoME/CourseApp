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
        int rowCounter = 0, colCounter = 0;
        for (int i = 0; i < this.matrix.length; i++) {
            matrix[i] = new Cell(rowCounter, colCounter, 0);
            if (colCounter == colsNumber-1){
                rowCounter++;
                colCounter = 0;
            } else {
                colCounter++;
            }
        }
        for (int i = 0; i < matrix.length; i++) {
            System.out.println(matrix[i]);
        }
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
        System.out.println("row="+row+" col="+col);
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i].getX() == row && matrix[i].getY() == col){
                return matrix[i];
            }
        }
        System.out.println("ssssssss");
        return null;
    }

    public Cell getElementByOrderNumber(int number){
        return number >= 0 && matrix != null ? matrix[number] : null;
    }

    public int getElementValue(int row, int col){
        return getElement(row, col).getValue();
    }

    public int getElementStatus(int row, int col){
        // empty - 0, 1 - true, -1 - false
        return getElement(row, col).getStatus() == null ? 0 : getElement(row, col).getStatus() ? 1 : -1;
    }

    private Cell[] getSpecificRowElements(Cell cell){
        Cell[] cells = new Cell[rowsNumber];
        int ind = 0;
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i].getY() == cell.getY()){
                cells[ind++] = matrix[i];
            }
        }
        return cells;
    }

    private Cell[] getSpecificColElements(Cell cell){
        Cell[] cells = new Cell[colsNumber];
        int ind = 0;
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i].getX() == cell.getX()){
                cells[ind++] = matrix[i];
            }
        }
        return cells;
    }

    /**
     * Входной параметр - начальная позиция (НП)
     Вызывается метод проверки текущей строки. Далее метод проверки текущего столбца и т.д.
     Проверка текущей строки(ячейка вхождения)
     Если она содержит НП, которая не является ЯВ, то возврат - НП
     Иначе проверка заполненых ячеек
     Если столбец ячейки содержит непустые значения, то возврат - заполненая ячейка строки
     Проверка текущего столбца
     Если она содержит НП, которая не является ЯВ, то возврат - НП
     Иначе проверка заполненых ячеек
     Если строка ячейки содержит непустые значения, то возврат - заполненная ячейка столбца
     * @param targetCell target cell
     */
    public List<Cell> findAS(Cell targetCell){
        List<Cell> cells = new ArrayList<>();
        cells.add(targetCell);
        boolean flag = true;
        do {
            if (flag) {
                cells.add(checkRow(cells.get(cells.size() - 1), targetCell));
            } else {
                cells.add(checkCol(cells.get(cells.size() - 1), targetCell));
            }
            if (cells.get(cells.size() - 1) == null || cells.get(cells.size() - 1) == targetCell){
                break;
            }
            flag = !flag;
        } while (true);
        return cells;
    }

    private Cell checkRow(Cell currentCell, Cell targetCell){
        Cell[] cells = getSpecificRowElements(currentCell);
        for (int i = 0; i < cells.length; i++) {
            if (cells[i] == targetCell && cells[i] != currentCell){
                return cells[i];
            }
            if (cells[i].getValue() == 0){
                break;
            }
            return checkCol(cells[i], targetCell);
        }
        return null;
    }

    private Cell checkCol(Cell currentCell, Cell targetCell){
        Cell[] cells = getSpecificColElements(currentCell);
        for (int i = 0; i < cells.length; i++) {
            if (cells[i] == targetCell && cells[i] != currentCell){
                return cells[i];
            }
            if (cells[i].getValue() == 0){
                break;
            }
            return checkRow(cells[i], targetCell);
        }
        return null;
    }

    public int getRowsNumber() {
        return rowsNumber;
    }

    public int getColsNumber() {
        return colsNumber;
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

        public int getY() {
            return y;
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

        @Override
        public String toString() {
            return "Cell{" +
                    "x=" + x +
                    ", y=" + y +
                    ", value=" + value +
                    ", status=" + status +
                    '}';
        }
    }
}