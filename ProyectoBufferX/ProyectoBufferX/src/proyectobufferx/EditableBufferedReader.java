/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectobufferx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author entel
 */
public class EditableBufferedReader extends BufferedReader {

    static final int ESC = 27; //^
    static final int DERECHA = 67;
    static final int IZQUIERDA = 68;
    static final int INICIO = 72;
    static final int FIN = 70;
    static final int INSERTAR = 50;
    static final int SUPRIMIR = 51;
    static final int CORCHETE = 91;
    static final int VIRGULILLA = 126; //simbolo ~
    static final int INTERROGANTE = 63;
    static final int ENTER = 13;

    static final int ESCAPE_SEC = 4000; //a partir de 3000 hem definit les escape secuences
    static final int SEC_INICIO = 4000;
    static final int SEC_DERECHA = 4001;
    static final int SEC_IZQUIERDA = 4002;
    static final int SEC_FIN = 4003;
    static final int SEC_INSERTAR = 4004;
    static final int SEC_SUPRIMIR = 4005;

    private int posicion, longitud;
    Line linia;

    public EditableBufferedReader(Reader in) {
        super(in);
        this.posicion = 0;
        this.longitud = 0;
        this.linia = new Line();

    }

    public String readLine() throws IOException {
        //this.linia.unsetRaw(); no me deja escribir ninguna letra pero puedo mover el cursor bien
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
                // System.out.print("1");
                this.linia.addCaracter(caracter);
            } else {
                break;
            }

        } while (caracter != ENTER);
        this.linia.addCaracter(caracter);
        // System.out.println("2");
        this.linia.unsetRaw();
        return this.linia.returnBuffer();
    }

    public int read() throws IOException {
        int caracter = 0;
        this.linia.setRaw();
        caracter = super.read();

        if (caracter == ESC) {
            caracter = super.read();
            if (caracter == CORCHETE) {
                caracter = super.read();
                //poner en el switch las teclas que son menos el esc
                switch (caracter) {
                    case 67:
                        return SEC_DERECHA;
                    case IZQUIERDA:
                        return SEC_IZQUIERDA;
                    case INICIO:
                        return SEC_INICIO;
                    case FIN:
                        return SEC_FIN;
                    case SUPRIMIR:
                        caracter = super.read();
                        return SEC_SUPRIMIR;
                    case INSERTAR:
                        caracter = super.read();
                        return SEC_INSERTAR;
                }
            }
        } else {
            System.out.print((char) caracter);
            return caracter;
        }
        return -1;
    }
}
