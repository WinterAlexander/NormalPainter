package net.jumpai.util.ui.textfield.validated;

import com.badlogic.gdx.utils.Array;

import java.util.function.Consumer;

/**
 * Validates the input of a {@link ValidatedTextField} by outputting a list of
 * errors that happened trying to validate the input.
 * <p>
 * Created on 2019-05-29.
 *
 * @author Alexander Winter
 */
public interface TextFieldValidator
{
	/**
	 * Validates the specified input by returning the list of errors or problems
	 * there is with the specified input. If there's no errors and the input is
	 * valid, an empty list is returned. The result is returned asynchroniously
	 * using a gdx callback.
	 *
	 * @param input input to validate
	 * @param callback to provide errors with the input, or an empty array if no errors
	 */
	void getErrors(String input, Consumer<Array<String>> callback);
}
