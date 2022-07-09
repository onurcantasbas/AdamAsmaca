package com.company;


import java.util.List;

public class PlayerSlot {

    private String playerName;

    public void requestLetterCount(int lowest, int highest) {
    }

    public int setLetterCount(int lowest, int highest) {
        return 0;
    }

    public String setWord(List<String> filteredDictionary) {
        return null;
    }

    public void requestPrediction() {
    }

    public char predict(List<Character> letterDictionary, List<String> filteredDictionary) {
        return 'a';
    }

    public List<Object> checkPrediction(String word, char prediction, char[] table) {
        return null;
    }

    public int sendPredictionCheckingResults(int successCount, int tryLimit) {
        return 0;
    }

    public void updateList(char prediction, int successCount, List<Integer> indexes, List<String> filteredDictionary) {
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

}
