package com.larryworm.classicgames.gamelogic;

import java.util.Arrays;
import java.util.List;

public class TTTBoardInfo implements IGameBoardInfo<TTTState> {

    private static final TTTBoardInfo instance = new TTTBoardInfo();

    @Override
    public TTTState getEmpty() {
        return TTTState.EMPTY;
    }

    @Override
    public List<TTTState> getValidBoardValues() {
        return Arrays.asList(TTTState.values());
    }

    @Override
    public boolean isValidValue(TTTState value) {
        return Arrays.asList(TTTState.values()).contains(value);
    }

    public static TTTBoardInfo getInstance() {
        return instance;
    }
}
