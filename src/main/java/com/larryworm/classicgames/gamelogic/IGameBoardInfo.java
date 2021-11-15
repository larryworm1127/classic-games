package com.larryworm.classicgames.gamelogic;

import java.util.List;

public interface IGameBoardInfo<T> {

    T getEmpty();

    List<T> getValidBoardValues();

    boolean isValidValue(T value);
}
