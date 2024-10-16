package br.encibra.desafio.exceptions;

public class DatabaseException extends  RuntimeException {
    public DatabaseException (String msg) {
        super(msg);
    }
}
