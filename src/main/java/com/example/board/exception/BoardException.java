package com.example.board.exception;

import lombok.Getter;


    public abstract class BoardException extends RuntimeException{
        public BoardException(String message) {
            super(message);
        }
        public abstract int getStatusCode();


    }
