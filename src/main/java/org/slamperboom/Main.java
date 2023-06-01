package org.slamperboom;

import org.slamperboom.model.Model;

public class Main {
    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        }
        Model model = new Model();
    }
}