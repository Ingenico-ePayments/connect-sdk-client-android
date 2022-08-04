/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.formatter;

import com.ingenico.connect.gateway.sdk.client.android.sdk.model.FormatResult;

import java.io.Serializable;

/**
 * Contains util methods for stringformatting
 */
public class StringFormatter implements Serializable {

	private static final long serialVersionUID = -365568554479175934L;

	private enum ChangeType {
		ADDED,
		REMOVED,
		REPLACED
	}

	/**
	 * Applies a mask to a String, based on the previous value and splice information. The result
	 * is a FormatResult object, that holds the masked String and the new cursor index. This masker is meant
	 * for user input fields, where users are still entering their information.
	 *
	 * @param mask the mask that will be applied. Characters to be masked should be surrounded by double accolades,
	 *             mask characters should be between accolade-blocks. E.g. a mask for an expiry date would look like
	 *             this: {{99}}/{{99}}.
	 * @param value the value that the mask will be applied to.
	 * @param oldValue the value that was in the edit text, before characters were removed or added.
	 * @param start the index of the start of the change.
	 * @param count the number of characters that were removed.
     * @param after the number of characters that were added.
     */
	public FormatResult applyMask(String mask, String value, String oldValue, int start, int count, int after) {

		if (mask == null || value == null || oldValue == null){
			return null;
		}

		FormatResult result = null;

		// Determine whether character(s) are added, removed or replaced and act accordingly.
		switch (addedRemovedOrReplaced(count, after)) {
			case ADDED: result = determineFormatResultADDED(mask, value, oldValue, after, start); break;
			case REMOVED: result = determineFormatResultREMOVED(mask, value, start); break;
			case REPLACED: result = determineFormatResultREPLACED(mask, value, oldValue, after, start); break;
		}

		return result;
	}

	// Determines what has happened to the text in the editText, using the information that
	// TextWatcher::beforeTextChanged provides.
	private ChangeType addedRemovedOrReplaced(int count, int after) {
		// after > 0 if characters have been added to the string.
		// count > 0 if characters have been removed from the string.
		// both should never be < 0.
		if (count < 0 || after < 0) {
			throw new IllegalArgumentException("Error when determining mask; count and/or after may not be < 0");
		}
		if (after != 0 && count == 0) {
			return ChangeType.ADDED;
		} else if (after == 0 && count != 0) {
			return ChangeType.REMOVED;
		} else {
			return ChangeType.REPLACED;
		}
	}

	private FormatResult determineFormatResultADDED(String mask, String value, String oldValue, int numAddedCharacters, int previousCursorIndex) {

		String formattedValue = "";
		int isInMask = 0;
		int valueIndex = 0;
		int oldValueIndex = 0;
		boolean cursorHasMoved = false;

		int newCursorIndex = previousCursorIndex;
		int numNewCharactersAdded = 0;
		boolean cursorMoving = false;

		// loop over the mask characters
		for(Integer maskIndex = 0; maskIndex < mask.length(); maskIndex++) {
			Character maskCharacter = mask.charAt(maskIndex);
			if (maskCharacter.equals('}')) {
				isInMask--;
			} else if (maskCharacter.equals('{')) {
				isInMask++;
			} else if (isInMask == 2) {

				if (valueIndex >= value.length()) {
					break;
				}

				// If the valueIndex passes the previousCursorLocation, we start moving the cursor
				// so it will move along with the characters that are being added.
				if (valueIndex == previousCursorIndex) {
					cursorMoving = true;
				}

				// If the numNewCharactersAdded counter equals the number of characters that were
				// added by the user, the cursor should stop moving
				if (numNewCharactersAdded == numAddedCharacters) {
					cursorMoving = false;
				}

				// If the character is a valid character, we add it to the formattedValue and update
				// relevant indices.
				if ((Character.toString(value.charAt(valueIndex)).
						matches(convertMaskCharacterToRegex(maskCharacter.toString())))) {

					formattedValue += value.charAt(valueIndex);

					if (cursorMoving) {
						newCursorIndex++;
						// If the cursor is moving, this implicitly means that we are adding new
						// charachters that were not in the oldValue, so we have to update the
						// numNewCharactersAdded counter
						numNewCharactersAdded++;
					}

				} else {
					// An invalid character had been inserted here; ignore it, but do not move
					// further through the mask
					maskIndex--;
				}

				// If the cursor is not moving, this means that the new numbers are already added,
				// or that they still need to be added. Either way we need to move along both the
				// old value and the new value
				if (!cursorMoving) {
					oldValueIndex++;
				}
				valueIndex++;

			} else if (isInMask == 0) {

				// Add the mask character that belongs here
				formattedValue += maskCharacter;

				// If the valueIndex goes past the length of the value, we are done with formatting
				// and we can break the loop.
				if (valueIndex >= value.length()) {
					if (cursorMoving) {
						newCursorIndex++;
					}
					break;
				}

				// If the valueIndex passes the previousCursorLocation, we start moving the cursor
				// so it will move along with the characters that are being added.
				if (valueIndex == previousCursorIndex) {
					cursorMoving = true;
				}

				// If the cursor is moving, update its location
				if (cursorMoving) {
					newCursorIndex++;
				}

				// If the numNewCharactersAdded counter equals the number of characters that were
				// added by the user, the cursor should stop moving
				if (numNewCharactersAdded == numAddedCharacters) {
					cursorMoving = false;
				}

				// If no character was added yet and the characters are equal to the mask character
				// here, this means that the indices can be updated.
				if (valueIndex == oldValueIndex &&
						value.charAt(valueIndex) == maskCharacter &&
						oldValue.charAt(oldValueIndex) == maskCharacter) {
					valueIndex++;
					oldValueIndex++;
				}

			} else {
				throw new IllegalArgumentException("Error while masking inputText; there seems to be an invalid mask.");
			}
		}
		return new FormatResult(formattedValue, newCursorIndex);
	}

	private FormatResult determineFormatResultREMOVED(String mask, String value, int newCursorLocation) {

		// Variables used to determine the new Formatted value
		String formattedValue = "";
		int isInMask = 0;
		int index = 0;

		// Variables used to determine the new Cursor index
		int newCursorIndex = newCursorLocation;


		// loop over the mask characters
		for(Integer i = 0; i < mask.length(); i++) {
			Character maskCharacter = mask.charAt(i);
			if (maskCharacter.equals('}')) {
				isInMask--;
			} else if (maskCharacter.equals('{')) {
				isInMask++;
			} else if (isInMask == 2) {

				// If the index reached the end of the value; we are done
				if (index >= value.length()) {
					break;
				}

				// If the character is valid, add it to the new value and update the index
				if ((Character.toString(value.charAt(index)).
						matches(convertMaskCharacterToRegex(maskCharacter.toString())))) {

					formattedValue += value.charAt(index);

				} else {
					// An invalid character has been detected; we have to go one step back in the
					// mask in order to mask the valid characters correctly.
					i--;
				}

				index++;

			} else if (isInMask == 0) {

				if (index >= value.length()) {
					// If the end of value is reached we can break the loop, but not before adding
					// the mask-character that belongs at the end.
					formattedValue += maskCharacter;
					break;
				} else if (Character.toString(value.charAt(index)).equals(maskCharacter.toString())) {
					// If the masked character was already in the old string add it to the formatted
					// result and update the index.
					formattedValue += value.charAt(index);
					index++;
				} else {
					// If the masked character was not already in the old value, add it. Do not
					// update the index, but do move the cursor by 1.
					formattedValue += maskCharacter;
				}
			} else {
				throw new IllegalStateException("Error while masking inputText; there seems to be an invalid mask.");
			}
		}

		return new FormatResult(formattedValue, newCursorIndex);
	}

	private FormatResult determineFormatResultREPLACED(String mask, String value, String oldValue, int numAddedCharacters, int previousCursorIndex) {

		FormatResult intermediateResult = determineFormatResultREMOVED(mask, value, previousCursorIndex);

		FormatResult finalResult = determineFormatResultADDED(mask, intermediateResult.getFormattedResult(), oldValue, numAddedCharacters, previousCursorIndex);

		return finalResult;
	}


	/**
	 * Will apply a mask and returns formatresult.
	 * @param mask the mask that will be applied
	 * @param value the value that the mask should be applied to
	 * @param oldValue the value that was in previously in the editText
	 * @param oldCursorIndex the cursorIndex before applying the changes
	 * @return formatresult, containing the formatted value and the new cursor index
	 * @deprecated the masking behaviour that is provided by {@link #applyMask(String, String, String, int, int, int)} provides a better user experience.
	 */
	@Deprecated
	public FormatResult applyMask(String mask, String value, String oldValue, Integer oldCursorIndex) {

		if (mask == null || value == null || oldValue == null){
			return null;
		}

		// if users press the backspace button
		if (oldValue.length() > value.length()) {
			return new FormatResult(value, null);
		}

		// Get value and new CursorIndex
		String maskedValue = (String) getMaskOrGetCursorIndex(mask, value, oldCursorIndex, true);
		int newCursorIndex = (Integer) getMaskOrGetCursorIndex(mask, value, oldCursorIndex, false);

		return new FormatResult(maskedValue, newCursorIndex);
	}



	/**
	 * Method calculates the masked value and the cursorIndex
	 * @param mask
	 * @param value
	 * @param cursorIndex
	 * @param getMask
	 * @return mask or cursorIndex
	 */
	private Object getMaskOrGetCursorIndex(String mask, String value, int cursorIndex, boolean getMask){

		if (mask == null || value == null) {
			return null;
		}

		String newValue = "";

		Integer isInMask = 0;
		Integer index = 0;

		// loop over the mask characters
		for(Integer i = 0; i < mask.length(); i++){
			Character maskCharacter = mask.charAt(i);
			if (maskCharacter.equals('}')) {
				isInMask--;
			} else if (maskCharacter.equals('{')) {
				isInMask++;
			} else {

				// if character between {{ and }}, add character at the same position of the value
				if(isInMask == 2){

					// if the index is higher than the value which has to be masked
					// then break function
					if(index >= value.length()){
						break;
					}


					// If this is a valid character, add it to the formattedValue
					if ((Character.toString(value.charAt(index)).
							matches(convertMaskCharacterToRegex(maskCharacter.toString())))) {

						newValue += value.charAt(index);

					} else {

						// if characters are not equal, then go 1 step back in the mask (there has been added or removed something)
						i--;
					}
					index++;

				// if character between {{}} blocks, then add mask values
				} else if (isInMask == 0){

					if(index < value.length()){
						// check whether cursor has to be moved 1 place
						if (index == cursorIndex){
							cursorIndex ++;
						}

						//check whether index has to be moved 1 place
						if(value.charAt(index) == maskCharacter){
							index ++;
						}
					}

					newValue += maskCharacter;

					if(index >= value.length()){
						break;
					}
				}
			}
		}

		if(getMask){
			return newValue;
		} else {
			return cursorIndex;
		}
	}

	/**
	 * Applies the mask to the value. This method can be used if you want to mask a static String
	 * @param mask the mask that should be applied to value
	 * @param value the String that the mask is applied to
	 * @return the masked value
	 */
	public String applyMask(String mask, String value){

		return (String) getMaskOrGetCursorIndex(mask, value, 0, true);
	}

	/**
	 * Removes the mask on a given value
	 * @param mask the mask that is applied to the value
	 * @param value the value of which the mask will be removed
	 * @return the unMasked value
	 */
	public String removeMask(String mask, String value){

		if (mask == null || value == null) {
			return null;
		}

		// First apply Mask on the value
		// So this method will work if you put in any value (masked or unmasked)
		value = applyMask(mask, value);

		String newValue = "";

		Integer isInMask = 0;
		Integer index = 0;

		// loop over the mask characters
		for(Integer i = 0; i < mask.length(); i++){
			Character maskCharacter = mask.charAt(i);
			if (maskCharacter.equals('}')) {
				isInMask--;
			} else if (maskCharacter.equals('{')) {
				isInMask++;
			} else {
				// if between {{ and }}, add position of value to newValue
				if (isInMask == 2) {
					if (index >= value.length()){
						break;
					}
					newValue += value.charAt(index);
				}
				index ++;
			}

		}


		return newValue;
	}


	/**
	 * Replaces a maskcharacter with a regex so it can be used for matching
	 * @param maskCharacter
	 * @return the regexversion of the maskCharacter
	 */
	private String convertMaskCharacterToRegex(String maskCharacter) {

		String result = maskCharacter.replaceAll("9", "\\\\d");
		result 		  = result.replaceAll("a", "[a-z]");
		result 		  = result.replaceAll("A", "[A-Z]");
		result		  = result.replaceAll("\\*", ".*");
		return result;
	}

}
