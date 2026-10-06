package Datos;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountTest {

    private static final String CABECERA = "Date        Amount  Balance";

    /** Calendario falso: cada llamada a hoy() devuelve la siguiente fecha de la lista. */
    private static Calendario fechas(LocalDate... dias) {
        Iterator<LocalDate> it = List.of(dias).iterator();
        return it::next;
    }

    @Test
    void cuentaNuevaImprimeSoloLaCabecera() {
        Account account = new Account(fechas());

        assertEquals(CABECERA, account.printStatement());
    }

    @Test
    void unDepositoApareceConFechaImporteYSaldo() {
        Account account = new Account(fechas(LocalDate.of(2015, 12, 24)));

        account.deposit(500);

        assertEquals(CABECERA + "\n"
                + "24.12.2015    +500      500", account.printStatement());
    }

    @Test
    void dosDepositosAcumulanElSaldo() {
        Account account = new Account(fechas(
                LocalDate.of(2015, 12, 24),
                LocalDate.of(2016, 1, 5)));

        account.deposit(500);
        account.deposit(200);

        assertEquals(CABECERA + "\n"
                + "24.12.2015    +500      500\n"
                + "5.1.2016      +200      700", account.printStatement());
    }

    @Test
    void ejemploDelEnunciado() {
        Account account = new Account(fechas(
                LocalDate.of(2015, 12, 24),
                LocalDate.of(2016, 8, 23)));

        account.deposit(500);
        account.withdraw(100);

        assertEquals(CABECERA + "\n"
                + "24.12.2015    +500      500\n"
                + "23.8.2016     -100      400", account.printStatement());
    }

    @Test
    void depositarCeroONegativoLanzaExcepcion() {
        Account account = new Account(fechas());

        assertThrows(IllegalArgumentException.class, () -> account.deposit(0));
        assertThrows(IllegalArgumentException.class, () -> account.deposit(-50));
    }

    @Test
    void retirarCeroONegativoLanzaExcepcion() {
        Account account = new Account(fechas());

        assertThrows(IllegalArgumentException.class, () -> account.withdraw(0));
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(-50));
    }

    @Test
    void retirarMasDelSaldoLanzaExcepcion() {
        Account account = new Account(fechas(LocalDate.of(2015, 12, 24)));
        account.deposit(100);

        assertThrows(SaldoInsuficiente.class, () -> account.withdraw(101));
    }

    @Test
    void unaOperacionFallidaNoCambiaElExtracto() {
        Account account = new Account(fechas(LocalDate.of(2015, 12, 24)));
        account.deposit(100);
        String antes = account.printStatement();

        assertThrows(SaldoInsuficiente.class, () -> account.withdraw(500));
        assertThrows(IllegalArgumentException.class, () -> account.deposit(-1));

        assertEquals(antes, account.printStatement());
    }
}
