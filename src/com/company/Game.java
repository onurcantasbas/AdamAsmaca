package com.company;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Game {
    private final Scanner scan = new Scanner(System.in);
    private PlayerSlot p1 = new PlayerSlot();
    private PlayerSlot p2 = new PlayerSlot();
    DictionaryInit dictionaryInit = new DictionaryInit();
    private String word;
    int successCount;
    int totalSuccess;
    List<Object> returned = new ArrayList<>();

    public Game() throws IOException {

        dictionaryInit.getCleanDictionary();
        dictionaryInit.getLetterDictionary();
    }

    public void gameInit() throws IOException {
        playerSelectMenu();
        wordSettingProcesses();
        char[] table = new char[word.length()];
        fillTableNull(table);  // başlangıçta tabloyu kelime uzunluğu kadar ________ ile doldurur
        predictionProcesses(word, table);
    }

    private void playerSelectMenu() {  // Player 1 ve Player 2 seçiminin yapıldığı metod. Kullanıcı seçimine göre p1 ve p2 atanası dinamik olarak yapılmakta.
        boolean continueKey = false;
        System.out.println("p1:Kelimeyi Seçen | p2:Tahmin eden");
        System.out.println("Seçim yapınız (1/2): ");
        while (!continueKey) {
            int key = scan.nextInt();
            switch (key) {
                case 1 -> {
                    p1 = new Human();
                    p2 = new Computer();
                    p1.setPlayerName("Human");
                    p2.setPlayerName("Computer");
                    continueKey = true;
                }
                case 2 -> {
                    p1 = new Computer();
                    p2 = new Human();
                    p1.setPlayerName("Computer");
                    p2.setPlayerName("Human");
                    continueKey = true;
                }
                default -> System.out.println("Yanlış giriş yaptınız tekrar deneyin(1/2): ");
            }
        }
    }

    private void wordSettingProcesses() throws IOException {  // oyunun başladığında player 2 nin belirlediği uzunluğa göre player 1'in kelime seçtiği, kelime seçim işlemlerinin çağırıldığı metod.
        p1.requestLetterCount(lowestLenght(), highestLenght());
        int letterCount = p2.setLetterCount(lowestLenght(), highestLenght());
        word = p1.setWord(dictionaryInit.getFilteredDictionary(letterCount));
    }

    private void predictionProcesses(String word, char[] table) {
        for (int limit = 0; limit < 10; limit++) {  // player2 nin 10 tahmin hakkı olacağı için limit<10
            successCount = 0; // her tahminin başarı sayısını tutmakta
            if (word.length() != totalSuccess) {        //70.satır açıklama --> Doğru tahmin edilen harf sayısı metod içinde totalSuccess de tutulmakta bilinen harf sayısı
                System.out.println("Kelime : " + word); //70.satır açıklama --> kelimenin uzunluğuna eşit olana yada player2 nin tahmin hakkı dolana kadar
                p1.requestPrediction();                 //70.satır açıklama --> tahmin etme işlemleri devam edicek.
                char prediction = p2.predict(dictionaryInit.letterDictionary, dictionaryInit.filteredDictionary);
                returned = p1.checkPrediction(word, prediction, table); // Player1'in chechPrediction metodundan dönen değikenler returned <object> arraylistine atandı
                successCount = (int) returned.get(0);   // döndürülen ilk değer successCount: kelimede başarıyla tahmin edilen harf sayısı.[ör: tahmin a kelimede 2 tane a varsa succesCount =2].
                totalSuccess = successCount + totalSuccess; // ilk tahminin başarı sonuçları totalSucccesle toplanıp totalSucces'e atandı
                limit = p1.sendPredictionCheckingResults(successCount, limit); // Tahmin başarılı olduğunda, deneme hakkının azalmaması için limit p1.sendPredictionResults metodunda azalılmakta,[Ör: successCount !=0 ise limit--]
                table = (char[]) returned.get(1); // bulunan kelimeler ___a___ gibi formatda yazdırmak, oyun takibini kolaylaştırmak için checkPrediction metodunda table charlisti dolduruldu.
                List<Integer> indexes = new ArrayList<>();
                indexes = (List<Integer>) returned.get(2); // Bilgisayarın bir sonraki tahmin için bulunan harflere ve harflerin yerlerine göre dictionary üzerinde filitreleme yapması için bulunan harflerin yerleri indexes arraylistine p1.checkPrediction metodunda atandı.
                p2.updateList(prediction, successCount, indexes, dictionaryInit.filteredDictionary); // bulunan harfler ve yerlerine göre dictionary list güncellenmekte.
            }
        }
        isWinner(); // Tüm harfler bulunduysa p2 , bulunamadıysa p1'in kazandığını yazdırmakta.
    }

    private void isWinner() { // oyunun sonucunu yazdırır.
        if (totalSuccess == word.length()) {
            System.out.println(p2.getPlayerName() + " kazandı " + p1.getPlayerName() + " kaybetti.");
        } else System.out.println(p2.getPlayerName() + " kaybetti" + p1.getPlayerName() + " kazandı.");
    }

    private int highestLenght() throws IOException { // harf sayisi aralığını belirlemek için cleanDictionary listesindeki en uzun kelimenin uzunluğunu döndürür.
        int highest = 0;
        for (int i = 0; i < dictionaryInit.getCleanDictionary().size(); i++) {
            if (highest < dictionaryInit.getCleanDictionary().get(i).length()) {
                highest = dictionaryInit.getCleanDictionary().get(i).length();
            }
        }
        return highest;
    }

    private int lowestLenght() throws IOException { // harf sayisi aralığını belirlemek için cleanDictionary listesindeki en kısa kelimenin uzunluğunu döndürür.
        int lowest = dictionaryInit.getCleanDictionary().get(0).length();
        for (int i = 0; i < dictionaryInit.getCleanDictionary().size(); i++) {
            if (lowest > dictionaryInit.getCleanDictionary().get(i).length()) {
                lowest = dictionaryInit.getCleanDictionary().get(i).length();
            }
        }
        return lowest;
    }

    private void fillTableNull(char[] table) {
        Arrays.fill(table, '_');

    }
}
