package com.globalcollect.gateway.sdk.client.android.sdk.formatter;

import java.io.Serializable;

import com.globalcollect.gateway.sdk.client.android.sdk.model.FormatResult;

/**
 * Contains util methods for stringformatting
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class StringFormatter implements Serializable {
	
	private static final long serialVersionUID = -365568554479175934L;
	
	
	
	/**
	 * Will apply a mask a returns formatresult
	 * @param mask
	 * @param value
	 * @param oldValue
	 * @param cursorIndex
	 * @return formatresult
	 */
	public FormatResult applyMask(String mask, String value, String oldValue, Integer cursorIndex) {
		
		if (mask == null || value == null){
			return null;
		}
		
		// if users press the backspace button
		if (oldValue.length() > value.length()) {
			return new FormatResult(value, null);
		}
		
		// Get value and cursorIndex
		value = (String) getMaskOrGetCursorIndex(mask, value, cursorIndex, true);
		cursorIndex = (Integer) getMaskOrGetCursorIndex(mask, value, cursorIndex, false);

		return new FormatResult(value, cursorIndex);
	}
	
	
	
	/**
	 * Method calculates the masked value and the cursorIndex
	 * @param mask
	 * @param value
	 * @param cursorIndex
	 * @param getMask
	 * @return mask or cursorIndex
	 */
	private Object getMaskOrGetCursorIndex(String mask, String value, Integer cursorIndex, Boolean getMask){
		
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
					index ++;
					
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
	 * Makes a masked value of a value
	 * @param mask
	 * @param value
	 * @return masked value
	 */
	public String applyMask(String mask, String value){
		
		return (String) getMaskOrGetCursorIndex(mask, value, 0, true);
	}
	
	/**
	 * Removes the mask on a given value
	 * @param mask
	 * @param value
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