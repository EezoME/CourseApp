package com.eezo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;

/**
 * Coursework application.
 * Created by Eezo on 07.02.2016.
 */
public class MainGUI extends JFrame {
    private JButton buttonInputDataManually;
    private JButton buttonLoadDataFromFile;
    private JButton buttonDoCalculations;
    private JPanel panel1;

    private TransData transData;

    public MainGUI() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(panel1);
        setBounds(430, 350, 780, 280);
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
                chooser.setCurrentDirectory(new File("..\\"));
                chooser.showDialog(panel1, "Выбрать файл");
                readFile(chooser.getSelectedFile());
            }
        });
        buttonDoCalculations.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                firstStage();
            }
        });
    }

    public void firstStage(){
        java.util.List<AlternativeSolution> asList = new ArrayList<>();
        asList.add(AlternativeSolution.northWestCorner(transData.getProvidersValues(), transData.getCustomersValues()));
        asList.get(0).calculateZ(transData.getMatrix());
        for (int i = 0; i < asList.get(0).getASCount(); i++) {
            //TODO find an alternative solution
        }
    }

    /** CUSTOM **/

    private void launchDialog() {
        InputDataDialog.main(transData);
    }

    private void readFile(File file) {
        if (file == null) {
            return;
        }
        if (transData == null) {
            transData = new TransData();
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            int state = 0;
            int counter = 0;
            boolean isEnd = false;
            while (!isEnd) {
                String line = br.readLine();
                if (line == null){
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
                            transData.setCustomers(new ArrayList<String>());
                            state = 1;
                            break;
                        case ":prov":
                            transData.setProviders(new ArrayList<String>());
                            state = 2;
                            break;
                        case ":custval":
                            transData.setProvidersValues(new int[transData.getCustomers().size()]);
                            state = 3;
                            counter = 0;
                            break;
                        case ":provval":
                            transData.setCustomersValues(new int[transData.getProviders().size()]);
                            state = 4;
                            counter = 0;
                            break;
                        case ":mtrx":
                            transData.setMatrix(new int[transData.getProviders().size()][transData.getCustomers().size()]);
                            state = 5;
                            counter = 0;
                            break;
                        case ":tmtrx":
                            transData.setTmatrix(new TransData.TriangularNumber[transData.getProviders().size()][transData.getCustomers().size()]);
                            state = 6;
                            counter = 0;
                            break;
                    }
                    continue;
                }
                switch (state) {
                    case 1:
                        transData.getCustomers().add(line);
                        break;
                    case 2:
                        transData.getProviders().add(line);
                        break;
                    case 3:
                        transData.getProvidersValues()[counter++] = Integer.parseInt(line);
                        break;
                    case 4:
                        transData.getCustomersValues()[counter++] = Integer.parseInt(line);
                        break;
                    case 5:
                        StringTokenizer st = new StringTokenizer(line);
                        int i = 0;
                        while (st.hasMoreTokens()) {
                            transData.getMatrix()[counter][i++] = Integer.parseInt(st.nextToken());
                        }
                        counter++;
                        break;
                    case 6:
                        StringTokenizer st2 = new StringTokenizer(line);
                        int j = 0;
                        while (st2.hasMoreTokens()) {
                            transData.getTmatrix()[counter][j++] = transData.new TriangularNumber(st2.nextToken());
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
