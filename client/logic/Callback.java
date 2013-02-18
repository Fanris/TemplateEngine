package de.tet.inputmaskTemplate.client.logic;

/**
 * A simple, generic callback-Function.
 * @author Martin predki
 *
 * @param <T> The parameter-type of the callback-function.
 */
public interface Callback<T> {
	/**
	 * The function that is called by the callback.
	 * @param param Could be used to pass some data to the callback-function.
	 */
	public void call(T param);
}
