/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectobufferx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author entel
 */
public class Line {

    static final int ESC = 27; //^
    private ArrayList<Integer> lineBuffer;
    private int posicion, longitud;
    private boolean insertar;
    private char ultimo_caracter;

    public Line() {
        this.lineBuffer = new ArrayList<>();
        this.posicion = 0;
        this.longitud = 0;
        this.insertar = false;
    }

    public void addCaracter(int caracter) {
        if (!this.insertar) { // insetar normal
            this.longitud = this.lineBuffer.size();

            if (this.posicion < this.longitud) {
                //System.out.print("l " + this.longitud + " ");
                // System.out.print("p " + this.posicion + " ");
                //  System.out.print("s " + this.lineBuffer.size() + " ");
                for (int i = this.longitud; i > this.posicion; i--) {
                    int a1 = this.lineBuffer.get(this.posicion);
                    int a2 = this.lineBuffer.get(this.posicion + 1);
                    //   System.out.print("c1 " + (char) a1 + " ");
                    // System.out.print("c2 " + (char) a2 + " ");
                    this.lineBuffer.add(i, this.lineBuffer.get(i - 1));
                    this.lineBuffer.remove(i - 1);
                }
            }
            this.lineBuffer.add(this.posicion, caracter);
        } else { //modo sobreescritura, pulso insert
            if (this.posicion <= this.longitud) {
                this.lineBuffer.set(this.posicion, caracter);
            } else {

                this.lineBuffer.add(this.longitud + 1, 0);
                this.lineBuffer.add(this.posicion, caracter);
                this.longitud++;
            }
        }
        this.ultimo_caracter = (char) caracter;
        this.posicion++;
    }

    public void changeInsert() {
        this.insertar = !this.insertar;
    }

    public String returnBuffer() {
        String str = "";
        int aux = 0;
        for (int i = 0; i < this.longitud; i++) {
            aux = this.lineBuffer.get(i);
            str += (char) aux;
        }
        return str;
    }

    public void derecha() {
        if (this.posicion < this.lineBuffer.size()) {
            this.posicion++;
        }
        System.out.print("\u001B[1C");
    }

    public void izquierda() {
        /*  if (this.posicion > 0) {
            this.posicion--;
        }*/
        if (this.posicion > 0) {
            this.posicion--;
        }
        System.out.print("\u001B[1D");
    }

    public void inicio() {
        System.out.print("\r");
    }

    public void fin() {
        if (this.lineBuffer.size() >= this.posicion) {
            int fin = this.lineBuffer.size() - this.posicion;
            System.out.print("\u001B[" + fin + "C");
        }
        this.posicion = this.lineBuffer.size();
    }

    public void suprimir() {
        System.out.print("\u001B[1P");
        this.lineBuffer.remove(this.posicion);
        this.longitud--;
    }

    public void bksp() {
        System.out.print("\b");
    }

    public static void setRaw() {
        String cmd[] = {"/bin/sh", "-c", "stty -echo raw </dev/tty"};
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void unsetRaw() {
        String cmd[] = {"/bin/sh", "-c", "stty echo cooked </dev/tty"};
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
