package com.lorempixel.presentation.adapter;

public class Span {
    private int mRow = 0;
    private int mColumn = 0;

    public Span(int row, int column) {
        this.mRow = row;
        this.mColumn = column;
    }

    public int getRow() {
        return mRow;
    }

    public int getColumn() {
        return mColumn;
    }
}
