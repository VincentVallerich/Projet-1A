package ensisa.group5.confined.exceptions;

import androidx.annotation.Nullable;

/**
 * Author VALLERICH Vincent on 05-06-2020
 */

public class DataBaseException extends Exception {
    public DataBaseException() {
        System.out.println("ERROR DATABASE ACCESS");
    }
}
