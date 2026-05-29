package com.student;

public enum YearLevel {

    FIRST_YEAR,
    SECOND_YEAR,
    THIRD_YEAR,
    FOURTH_YEAR;

    @Override
    public String toString() {

        return switch (this) {

            case FIRST_YEAR -> "1st Year";
            case SECOND_YEAR -> "2nd Year";
            case THIRD_YEAR -> "3rd Year";
            case FOURTH_YEAR -> "4th Year";
        };
    }

    public static YearLevel fromDisplayText(String text) {
        if (text == null) {
            return null;
        }

        for (YearLevel level : values()) {
            if (level.toString().equals(text)) {
                return level;
            }
        }

        return null;
    }
}
