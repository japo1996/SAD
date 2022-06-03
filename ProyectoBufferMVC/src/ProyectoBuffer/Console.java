package ProyectoBuffer;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

/*
View represents the visualization of the data that model contains.
 */
/**
 *
 * @author japol
 */
public class Console implements Observer {

    private Line linia;

    static final String ANSI_DERECHA = "\033[C";
    static final String ANSI_IZQUIERDA = "\033[D";
    static final String ANSI_INSERTAR = "\033[4h";
    static final String ANSI_BACKSPACE = "\b";
    static final String ANSI_SUPRIMIR = "\033[P";
    static final String ANSI_INICIO = "\033[1~";
    static final String ANSI_FIN = "\033[4~";
    static final String ANSI_ESPACIO = "\033[ ";
    static final String ANSI_BLANK_SPACE = "\033[@";

    static final int ESCAPE_SEC = 4000;
    static final int SEC_INICIO = 4000;
    static final int SEC_DERECHA = 4001;
    static final int SEC_IZQUIERDA = 4002;
    static final int SEC_FIN = 4003;
    static final int SEC_INSERTAR = 4004;
    static final int SEC_SUPRIMIR = 4005;
    static final int CARACTER = 4006;
    static final int FINAL = 4007;

    public Console(Line linia) {
        this.setRaw();
        this.linia = linia;
    }

    public void derecha() {
        System.out.print(ANSI_DERECHA);
    }

    public void izquierda() {
        System.out.print(ANSI_IZQUIERDA);
    }

    public void fin() {
        this.unsetRaw();
        System.out.print(ANSI_FIN);
    }

    public void inicio() {
        System.out.print(ANSI_INICIO);
    }

    public void suprimir() {
        System.out.print(ANSI_SUPRIMIR);
    }

    public void insertar() {
        System.out.print(ANSI_INSERTAR);
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

    public void update(Observable o, Object o1) {
        int opcion = (int) o1;
        switch (opcion) {
            case CARACTER:
                boolean aux = this.linia.isInsertar();
                this.printChar(this.linia.getUltimo_caracter(), aux);
                break;
            case SEC_DERECHA:
                this.derecha();
                break;
            case SEC_IZQUIERDA:
                this.izquierda();
                break;
            case SEC_SUPRIMIR:
                this.suprimir();
                break;
            case SEC_INICIO:
                this.goTo(this.linia.getPosicion());
                break;
            case SEC_FIN:
                this.goTo(this.linia.getPosicion());
                this.fin();
                break;
            case SEC_INSERTAR:
                this.insertar();
                break;
            case FINAL:
                this.unsetRaw();
                break;
        }
    }

    public void goTo(int posicion) {
        int aux = posicion + 1;
        System.out.print("\033[" + aux + "G");
    }

    public void printChar(char car, boolean insert) {
        if (insert) {
            System.out.print(car);
        } else {
            System.out.print(ANSI_BLANK_SPACE);
            System.out.print(car);
        }
    }
}
