package de.uulm.sp.pvs.pnbk.main.candles;

public class Field {
	public final int x;
	public final int y;

	public Field(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Field getMagicHalf() {
		final var tempX = (int) (this.x + Math.signum(this.x)) / 2;
		final int tempY = (int) (this.y + Math.signum(this.y)) / 2;
		return new Field(tempX, tempY);
	}

	public Field add(Field f) {
		return new Field(x + f.x, y + f.y);
	}

	public Field sub(Field f) {
		return new Field(x - f.x, y - f.y);
	}
}
