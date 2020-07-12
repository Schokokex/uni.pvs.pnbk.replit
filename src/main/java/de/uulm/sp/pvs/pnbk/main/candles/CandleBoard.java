package de.uulm.sp.pvs.pnbk.main.candles;

import java.util.ArrayList;

public class CandleBoard {

	final static char DARK = 'e';
	final static char CANDLE = 's';
	final static char WALL = 'w';
	final static char BRIGHT = 'l';

	private final ArrayList<ArrayList<Character>> board = new ArrayList<ArrayList<Character>>();

	/**
	 * 
	 * @param boardString a String with the board and light source.
	 */
	public CandleBoard(String boardString) {
		final var candles = new ArrayList<Field>();
		final var darks = new ArrayList<Field>();

		final var rows = boardString.split("\n");
		for (int r = 0; r < rows.length; r++) {

			board.add(new ArrayList<Character>());

			final var row = rows[r].toCharArray();
			for (int c = 0; c < row.length; c++) {

				final char field = row[c];

				board.get(r).add(field);

				if (CANDLE == field) {
					candles.add(new Field(r, c));
				} else if (DARK == field) {
					darks.add(new Field(r, c));
				}
			}
		}

		for (Field dark : darks) {
			for (Field candle : candles) {
				if (isIn2FieldsRange(dark, candle) && !isBetweenFieldWall(dark, candle)) {
					board.get(dark.x).set(dark.y, BRIGHT);
				}
			}
		}

	}

	// x = r, y = c

	private boolean isBetweenFieldWall(Field a, Field b) {
		final var halfFieldRelative = a.sub(b).getMagicHalf();
		final var halfFieldAbsolute = b.add(halfFieldRelative);
		return (WALL == board.get(halfFieldAbsolute.x).get(halfFieldAbsolute.y));
	}

	@Override
	public String toString() {

		final var rowList = new ArrayList<String>();
		for (ArrayList<Character> row : board) {
			rowList.add(row.toString().replaceAll("[\\], \\[]", ""));
		}

		final var out = String.join("\n", rowList);

		return out;
	}

	private static boolean isIn2FieldsRange(Field a, Field b) {
		if (2 < Math.abs(a.x - b.x)) {
			return false;
		}
		if (2 < Math.abs(a.y - b.y)) {
			return false;
		}
		return true;
	}

}
