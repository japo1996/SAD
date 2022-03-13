/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectobufferx;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 *
 * @author entel
 */
public class ProyectoBufferX {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BufferedReader in = new EditableBufferedReader(new InputStreamReader(System.in));
        String str = null;
        try {
            str = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("\nline is: " + str);
    }

}
