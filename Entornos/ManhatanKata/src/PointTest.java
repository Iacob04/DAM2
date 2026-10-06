import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PointTest {

    @Test
    void distanciaEntrePuntosPositivos() {
        Point a = new Point(1, 2);
        Point b = new Point(4, 6);
        assertEquals(7, Point.manhattanDistance(a, b));
    }

    @Test
    void distanciaConCoordenadasNegativas() {
        Point a = new Point(-3, 5);
        Point b = new Point(2, -1);
        assertEquals(11, Point.manhattanDistance(a, b));
    }

    @Test
    void distanciaAlMismoPuntoEsCero() {
        Point a = new Point(5, 5);
        assertEquals(0, Point.manhattanDistance(a, a));
    }

    @Test
    void distanciaEntrePuntosConMismasCoordenadasEsCero() {
        Point a = new Point(3, -4);
        Point b = new Point(3, -4);
        assertEquals(0, Point.manhattanDistance(a, b));
    }

    @Test
    void distanciaEsSimetrica() {
        Point a = new Point(1, 9);
        Point b = new Point(-6, 2);
        assertEquals(Point.manhattanDistance(a, b), Point.manhattanDistance(b, a));
    }

    @Test
    void distanciaSoloHorizontal() {
        Point a = new Point(0, 0);
        Point b = new Point(10, 0);
        assertEquals(10, Point.manhattanDistance(a, b));
    }

    @Test
    void distanciaSoloVertical() {
        Point a = new Point(0, 0);
        Point b = new Point(0, -8);
        assertEquals(8, Point.manhattanDistance(a, b));
    }
}