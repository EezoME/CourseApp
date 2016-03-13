package com.eezo;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * Created by Eezo on 07.02.2016.
 */
public class TransData {
    public static TransData staticObject;

    private List<String> customers;
    private int[] customersValues; // N
    private List<String> providers;
    private int[] providersValues; // S
    private int[][] matrix;
    private FuzzyMatrix.TriangularNumber[][] tmatrix;

    private TransData(Builder builder){
        this.customersValues = builder.customersValues;
        this.providersValues = builder.providersValues;
        this.matrix = builder.matrix;
        this.customers = builder.customers;
        this.providers = builder.providers;
        this.tmatrix = builder.tmatrix;
    }

    public static class Builder {
        private List<String> customers;
        private int[] customersValues; // N
        private List<String> providers;
        private int[] providersValues; // S
        private int[][] matrix;
        private FuzzyMatrix.TriangularNumber[][] tmatrix;

        /**
         * Adds customers list values, providers list values and matrix.
         * @param customersValues customers list values
         * @param providersValues providers list values
         * @param matrix matrix
         */
        public Builder(int[] customersValues, int[] providersValues, int[][] matrix){
            if (customersValues == null || providersValues == null || matrix == null){
                throw new NullPointerException();
            }
            if (customersValues.length == 0 || providersValues.length == 0 ||
                    matrix.length == 0 || matrix[0].length == 0){
                throw new IllegalArgumentException("Some of the arrays are zero length.");
            }
            this.customersValues = customersValues;
            this.providersValues = providersValues;
            this.matrix = matrix;
        }

        /**
         * Adds customers list values, providers list values,
         *  customers list, providers list and matrix.
         * @param customers customers data
         * @param providers providers data
         * @param matrix matrix
         */
        public Builder(List<String> customers, List<String> providers, int[][] matrix){
            if (customers == null || providers == null || matrix == null){
                throw new NullPointerException();
            }
            if (customers.size() == 0 || providers.size() == 0 ||
                    matrix.length == 0 || matrix[0].length == 0){
                throw new IllegalArgumentException("Some of the arrays are zero length.");
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
            this.customers = newCustomers;
            this.providers = newProviders;
            this.customersValues = s;
            this.providersValues = n;
            this.matrix = matrix;
        }

        public Builder customers(List<String> customers){
            if (customers == null){
                throw new NullPointerException();
            }
            if (customers.size() == 0 || customers.size() != customersValues.length){
                throw new IllegalArgumentException("Customers list are zero length or has different length with customers values array.");
            }
            this.customers = customers;
            return this;
        }

        public Builder providers(List<String> providers){
            if (providers == null){
                throw new NullPointerException();
            }
            if (providers.size() == 0 || providers.size() != providersValues.length){
                throw new IllegalArgumentException("Providers list are zero length or has different length with providers values array.");
            }
            this.providers = providers;
            return this;
        }

        public Builder fuzzyMatrix(FuzzyMatrix.TriangularNumber[][] tmatrix){
            if (tmatrix == null){
                throw new NullPointerException();
            }
            if (tmatrix.length == 0 || tmatrix[0].length == 0){
                throw new IllegalArgumentException("Fuzzy matrix array are zero length.");
            }
            this.tmatrix = tmatrix;
            return this;
        }

        public TransData build(){
            staticObject = new TransData(this);
            return staticObject;
        }
    }

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

    public FuzzyMatrix.TriangularNumber[][] getTmatrix() {
        return tmatrix;
    }

    public void setTmatrix(FuzzyMatrix.TriangularNumber[][] tmatrix) {
        if (tmatrix == null){
            return;
        }
        this.tmatrix = tmatrix;
    }

}
