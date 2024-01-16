package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    public int rowPos;
    public int colPos;
    public ChessPosition(int row, int col) {
        rowPos = row;
        colPos = col;
    }

    @Override
    public String toString() {
        return "ChessPosition{"
                + rowPos +
                ", " + colPos +
                ')';
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        //throw new RuntimeException("Not implemented");
        return rowPos;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        //throw new RuntimeException("Not implemented");
        return colPos;
    }

    public boolean isOnBoard(){
        if (rowPos > 0 && rowPos <= 8){
            if (colPos > 0 && colPos <= 8){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPosition that = (ChessPosition) o;
        return rowPos == that.rowPos && colPos == that.colPos;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowPos, colPos);
    }
}
