package chess;

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
        return "ChessPosition{" +
                "rowPos=" + rowPos +
                ", colPos=" + colPos +
                '}';
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
}
