package edu.utexas.ee360p_teamproject;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * Created by aria on 4/22/17.
 */

class ReqHandler {
    private static Object allRooms;

    public static Object getAllRooms(Consumer<ArrayList<String>> consumer) {
        return allRooms;
    }
}
