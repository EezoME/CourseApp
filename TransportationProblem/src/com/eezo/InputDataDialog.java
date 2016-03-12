package com.eezo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InputDataDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton buttonMatrix;
    private JButton buttonProvidersAdd;
    private JList listCustomers;
    private JList listProviders;
    private JTable tableMatrix;
    private JButton buttonCustomersAdd;
    private JButton buttonCustomersDelete;
    private JButton buttonProvidersDelete;
    private JCheckBox checkboxWriteData;
    private JTextField tfFileName;
    private JTable tableTMatrix;
    private JButton buttonTMatrix;
    private List<String> customers;
    private List<String> providers;
    private int[][] matrix;
    private TransData.TriangularNumber[][] tmatrix;

    private TransData transData;

    public InputDataDialog(TransData transData) {
        setModal(true);
        setContentPane(contentPane);
        setBounds(500, 310, 560, 340);
        getRootPane().setDefaultButton(buttonOK);

        this.transData = transData;
        if (transData != null){
            customers = transData.getCustomers();
            providers = transData.getProviders();
            matrix = transData.getMatrix();
            tmatrix = transData.getTmatrix();

            for (int i = 0; i < customers.size(); i++) {
                customers.set(i, customers.get(i)+"-"+transData.getProvidersValues()[i]);
            }
            for (int i = 0; i < providers.size(); i++) {
                providers.set(i, providers.get(i)+"-"+transData.getCustomersValues()[i]);
            }
        }
        if (customers == null){
            customers = new ArrayList<>();
        }
        if (providers == null){
            providers = new ArrayList<>();
        }
        if (matrix == null){
            matrix = new int[customers.size()][providers.size()];
        }
        if (tmatrix == null){
            tmatrix = new TransData.TriangularNumber[customers.size()][providers.size()];
        }
        loadLists();
        loadMatrix();
        loadTMatrix();

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        buttonCustomersAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String customer = JOptionPane.showInputDialog("Введите значение в формате <название>-<предложение> (<название>-<Si>)");
                if (!checkListRecord(customer)){
                    JOptionPane.showMessageDialog(null,"Неверно введено значение.");
                    return;
                }
                customers.add(customer);
                loadLists();
            }
        });
        buttonCustomersDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (listCustomers.getSelectedIndex() == -1){
                    return;
                }
                customers.remove(listCustomers.getSelectedIndex());
                loadLists();
            }
        });
        buttonProvidersAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String provider = JOptionPane.showInputDialog("Введите значение в формате <название>-<спрос> (<название>-<Nj>)");
                if (!checkListRecord(provider)){
                    JOptionPane.showMessageDialog(null,"Неверно введено значение.");
                    return;
                }
                providers.add(provider);
                loadLists();
            }
        });
        buttonProvidersDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (listProviders.getSelectedIndex() == -1){
                    return;
                }
                providers.remove(listProviders.getSelectedIndex());
                loadLists();
            }
        });
        buttonMatrix.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                matrix = new int[providers.size()][customers.size()];
                loadMatrix();
            }
        });
        buttonTMatrix.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tmatrix = new TransData.TriangularNumber[providers.size()][customers.size()];
                loadTMatrix();
            }
        });
    }

    private void loadLists(){
        listCustomers.setListData(customers.toArray());
        listProviders.setListData(providers.toArray());
    }

    private void loadMatrix(){
        DefaultTableModel model = (DefaultTableModel) tableMatrix.getModel();
        model.setRowCount(matrix.length);
        if (matrix.length > 0){
            model.setColumnCount(matrix[0].length);
        } else {
            model.setColumnCount(0);
        }
        for (int i = 0; i < model.getRowCount(); i++) {
            for (int j = 0; j < model.getColumnCount(); j++) {
                model.setValueAt(matrix[i][j], i, j);
            }
        }
    }

    private void loadTMatrix(){
        DefaultTableModel model = (DefaultTableModel) tableTMatrix.getModel();
        model.setRowCount(tmatrix.length);
        if (tmatrix.length > 0){
            model.setColumnCount(tmatrix[0].length);
        } else {
            model.setColumnCount(0);
        }
        for (int i = 0; i < model.getRowCount(); i++) {
            for (int j = 0; j < model.getColumnCount(); j++) {
                model.setValueAt(tmatrix[i][j], i, j);
            }
        }
    }

    private void onOK() {
        if (transData == null){
            transData = new TransData();
        }
        String message = TransData.getInputErrorMessage(transData.inputAllData(customers, providers, matrix, tmatrix));
        if (message != null){
            JOptionPane.showMessageDialog(null,message);
            return;
        }
        if (checkboxWriteData.isSelected()){
            writeFile();
        }

        dispose();
    }


    private void onCancel() {
// add your code here if necessary
        dispose();
    }

    private void writeFile(){
        String fileName = tfFileName.getText();
        if (fileName.isEmpty()){
            JOptionPane.showMessageDialog(null, "Enter file name in the text field.");
            return;
        }
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(fileName)));
            bw.write(":cust\n");
            for (int i = 0; i < transData.getCustomers().size(); i++) {
                bw.write(transData.getCustomers().get(i)+"\n");
            }
            bw.write(":prov\n");
            for (int i = 0; i < transData.getProviders().size(); i++) {
                bw.write(transData.getProviders().get(i)+"\n");
            }
            bw.write(":custval\n");
            for (int i = 0; i < transData.getProvidersValues().length; i++) {
                bw.write(transData.getProvidersValues()[i]+"\n");
            }
            bw.write(":provval\n");
            for (int i = 0; i < transData.getCustomersValues().length; i++) {
                bw.write(transData.getCustomersValues()[i]+"\n");
            }
            bw.write(":mtrx\n");
            for (int i = 0; i < transData.getMatrix().length; i++) {
                for (int j = 0; j < transData.getMatrix()[i].length; j++) {
                    bw.write(transData.getMatrix()[i][j]+" ");
                }
                bw.write("\n");
            }
            bw.write(":tmtrx\n");
            for (int i = 0; i < transData.getTmatrix().length; i++) {
                for (int j = 0; j < transData.getTmatrix()[i].length; j++) {
                    bw.write(transData.getTmatrix()[i][j]+" ");
                }
                bw.write("\n");
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkListRecord(String record) {
        return !(record == null || record.isEmpty()) && record.matches("^[\"#'№!-&*а-яА-ЯёЁa-zA-Z0-9\\s]+[-]\\s*[\\d]+$");
    }

    public static void main(TransData transData) {
        InputDataDialog dialog = new InputDataDialog(transData);
        dialog.pack();
        dialog.setVisible(true);
    }
}
