/*
 * Copyright 2015 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jbpm.designer.client.util;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.regexp.shared.RegExp;
import org.jbpm.designer.client.resources.i18n.DesignerEditorConstants;

public class DataIOEditorNameTextBox extends AbstractValidatingTextBox {

    Set<String> invalidValues = null;
    boolean isCaseSensitive = false;
    String invalidValueErrorMessage = null;

    protected String invalidCharactersInNameErrorMessage = DesignerEditorConstants.INSTANCE.Removed_invalid_characters_from_name();

    // Pattern for valid value
    protected  RegExp regExp = null;

    public DataIOEditorNameTextBox() {
        super();
    }

    /**
     * Sets the invalid values for the TextBox
     *
     * @param invalidValues
     * @param isCaseSensitive
     * @param invalidValueErrorMessage
     */
    public void setInvalidValues(final Set<String> invalidValues, final boolean isCaseSensitive, final String invalidValueErrorMessage) {
        if (isCaseSensitive) {
            this.invalidValues = invalidValues;
        }
        else {
            this.invalidValues = new HashSet<String>();
            for (String value : invalidValues) {
                this.invalidValues.add(value.toLowerCase());
            }
        }
        this.isCaseSensitive = isCaseSensitive;
        this.invalidValueErrorMessage = invalidValueErrorMessage;
    }

    /**
     * Sets the RegExp pattern for the TextBox
     *
     * @param pattern
     * @param invalidCharactersInNameErrorMessage
     */
    public void setRegExp(final String pattern, final String invalidCharactersInNameErrorMessage) {
        regExp = RegExp.compile(pattern);
        this.invalidCharactersInNameErrorMessage = invalidCharactersInNameErrorMessage;
    }

    @Override
    public String isValidValue(final String value, final boolean isOnFocusLost) {
        if (invalidValues != null && !invalidValues.isEmpty()) {
            if (isOnFocusLost) {
                String err = testForInvalidValue(value);
                if (err != null && !err.isEmpty()) {
                    return err;
                }
            }
        }

        if (regExp != null) {
            boolean isValid = this.regExp.test(value);
            if (!isValid) {
                String invalidChars = getInvalidCharsInName(value);
                return invalidCharactersInNameErrorMessage + ": " + invalidChars;
            }
        }

        return null;
    }

    /**
     * Tests whether a value is in the list of invalid values
     *
     * @param value
     * @return error message if value is invalid; otherwise null
     */
    protected String testForInvalidValue(final String value) {
        if (value == null || value.isEmpty() || invalidValues == null) {
            return null;
        }
        String testValue;
        if (!isCaseSensitive) {
            testValue = value.toLowerCase();
        }
        else {
            testValue = value;
        }
        if (invalidValues.contains(testValue)) {
            return invalidValueErrorMessage;
        }
        else {
            return null;
        }
    }

    @Override
    protected String makeValidValue(final String value) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        // It's a known invalid value
        if (testForInvalidValue(value) != null) {
            return "";
        }
        else {
            StringBuilder validValue = new StringBuilder(value.length());
            for (int i = 0; i < value.length(); i++) {
                char c = value.charAt(i);
                if (isValidChar(c)) {
                    validValue.append(c);
                }
            }

            return validValue.toString();
        }
    }


    protected String getInvalidCharsInName(final String value) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        else {
            StringBuilder invalidChars = new StringBuilder(value.length());
            for (int i = 0; i < value.length(); i++) {
                char c = value.charAt(i);
                if (! isValidChar(c)) {
                    invalidChars.append(c);
                }
            }

            return invalidChars.toString();
        }
    }

    protected boolean isValidChar(final char c) {
        if (regExp != null) {
            return regExp.test("" + c);
        }
        else {
            return true;
        }
    }
}