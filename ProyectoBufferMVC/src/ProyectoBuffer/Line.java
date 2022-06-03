/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProyectoBuffer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author japol
 */
public class Line extends Observable {

    static final int CHARACTER = 4006;
    static final int FINAL = 4007;
    static final int SEC_BACKSPACE = 127;
    static final int ESCAPE_SEC = 4000;
    static final int SEC_HOME = 4000;
    static final int SEC_RIGHT = 4001;
    static final int SEC_LEFT = 4002;
    static final int SEC_FIN = 4003;
    static final int SEC_INSERT = 4004;
    static final int SEC_DELETE = 4005;

    private ArrayList<Integer> lineBuffer;
    private int posicion;
    private boolean insertar;
    private char ultimo_caracter;

    public Line() {
        this.lineBuffer = new ArrayList<>();
        this.posicion = 0;
        this.insertar = false;
    }

    public void addCaracter(int caracter) {
        if (this.insertar) { 
            if (this.posicion < this.lineBuffer.size()) {
                this.lineBuffer.set(this.posicion, caracter);
            } else {
                this.lineBuffer.add(this.lineBuffer.size() + 1, 0);
                this.lineBuffer.add(this.posicion, caracter);
            }
        } else { 
            if (this.posicion < this.lineBuffer.size()) {
                for (int i = this.lineBuffer.size(); i > this.posicion; i--) {
                    this.lineBuffer.add(i, this.lineBuffer.get(i - 1));
                    this.lineBuffer.remove(i - 1);
                }
            }
            this.lineBuffer.add(this.posicion, caracter);
        }
        this.ultimo_caracter = (char) caracter;
        this.posicion++;
        this.setChanged();
        this.notifyObservers(CHARACTER);
    }

    public void changeInsert() {
        this.insertar = !this.insertar;
        this.setChanged();
        this.notifyObservers(SEC_INSERT);
    }

    public String returnBuffer() {
        String str = "";
        int aux = 0;
        for (int i = 0; i < this.lineBuffer.size(); i++) {
            aux = this.lineBuffer.get(i);
            str += (char) aux;
        }
        return str;
    }

    public void derecha() {
        if (this.posicion < this.lineBuffer.size()) {
            this.posicion++;
        }
        this.setChanged();
        this.notifyObservers(SEC_RIGHT);
    }

    public void izquierda() {
        if (this.posicion > 0) {
            this.posicion--;
        }
        this.setChanged();
        this.notifyObservers(SEC_LEFT);
    }

    public void inicio() {
        this.posicion = 0;
        this.setChanged();
        this.notifyObservers(SEC_HOME);
    }

    public void fin() {
        if (this.lineBuffer.size() >= this.posicion) {
            int fin = this.lineBuffer.size() - this.posicion;
            System.out.print("\u001B[" + fin + "C");
        }
        this.posicion = this.lineBuffer.size();
        this.setChanged();
        this.notifyObservers(SEC_FIN);
    }

    public void suprimir() {
        this.lineBuffer.remove(this.posicion);
        this.setChanged();
        this.notifyObservers(SEC_DELETE);
    }

    public void bksp() {
        System.out.print("\b");
        this.setChanged();
        this.notifyObservers(SEC_INSERT);
    }

    public int getPosicion() {
        return posicion;
    }

    public int getLongitud() {
        return this.lineBuffer.size();
    }

    public boolean isInsertar() {
        return insertar;
    }

    public char getUltimo_caracter() {
        return ultimo_caracter;
    }

    public void enter() {
        this.inicio();
        this.setChanged();
        this.notifyObservers(FINAL);
    }
}
