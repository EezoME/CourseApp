package com.eezo.view;

import com.eezo.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Coursework application.
 * Created by Eezo on 07.02.2016.
 */
public class MainGUI extends JFrame {
    private JButton buttonInputDataManually;
    private JButton buttonLoadDataFromFile;
    private JPanel panel1;
    private JButton buttonFirstStage;
    private JButton buttonSecondStage;
    private JTable table1FS;
    private JTable table2SS;

    public MainGUI() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(panel1);
        setBounds(430, 350, 825, 280);
        setVisible(true);
        buttonInputDataManually.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                launchDialog();
            }
        });
        buttonLoadDataFromFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Выберете исходный файл");
                chooser.setCurrentDirectory(new File("..\\CourseApp\\"));
                chooser.showDialog(panel1, "Выбрать файл");
                readFile(chooser.getSelectedFile());
            }
        });
        buttonFirstStage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                firstStage();
            }
        });
        buttonSecondStage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                secondStage();
            }
        });
    }

    /*
    	Первая часть
1. Определение опороного решения (северо-западный угол для первого раза, +- для последующих).
2. Подсчёт Z.
3. Определение альтернативного решения:
3.1. Если есть АР - далее к п.4.
3.2. Если нет АР - к п.6.
4. Подсчёт сигма.
5. Возврат к п.3.
6. Когда закончатся АР, определить лучшее АР:
6.1. Если лучшее АР - первое, то далее к п.7.
6.2. Если лучшее АР - не первое, то к п.2.
7. Вывод о лучшем решении.
     */
    private void firstStage() {
        Messaging.log("FIRST STAGE STARTED");
        java.util.List<AlternativeSolution> asList = new ArrayList<>();
        try {
            asList.add(AlternativeSolution.northWestCorner(TransData.staticObject.getProvidersValues(), TransData.staticObject.getCustomersValues()));
        } catch (NullPointerException e) {
            Messaging.showMessageDialog("Введите данные или загрузите их с файла.", "err");
            return;
        }
        asList.get(0).calculateZ(TransData.staticObject.getMatrix());
        int loopCounter = 0;
        do {
            loopCounter++;
            Messaging.log("Loop #" + loopCounter);
            asList.add(asList.get(0).findNewAS());
            int counter = 1;
            while (asList.get(counter) != null) {
                asList.get(counter).calculateSigma(TransData.staticObject.getMatrix());
                asList.add(asList.get(0).findNewAS());
                counter++;
            }
            asList.remove(asList.size() - 1);
            counter = 0;
            int min = Integer.MAX_VALUE;
            for (int i = 0; i < asList.size(); i++) {
                System.out.println(asList.get(i));
                if (asList.get(i).getSigma() < min) {
                    min = asList.get(i).getSigma();
                    counter = i;
                }
            }
            // VIEW
            tablesStageOneToView(asList);
            //TODO histogram
            // IS CONTINUE ?
            if (min >= 0) {
                Messaging.showMessageDialog("Нет альтернатив лучших чем первоначальное решение.");
                break;
            } else {
                System.out.println("--  " + asList.get(counter));
                Messaging.showMessageDialog("Найдена лучшая альтернатива: номер - " + counter + ", значение сигма = " + min);
                asList.set(0, asList.get(counter));
                asList.get(0).recalculateValues();
                asList.get(0).resetSigma();
                while (asList.size() > 1) {
                    asList.remove(1);
                }
            }
        } while (true);
        Messaging.log("FIRST STAGE FINISHED");
    }

    /*
    	Вторая часть
1. Определение опороного решения (северо-западный угол).
2. Подсчёт Z (пополнение матрицы растрат).
3. Поиск АР через +-:
3.1. Если есть АР - к п.2.
3.2. Если нет АР - далее к п.4.
4. Трансформация матрицы растрат.
5. Поиск лучшего АР методом (ММ/С):
5.1. Если лучшее АР не первое - к п.2.
5.2. Если лучшее АР первое - далее к п.6.
6. Вывод о лучшем решении.
     */
    private void secondStage() {
        Messaging.log("SECOND STAGE STARTED");
        java.util.List<AlternativeSolution> asList = new ArrayList<>();
        try {
            asList.add(AlternativeSolution.northWestCorner(TransData.staticObject.getProvidersValues(), TransData.staticObject.getCustomersValues()));
        } catch (NullPointerException e) {
            Messaging.showMessageDialog("Введите данные или загрузите их с файла.", "err");
            return;
        }
        asList.get(0).calculateFuzzyZ(TransData.staticObject.getFuzzyMatrix());
        int loopCounter = 0;
        boolean switchMethod = true;
        AlternativeSolution prevBest = null;
        do {
            loopCounter++;
            Messaging.log("Loop #" + loopCounter);
            for (int i = 0; i < asList.get(0).getMatrix().getRowsNumber(); i++) {
                for (int j = 0; j < asList.get(0).getMatrix().getColsNumber(); j++) {
                    System.out.print(asList.get(0).getMatrix().getElementValue(i, j) + " ");
                }
                System.out.println();
            }
            int counter = 1;
            while (counter < asList.get(0).getASCount() + 1) {
                asList.add(asList.get(0).findNewAS());
                if (asList.get(counter) == null) {
                    asList.remove(counter);
                    break;
                }
                asList.get(counter).recalculateValues();
                asList.get(counter).calculateFuzzyZ(TransData.staticObject.getFuzzyMatrix());
                counter++;
            }
            tablesStageTwoToView(asList);
            List<AlternativeSolution> subList = switchMethod ? getBestASWithMinimaxCriteria(asList) : getBestASWithSevigCriteria(asList);
            if (subList == null || subList.isEmpty()) {
                Messaging.showMessageDialog("secondStage: list is null or empty");
                return;
            }
            AlternativeSolution best;
            if (subList.size() == 1) {
                Messaging.showMessageDialog("Найдено лучшее решение: " + subList.get(0).getFuzzyZ());
                best = subList.get(0);
            } else {
                String message = "Обнаружено несколько одинаково лучших альтернатив." +
                        "\nУкажите номер лучший из списка (укажите номер):\n";
                for (int i = 0; i < subList.size(); i++) {
                    message += (i + 1) + " - " + subList.get(i).getFuzzyZ() + "; eps=" + subList.get(i).getFuzzyEps() + "\n";
                }
                String answer = Messaging.showInputDialog(message);
                if (answer == null || answer.isEmpty()) {
                    Messaging.showMessageDialog("Неверно введён номер.", "err");
                    return;
                }
                best = subList.get(Integer.parseInt(answer));
                /*if (best.equals(asList.get(0))){
                    Messaging.showMessageDialog("Найдено ручшее решение: "+subList.get(0).getFuzzyZ());
                }*/
            }
            best.convertFuzzyNumbers();
            if (prevBest != null && best.equals(prevBest)) {
                Messaging.showMessageDialog("Нет альтернатив лучших чем первоначальное решение." + best.getFuzzyZ());
                break;
            }
            prevBest = best;
            System.out.println(prevBest.getASCount()+"  +++++");
            asList.set(0, best);
            while (asList.size() > 1) {
                asList.remove(1);
            }
            switchMethod = !switchMethod;
        } while (true);
        System.out.println("SECOND STAGE FINISHED.");
    }

    /**
     * CUSTOM
     **/

    private List<AlternativeSolution> getBestASWithMinimaxCriteria(List<AlternativeSolution> list) {
        if (list == null) {
            Messaging.log("Minimax: list is null.");
            return null;
        }
        if (list.size() < 2) {
            Messaging.log("Minimax: list has < 2 items.");
            list.get(0).convertFuzzyNumbers();
            List<AlternativeSolution> solutionList = new ArrayList<>();
            solutionList.add(list.get(0));
            return solutionList;
        }
        int[] minValues = new int[list.size()];
        float[] avgValue = new float[list.size()];
        for (int i = 0; i < list.size(); i++) {
            list.get(i).convertFuzzyNumbers();
            System.out.println(" ------ " + list.get(i).getFuzzyZ() + " --------- ");
            minValues[i] = list.get(i).getFuzzyMin();
            avgValue[i] = list.get(i).getFuzzyEps();
        }
        int max = Integer.MIN_VALUE;
        float minFloat = Float.MAX_VALUE;
        for (int i = 0; i < minValues.length; i++) {
            if (max < minValues[i]) {
                max = minValues[i];
                if (minFloat > avgValue[i]) {
                    minFloat = avgValue[i];
                }
            }
        }
        List<AlternativeSolution> resultList = new ArrayList<>();
        System.out.println(minValues.length + " ----- " + list.size());
        for (int i = 0; i < minValues.length; i++) {
            //if (Float.compare(minFloat, Float.MAX_VALUE) != 0){
            if (minValues[i] == max && Float.compare(minFloat, avgValue[i]) == 0) {
                resultList.add(list.get(i));
            }
            /*} else {
                if (minValues[i] == min){
                    resultList.add(list.get(i));
                }
            }*/
        }
        return resultList;
    }

    private List<AlternativeSolution> getBestASWithSevigCriteria(List<AlternativeSolution> list) {
        if (list == null) {
            Messaging.log("Sevig: list is null.");
            return null;
        }
        if (list.size() < 2) {
            Messaging.log("Sevig: list has < 2 items.");
            list.get(0).convertFuzzyNumbers();
            List<AlternativeSolution> solutionList = new ArrayList<>();
            solutionList.add(list.get(0));
            return solutionList;
        }
        FuzzyMatrix.TriangularNumber max = new FuzzyMatrix.TriangularNumber(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).convertFuzzyNumbers();
            max = list.get(i).getFuzzyZ().getMax(max);
        }
        FuzzyMatrix.TriangularNumber[] A = new FuzzyMatrix.TriangularNumber[list.size()];
        int maxVal = Integer.MAX_VALUE;
        for (int i = 0; i < A.length; i++) {
            A[i] = max.substractTNs(list.get(i).getFuzzyZ());
            if (A[i].getMax() < maxVal) {
                maxVal = A[i].getMax();
            }
        }
        List<AlternativeSolution> solutions = new ArrayList<>();
        for (int i = 0; i < A.length; i++) {
            if (A[i].getMax() == maxVal) {
                solutions.add(list.get(i));
            }
        }
        return solutions;
    }

    private void tablesStageOneToView(java.util.List<AlternativeSolution> list) {
        DefaultTableModel model = (DefaultTableModel) table1FS.getModel();
        int rowCounter = table1FS.getRowCount();
        model.setColumnCount(list.get(0).getMatrix().getColsNumber() + 1);
        for (int i = 0; i < list.size(); i++) {
            Matrix matrix = list.get(i).getMatrix();
            model.setRowCount(table1FS.getRowCount() + matrix.getRowsNumber() + 1);
            table1FS.setValueAt("AS #" + (i + 1), rowCounter, 5);
            table1FS.setValueAt("sigma=" + list.get(i).getSigma(), rowCounter + 1, 5);
            for (int j = 0; j < matrix.getRowsNumber(); j++) {
                for (int k = 0; k < matrix.getColsNumber(); k++) {
                    table1FS.setValueAt(matrix.getElementValue(j, k) + " / " + (matrix.getElementStatus(j, k) == 0 ? " " : matrix.getElementStatus(j, k)),
                            rowCounter, k);
                }
                rowCounter++;
            }
            rowCounter++;
        }
    }

    private void tablesStageTwoToView(java.util.List<AlternativeSolution> list) {
        DefaultTableModel model = (DefaultTableModel) table2SS.getModel();
        int rowCounter = table2SS.getRowCount();
        model.setColumnCount(list.get(0).getMatrix().getColsNumber() + 1);
        for (int i = 0; i < list.size(); i++) {
            Matrix matrix = list.get(i).getMatrix();
            model.setRowCount(table2SS.getRowCount() + matrix.getRowsNumber() + 1);
            table2SS.setValueAt("AS #" + (i + 1), rowCounter, 5);
            table2SS.setValueAt("Z=" + list.get(i).getFuzzyZ(), rowCounter + 1, 5);
            for (int j = 0; j < matrix.getRowsNumber(); j++) {
                for (int k = 0; k < matrix.getColsNumber(); k++) {
                    table2SS.setValueAt(matrix.getElementValue(j, k), rowCounter, k);
                }
                rowCounter++;
            }
            rowCounter++;
        }
    }

    private void launchDialog() {
        InputDataDialog.main(null);
    }

    private void readFile(File file) {
        if (file == null) {
            Messaging.showMessageDialog("File not found.");
            return;
        }
        new TransData.Builder(new ArrayList<String>(), new ArrayList<String>(), new int[5][3]).fuzzyMatrix(new FuzzyMatrix.TriangularNumber[5][3]).build();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            int state = 0;
            int counter = 0;
            boolean isEnd = false;
            while (!isEnd) {
                String line = br.readLine();
                if (line == null) {
                    isEnd = true;
                    continue;
                }
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                if (line.startsWith(":")) {
                    switch (line) {
                        case ":cust":
                            //TransData.staticObject.setCustomers(new ArrayList<String>());
                            state = 1;
                            break;
                        case ":prov":
                            //TransData.staticObject.setProviders(new ArrayList<String>());
                            state = 2;
                            break;
                        case ":custval":
                            TransData.staticObject.setProvidersValues(new int[TransData.staticObject.getCustomers().size()]);
                            state = 3;
                            counter = 0;
                            break;
                        case ":provval":
                            TransData.staticObject.setCustomersValues(new int[TransData.staticObject.getProviders().size()]);
                            state = 4;
                            counter = 0;
                            break;
                        case ":mtrx":
                            TransData.staticObject.setMatrix(new int[TransData.staticObject.getProviders().size()][TransData.staticObject.getCustomers().size()]);
                            state = 5;
                            counter = 0;
                            break;
                        case ":tmtrx":
                            TransData.staticObject.setFuzzyMatrix(new FuzzyMatrix.TriangularNumber[TransData.staticObject.getProviders().size()][TransData.staticObject.getCustomers().size()]);
                            state = 6;
                            counter = 0;
                            break;
                    }
                    continue;
                }
                switch (state) {
                    case 1:
                        TransData.staticObject.getCustomers().add(line);
                        break;
                    case 2:
                        TransData.staticObject.getProviders().add(line);
                        break;
                    case 3:
                        TransData.staticObject.getProvidersValues()[counter++] = Integer.parseInt(line);
                        break;
                    case 4:
                        TransData.staticObject.getCustomersValues()[counter++] = Integer.parseInt(line);
                        break;
                    case 5:
                        StringTokenizer st = new StringTokenizer(line);
                        int i = 0;
                        while (st.hasMoreTokens()) {
                            TransData.staticObject.getMatrix()[counter][i++] = Integer.parseInt(st.nextToken());
                        }
                        counter++;
                        break;
                    case 6:
                        StringTokenizer st2 = new StringTokenizer(line);
                        int j = 0;
                        while (st2.hasMoreTokens()) {
                            TransData.staticObject.getFuzzyMatrix()[counter][j++] = new FuzzyMatrix.TriangularNumber(st2.nextToken());
                        }
                        counter++;
                        break;
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainGUI();
            }
        });
    }
}
