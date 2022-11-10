package hr.java.vjezbe.entitet;

import java.util.Scanner;

/**
 * Zapečačeno sučelje koje sadrži metodu unosSoftwarea.
 */
public sealed interface Online permits Ispit{

        void unosSoftwarea(Scanner unos);

}
