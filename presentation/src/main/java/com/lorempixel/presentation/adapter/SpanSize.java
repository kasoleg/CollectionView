package com.lorempixel.presentation.adapter;

public class SpanSize {
    private final int mRows;
    private final int mColumns;
    private final int mRow;
    private final int mColumn;

    public SpanSize(int rows, int columns, int row, int column) {
        this.mRows = rows;
        this.mColumns = columns;
        mRow = row;
        mColumn = column;
    }

    public int getRows() {
        return mRows;
    }

    public int getColumns() {
        return mColumns;
    }

    public int getRow() {
        return mRow;
    }

    public int getColumn() {
        return mColumn;
    }
}
