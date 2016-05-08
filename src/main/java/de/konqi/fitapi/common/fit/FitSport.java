package de.konqi.fitapi.common.fit;

public enum FitSport {
    GENERIC(0),
    RUNNING(1),
    CYCLING(2),
    SWIMMING(5),
    TRAINING(10),
    WALKING(11),
    HIKING(17),
    HORSEBACK_RIDING(27),
    INLINE_SKATING(30),
    ROCK_CLIMBING(31),
    ALL(254),
    INVALID(255);

    protected int value;

    FitSport(int value) {
        this.value = value;
    }

    public static FitSport getByValue(int value) {
        for (FitSport type : FitSport.values()) {
            if (value == type.value)
                return type;
        }

        return FitSport.INVALID;
    }
}