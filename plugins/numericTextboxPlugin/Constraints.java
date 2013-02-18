package de.tet.inputmaskTemplate.plugins.numericTextboxPlugin;

/**
 * Enumeration of possible Constraints
 * @author Martin Predki
 *
 */
public enum Constraints {
	INVALID ("INVALID"),
	LOWER_THAN ("LOWER_THAN"),
	LOWER_EQUAL_THAN ("LOWER_EQUAL_THAN"),	
	HIGHER_THAN ("HIGHER_THAN"),
	HIGHER_EQUAL_THAN ("HIGHER_EQUAL_THAN"),
	EQUAL ("EQUAL"),
	LOWER_THAN_IGNORE_UNIT ("LOWER_THAN_IGNORE_UNIT"),
	LOWER_EQUAL_THAN_IGNORE_UNIT ("LOWER_EQUAL_THAN_IGNORE_UNIT"),
	HIGHER_THAN_IGNORE_UNIT ("HIGHER_THAN_IGNORE_UNIT"),
	HIGHER_EQUAL_THAN_IGNORE_UNIT ("HIGHER_EQUAL_THAN_IGNORE_UNIT"),
	EQUAL_IGNORE_UNIT ("EQUAL_IGNORE_UNIT");


	private final String myConstraint;
	
	private Constraints(String constraint) {
		myConstraint = constraint;
	}
	
	@Override
	public String toString() {
		return myConstraint;
	}
	
	public static Constraints lookup(String aString) {
		for (Constraints c : Constraints.values()) {
			if (c.myConstraint.equalsIgnoreCase(aString)) {
				return c;
			}
		}
		return INVALID;
	}
}
