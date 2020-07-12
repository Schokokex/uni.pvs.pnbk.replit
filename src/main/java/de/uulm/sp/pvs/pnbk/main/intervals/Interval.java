package de.uulm.sp.pvs.pnbk.main.intervals;

import java.util.Iterator;
import java.util.regex.Pattern;

public class Interval implements Iterable<Integer> {
	final private int start;
	final private int end;
	final private int stepSize;

	/**
	 * (10,20,0) invalid
	 * 
	 * (20,20,0) valid
	 * 
	 * (10,20,-1) invalid
	 * 
	 * (20,10,-1) invalid
	 * 
	 * @param intervalString string with the pattern (#,#,#)
	 * @throws IllegalArgumentException string does not match the pattern or has bad
	 *                                  numbers
	 */
	public Interval(String intervalString) throws IllegalArgumentException {
		final var pattern = Pattern.compile("\\((\\d+),(\\d+),(\\d+)\\)");
		final var matcher = pattern.matcher(intervalString);
		if (!matcher.find()) {
			throw new IllegalArgumentException("Interval doesnt match (#,#,#)");
		}

		this.start = Integer.parseInt(matcher.group(1));
		this.end = Integer.parseInt(matcher.group(2));
		this.stepSize = Integer.parseInt(matcher.group(3));

		if (0 > stepSize) {
			throw new IllegalArgumentException("Step size must not be smaller than 0");
		}
		if ((0 == stepSize) && (start != end)) {
			throw new IllegalArgumentException("Step size must not be 0");
		}

	}

	@Override
	final public String toString() {
		return String.format("(%d,%d,%d)", start, end, stepSize);
	}

	@Override
	public Iterator<Integer> iterator() {
		return new Iterator<Integer>() {
			private Integer iterator = null;

			@Override
			public boolean hasNext() {
				if (null == iterator && start<=end) {
					return true;
				}
				if (0 == stepSize) {
					return false;
				}
				if ((iterator + stepSize) > end) {
					return false;
				}
				return true;
			}

			@Override
			public Integer next() {
				if (null == iterator) {
					iterator = start;
				} else {
					iterator += stepSize;
				}
				if (iterator > end) {
					iterator = null;
				}

				return iterator;
			}
		};
	}

}
