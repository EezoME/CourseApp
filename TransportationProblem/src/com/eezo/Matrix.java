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
        }/*
        for (int i = 0; i < matrix.length; i++) {
            System.out.println(matrix[i]);
        }*/
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

    public int getEmptyCellPosition(int lastEmptyCellPosition){
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i].getValue() == 0 && i > lastEmptyCellPosition){
                return i;
            }
        }
        return -1;
    }

    public Cell getElement(int row, int col){
        //System.out.println("row="+row+" col="+col);
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i].getX() == row && matrix[i].getY() == col){
                return matrix[i];
            }
        }
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

    public Cell matrixContainsCell(Cell cell){
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i].equals(cell)){
                return matrix[i];
            }
        }
        return null;
    }

    /**
     * Returns a row, which contains specific cell
     * @param cell specific cell
     * @return an array of cells
     */
    private Cell[] getSpecificRowElements(Cell cell){
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
     * Returns a column, which contains specific cell
     * @param cell specific cell
     * @return an array of cells
     */
    private Cell[] getSpecificColElements(Cell cell){
        Cell[] cells = new Cell[rowsNumber];
        int ind = 0;
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i].getY() == cell.getY()){
                cells[ind++] = matrix[i];
            }
        }
        return cells;
    }

    /**
     * Finds a list of cells that should be marked with "+" or "-"
     * @param targetCell target cell
     * @return a list or cells
     */
    /*  Входной параметр - начальная позиция (НП)
        Вызывается метод проверки текущей строки, далее метод проверки текущего столбца и т.д.
        пока не образуется замкнутый контур
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
            System.out.println("Adding cell: "+cells.get(cells.size() - 1));
            if (cells.get(cells.size() - 1) == null || cells.get(cells.size() - 1) == targetCell){
                break;
            }
            flag = !flag;
        } while (true);
        return cells;
    }

    /**
     * Returns:<ul>
     *     <li><b>{targetCell}</b> - if row contains {targetCell} and {currentCell} != {targetCell}</li>
     *     <li><b>a cell from row</b> - if column of that cell contains non zero value cells</li>
     *     <li><b>null</b> - otherwise</li>
     * </ul>
     * @param currentCell cell entry
     * @param targetCell target cell
     * @return resulting cell
     */
    /*  Проверка текущей строки(ячейка вхождения, начальная позиция)
        Если она содержит НП, которая не является ЯВ, то возврат - НП
        Иначе проверка заполненых ячеек
        Если столбец ячейки содержит непустые значения, то возврат - заполненая ячейка строки
     */
    private Cell checkRow(Cell currentCell, Cell targetCell){
        Cell[] cells = getSpecificRowElements(currentCell);
        for (int i = 0; i < cells.length; i++) {
            if (cells[i] == currentCell)
                continue;
            if (cells[i] == targetCell && targetCell != currentCell){
                return cells[i];
            }
            if (cells[i].getValue() == 0){
                continue;
            }
            if (checkLineHasMoreValues(cells[i], "col", targetCell)){
                return cells[i];
            }
        }
        return null;
    }

    /**
     * Returns:<ul>
     *     <li><b>{targetCell}</b> - if column contains {targetCell} and {currentCell} != {targetCell}</li>
     *     <li><b>a cell from column</b> - if row of that cell contains non zero value cells</li>
     *     <li><b>null</b> - otherwise</li>
     * </ul>
     * @param currentCell cell entry
     * @param targetCell target cell
     * @return resulting cell
     */
    /*  Проверка текущего столбца(ячейка вхождения, начальная позиция)
        Если она содержит НП, которая не является ЯВ, то возврат - НП
        Иначе проверка заполненых ячеек
        Если строка ячейки содержит непустые значения, то возврат - заполненная ячейка столбца
     */
    private Cell checkCol(Cell currentCell, Cell targetCell){
        Cell[] cells = getSpecificColElements(currentCell);
        for (int i = 0; i < cells.length; i++) {
            if (cells[i] == currentCell)
                continue;
            if (cells[i] == targetCell && targetCell != currentCell){
                return cells[i];
            }
            if (cells[i].getValue() == 0){
                continue;
            }
            if (checkLineHasMoreValues(cells[i], "row", targetCell)){
                return cells[i];
            }
        }
        return null;
    }

    /**
     * Check line (row or column) for non zero values
     * @param currentCell core cell
     * @param lineType "row" or "col"
     * @return <b>true</b> - if line has non zero values, <b>false</b> - otherwise
     */
    private boolean checkLineHasMoreValues(Cell currentCell, String lineType, Cell targetCell){
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i] == currentCell){
                continue;
            }
            if (lineType.equalsIgnoreCase("row")){
                if (matrix[i].getX() == currentCell.getX()){
                    if (matrix[i].getValue() != 0 || matrix[i] == targetCell)
                        return true;
                }
            } else {
                if (matrix[i].getY() == currentCell.getY()){
                    if (matrix[i].getValue() != 0 || matrix[i] == targetCell)
                        return true;
                }
            }
        }
        return false;
    }

    public int getRowsNumber() {
        return rowsNumber;
    }

    public int getColsNumber() {
        return colsNumber;
    }

    @Override
    public String toString() {
        String result = "Matrix row count = "+rowsNumber+
                "\nmatrix column count = "+colsNumber+
                "\n\tmatrix:\n";
        for (int i = 0; i < matrix.length; i++) {
            result += matrix[i].printStatus();
        }
        return result;
    }

    class Cell {
        /**
         * Row index
         */
        private int x;
        /**
         * Column index
         */
        private int y;
        /**
         * Cell value
         */
        private int value;
        /**
         * Cell status:
         * <ul>
         *     <li><b>true</b> - cell marked with "+"</li>
         *     <li><b>false</b> - cell marked with "-"</li>
         *     <li><b>NULL</b> - cell has no marker</li>
         * </ul>
         */
        private Boolean status;

        /**
         * Main cell constructor. Field {status} is sets into NULL
         * @param x cell row index
         * @param y cell column index
         * @param value cell value
         */
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

        public void setStatus(Boolean status) {
            this.status = status;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Cell)) return false;

            Cell cell = (Cell) o;

            return !(getX() != cell.getX() || getY() != cell.getY()) && getValue() == cell.getValue();

        }

        @Override
        public int hashCode() {
            int result = getX();
            result = 31 * result + getY();
            result = 31 * result + getValue();
            return result;
        }

        public String printStatus(){
            String s = status == null ? "0" : status ? "+" : "-";
            if (y == colsNumber-1)
                s += "\n";
            return s;
        }

        @Override
        public String toString() {
            return "Cell{" +
                    "x = " + x +
                    ", y = " + y +
                    ", value = " + value +
                    ", status = " + printStatus() +
                    '}';
        }
    }
}