package com.DCB;

import java.util.Scanner;

/*
 * Class:       CS 4308 Section 2
 * Term:        Fall 2019
 * Name:        Robert, Chris, James
 * Instructor:   Deepa Muralidhar
 * Project:  Deliverable 1 Scanner - Java
 */

public class ScriptReader {
    private final Scanner scanner;

    public ScriptReader(Scanner scanner) {
        this.scanner = scanner;
        scanner.useDelimiter("");
    }

    public char getNextChar() {
        return scanner.next().charAt(0);
    }


    public boolean hasNextChar() {
        return scanner.hasNext();
    }
}
