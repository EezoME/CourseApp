package com.eezo.view;

import com.eezo.FuzzyMatrix;
import com.eezo.TransData;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * GUI that allows to input data via form manually.
 */
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
    /**
     * List of customers and their offers
     */
    private List<String> customers;
    /**
     * List of providers and their demands
     */
    private List<String> providers;
    /**
     * Costs per unit of production matrix
     */
    private int[][] matrix;
    /**
     * Fuzzy costs per unit of production matrix
     */
    private FuzzyMatrix.TriangularNumber[][] tmatrix;


    public InputDataDialog() {
        setModal(true);
        setContentPane(contentPane);
        setBounds(500, 310, 560, 340);
        getRootPane().setDefaultButton(buttonOK);

            customers = TransData.staticObject.getCustomers();
            providers = TransData.staticObject.getProviders();
            matrix = TransData.staticObject.getMatrix();
            tmatrix = TransData.staticObject.getFuzzyMatrix();

            for (int i = 0; i < customers.size(); i++) {
                customers.set(i, customers.get(i)+"-"+TransData.staticObject.getProvidersValues()[i]);
            }
            for (int i = 0; i < providers.size(); i++) {
                providers.set(i, providers.get(i)+"-"+TransData.staticObject.getCustomersValues()[i]);
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
            tmatrix = new FuzzyMatrix.TriangularNumber[customers.size()][providers.size()];
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
                tmatrix = new FuzzyMatrix.TriangularNumber[providers.size()][customers.size()];
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
        try {
            new TransData.Builder(customers, providers, matrix).build();
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
            return;
        }
        /*String message = TransData.getInputErrorMessage(transData.inputAllData(customers, providers, matrix, tmatrix));
        if (message != null){
            JOptionPane.showMessageDialog(null,message);
            return;
        }*/
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
            for (int i = 0; i < TransData.staticObject.getCustomers().size(); i++) {
                bw.write(TransData.staticObject.getCustomers().get(i)+"\n");
            }
            bw.write(":prov\n");
            for (int i = 0; i < TransData.staticObject.getProviders().size(); i++) {
                bw.write(TransData.staticObject.getProviders().get(i)+"\n");
            }
            bw.write(":custval\n");
            for (int i = 0; i < TransData.staticObject.getProvidersValues().length; i++) {
                bw.write(TransData.staticObject.getProvidersValues()[i]+"\n");
            }
            bw.write(":provval\n");
            for (int i = 0; i < TransData.staticObject.getCustomersValues().length; i++) {
                bw.write(TransData.staticObject.getCustomersValues()[i]+"\n");
            }
            bw.write(":mtrx\n");
            for (int i = 0; i < TransData.staticObject.getMatrix().length; i++) {
                for (int j = 0; j < TransData.staticObject.getMatrix()[i].length; j++) {
                    bw.write(TransData.staticObject.getMatrix()[i][j]+" ");
                }
                bw.write("\n");
            }
            bw.write(":tmtrx\n");
            for (int i = 0; i < TransData.staticObject.getFuzzyMatrix().length; i++) {
                for (int j = 0; j < TransData.staticObject.getFuzzyMatrix()[i].length; j++) {
                    bw.write(TransData.staticObject.getFuzzyMatrix()[i][j]+" ");
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

    public static void main(String[] args) {
        InputDataDialog dialog = new InputDataDialog();
        dialog.pack();
        dialog.setVisible(true);
    }
}
