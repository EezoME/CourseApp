package com.eezo;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * Created by Eezo on 07.02.2016.
 */
public class TransData {
    private static final int NULL_PARAMETER_ERROR = -1;
    private static final int CUSTOMERS_OFFERS_DISCREPANCY_ERROR = -2;
    private static final int PROVIDERS_DEMANDS_DISCREPANCY_ERROR = -3;
    private static final int EMPTY_LIST_ERROR = -4;

    private List<String> customers;
    private int[] customersValues; // N
    private List<String> providers;
    private int[] providersValues; // S
    private int[][] matrix;
    private TriangularNumber[][] tmatrix;


    public List<String> getCustomers() {
        return customers;
    }

    public void setCustomers(List<String> customers) {
        if (customers == null){
            return;
        }
        this.customers = customers;
    }

    public int[] getCustomersValues() {
        return customersValues;
    }

    public void setCustomersValues(int[] customersValues) {
        if (customersValues == null || customersValues.length == 0){
            return;
        }
        this.customersValues = customersValues;
    }

    public List<String> getProviders() {
        return providers;
    }

    public void setProviders(List<String> providers) {
        if (providers == null){
            return;
        }
        this.providers = providers;
    }

    public int[] getProvidersValues() {
        return providersValues;
    }

    public void setProvidersValues(int[] providersValues) {
        if (providersValues == null || providersValues.length == 0){
            return;
        }
        this.providersValues = providersValues;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0){
            return;
        }
        this.matrix = matrix;
    }

    public TriangularNumber[][] getTmatrix() {
        return tmatrix;
    }

    public void setTmatrix(TriangularNumber[][] tmatrix) {
        if (tmatrix == null){
            return;
        }
        this.tmatrix = tmatrix;
    }

    class TriangularNumber {
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

    /** ���������� (offer) customersValues � ����� (demand) providersValues
     * Writes all data into the object.
     * @param customers list of customers
     * @param s offer for customers
     * @param providers list of providers
     * @param n demand of providers
     * @return int value:<br>
     *     <b>0</b> - if it`s fine;<br>
     *     <b>-1</b> - if at least one of the parameters is null;<br>
     *     <b>-2</b> - if the customers list length not equal to the offers array length;<br>
     *     <b>-3</b> - if the providers list length not equal to the demands array length;<br>
     *     <b>-4</b> - if the customers list or providers list is empty.
     */
    public int inputAllData(List<String> customers, int[] s, List<String> providers, int[] n, int[][] matrix, TriangularNumber[][] tmatrix){
        if (customers == null || s == null || providers == null || n == null || matrix == null || tmatrix == null){
            return NULL_PARAMETER_ERROR;
        }
        if (customers.size() != s.length){
            return CUSTOMERS_OFFERS_DISCREPANCY_ERROR;
        }
        if (providers.size() != n.length){
            return PROVIDERS_DEMANDS_DISCREPANCY_ERROR;
        }
        if (customers.size() == 0 || providers.size() == 0){
            return EMPTY_LIST_ERROR;
        }
        this.customers = customers;
        this.customersValues = n;
        this.providers = providers;
        this.providersValues = s;
        this.matrix = matrix;
        for (int i = 0; i < tmatrix.length; i++) {
            for (int j = 0; j < tmatrix[i].length; j++) {
                if (tmatrix[i][j] == null){
                    new TriangularNumber("0,0,0");
                }
            }
        }
        this.tmatrix = tmatrix;
        return 0;
    }

    /**
     * Writes all data into the object.
     * @param customers list of customers
     * @param providers list of providers
     * @return int value:<br>
     *     <b>0</b> - if it`s fine;<br>
     *     <b>-1</b> - if at least one of the parameters is null;<br>
     *     <b>-2</b> - if the customers list length not equal to the offers array length;<br>
     *     <b>-3</b> - if the providers list length not equal to the demands array length;<br>
     *     <b>-4</b> - if the customers list or providers list is empty.
     */
    public int inputAllData(List<String> customers, List<String> providers, int[][] matrix, TriangularNumber[][] tmatrix){
        if (customers == null || providers == null || matrix == null){
            return NULL_PARAMETER_ERROR;
        }
        if (customers.size() == 0 || providers.size() == 0){
            return EMPTY_LIST_ERROR;
        }
        int[] s = new int[customers.size()];
        int[] n = new int[providers.size()];
        List<String> newCustomers = new ArrayList<>();
        List<String> newProviders = new ArrayList<>();
        for (int i = 0; i < customers.size(); i++) {
            int pos = customers.get(i).indexOf("-");
            newCustomers.add(customers.get(i).substring(0,pos).trim());
            s[i] = Integer.parseInt(customers.get(i).substring(pos+1).trim());
        }
        for (int i = 0; i < providers.size(); i++) {
            int pos = providers.get(i).indexOf("-");
            newProviders.add(providers.get(i).substring(0,pos).trim());
            n[i] = Integer.parseInt(providers.get(i).substring(pos+1).trim());
        }
        return inputAllData(newCustomers, s, newProviders, n, matrix, tmatrix);
    }

    public int inputAllData(TransData object){
        if (object == null){
            return -1;
        }
        return inputAllData(object.customers, object.providers, object.getMatrix(), object.getTmatrix());
    }

    /**
     * Returns error message for error code from method inputAllData.
     * @param code error code
     * @return error message;<br>
     *     <b>null</b> if all is fine.
     */
    public static String getInputErrorMessage(int code){
        if (code == NULL_PARAMETER_ERROR){
            return "One of parameters is NULL.";
        }
        if (code == CUSTOMERS_OFFERS_DISCREPANCY_ERROR){
            return "The customers list length not equal to the offers array length.";
        }
        if (code == PROVIDERS_DEMANDS_DISCREPANCY_ERROR){
            return "Providers list length not equal to the demands array length.";
        }
        if (code == EMPTY_LIST_ERROR){
            return "The customers list or the providers list is empty.";
        }
        return null;
    }

}
