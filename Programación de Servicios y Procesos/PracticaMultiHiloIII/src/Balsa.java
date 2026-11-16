import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Random;

// PROCESO HIJO: una balsa. Habla con el barco por stdin/stdout.
public class Balsa {

    static PrintStream out = System.out;
    static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    static String pedir(String msg) throws Exception {
        out.println(msg);
        out.flush();
        return in.readLine();
    }

    public static void main(String[] args) throws Exception {
        Random rnd = new Random();
        while (true) {
            pedir("ENTRAR");                               // espera turno en la playa
            String[] r = pedir("EMBARCAR").split(" ");     // "suben quedan"
            int quedan = Integer.parseInt(r[1]);
            Thread.sleep(2000 + rnd.nextInt(1001));        // subir a bordo: 2-3 s
            pedir("SALIR");                                // libera sitio en la playa
            if (quedan == 0) break;
            Thread.sleep(1800 + rnd.nextInt(201));         // ir al barco y volver: 1,8-2 s
        }
        out.println("FIN");
        out.flush();
    }
}
