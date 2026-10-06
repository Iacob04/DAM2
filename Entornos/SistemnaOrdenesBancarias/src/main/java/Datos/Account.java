package Datos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Account {

    private static final String CABECERA = "Date        Amount  Balance";
    private static final String FORMATO_LINEA = "%-12s%6s%9d";
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("d.M.yyyy");

    private final Calendario calendario;
    private final List<Movimiento> movimientos = new ArrayList<>();

    public Account() {
        this(LocalDate::now);
    }

    public Account(Calendario calendario) {
        this.calendario = calendario;
    }

    public void deposit(int amount) {
        validarPositivo(amount);
        movimientos.add(new Movimiento(calendario.hoy(), amount));
    }

    public void withdraw(int amount) {
        validarPositivo(amount);
        if (amount > saldo()) {
            throw new SaldoInsuficiente("Saldo insuficiente para retirar " + amount);
        }
        movimientos.add(new Movimiento(calendario.hoy(), -amount));
    }

    public String printStatement() {
        StringBuilder sb = new StringBuilder(CABECERA);
        int saldo = 0;
        for (Movimiento m : movimientos) {
            saldo += m.importe();
            sb.append('\n').append(String.format(FORMATO_LINEA,
                    m.fecha().format(FORMATO_FECHA),
                    String.format("%+d", m.importe()),
                    saldo));
        }
        return sb.toString();
    }

    private int saldo() {
        return movimientos.stream().mapToInt(Movimiento::importe).sum();
    }

    private static void validarPositivo(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("El importe debe ser mayor que 0: " + amount);
        }
    }
}
