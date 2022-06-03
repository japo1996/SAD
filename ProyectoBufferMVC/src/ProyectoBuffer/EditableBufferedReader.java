/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProyectoBuffer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author japol
 */
public class EditableBufferedReader extends BufferedReader {

    Line linia;
    Console consola;

    static final int ESC = 27; //^
    static final int DERECHA = 67;
    static final int IZQUIERDA = 68;
    static final int INICIO = 72;
    static final int FIN = 70;
    static final int INSERTAR = 50;
    static final int SUPRIMIR = 51;
    static final int CORCHETE = 91;
    static final int VIRGULILLA = 126;
    static final int INTERROGANTE = 63;
    static final int ENTER = 13;

    static final int ESCAPE_SEC = 4000;
    static final int SEC_INICIO = 4000;
    static final int SEC_DERECHA = 4001;
    static final int SEC_IZQUIERDA = 4002;
    static final int SEC_FIN = 4003;
    static final int SEC_INSERTAR = 4004;
    static final int SEC_SUPRIMIR = 4005;

    private int posicion, longitud;

    public EditableBufferedReader(Reader in) {
        super(in);
        this.posicion = 0;
        this.longitud = 0;
        this.linia = new Line();
        this.consola = new Console(this.linia);
        this.linia.addObserver(this.consola);
    }

    public String readLine() throws IOException {
        int caracter = 0;
        do {
            caracter = this.read();
            if (caracter >= ESCAPE_SEC) {
                switch (caracter) {
                    case SEC_DERECHA:
                        this.linia.derecha();
                        break;
                    case SEC_IZQUIERDA:
                        this.linia.izquierda();
                        break;
                    case SEC_INICIO:
                        this.linia.inicio();
                        break;
                    case SEC_FIN:
                        this.linia.fin();
                        break;
                    case SEC_SUPRIMIR:
                        this.linia.suprimir();
                        break;
                    case SEC_INSERTAR:
                        this.linia.changeInsert();
                        break;
                }
            } else if (caracter != ENTER) {
                this.linia.addCaracter(caracter);
            } else {
                break;
            }
        } while (caracter != ENTER);
        this.linia.enter();
        return this.linia.returnBuffer();
    }

    public int read() throws IOException {
        int caracter = 0;
        caracter = super.read();
        if (caracter == ESC) {
            caracter = super.read();
            if (caracter == CORCHETE) {
                caracter = super.read();
                switch (caracter) {
                    case DERECHA:
                        return SEC_DERECHA;
                    case IZQUIERDA:
                        return SEC_IZQUIERDA;
                    case INICIO:
                        return SEC_INICIO;
                    case FIN:
                        return SEC_FIN;
                    case SUPRIMIR:
                        caracter = this.read();
                        if (caracter == VIRGULILLA) {
                            return SEC_SUPRIMIR;
                        }
                        return -1;
                    case INSERTAR:
                        caracter = super.read();
                        return SEC_INSERTAR;
                }
            }
        } else {
            return caracter;
        }
        return -1;
    }
}
