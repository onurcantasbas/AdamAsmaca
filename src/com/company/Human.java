package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Human extends PlayerSlot {

    Scanner scan = new Scanner(System.in);
    Random rnd = new Random();
    List<Object> willReturn = new ArrayList<>();

    @Override
    public void requestLetterCount(int lowest, int highest) {
    }

    @Override
    public int setLetterCount(int lowest, int highest) {

        return scan.nextInt();
    }

    @Override
    public String setWord(List<String> filteredDictionary) {
        String word = "";
        boolean control = true;
        System.out.println(" Belirlenecek olan kelimenin random olarak atanması için 1");
        System.out.println(" Liste içinden seçmek için 2 ");
        while (control) {
            int key = scan.nextInt();
            switch (key) {
                case 1 -> {  //  Bilgisayarın seçtiği, harf sayısına göre filtrelenmiş dictionary listesinden rastgele kelime seçer.
                    int rndIndex = rnd.nextInt(0, filteredDictionary.size());
                    word = filteredDictionary.get(rndIndex);
                    control = false;
                }
                case 2 -> {
                    for (int i = 0; i < filteredDictionary.size(); i++) {//Bilgisayarın seçtiği, harf sayısına göre filtrelenmiş dictionary listesi içindeki kelimeleri yazdırır. Kullanıcı indekse göre kelime seçer.
                        System.out.println(i + ":" + filteredDictionary.get(i));
                    }
                    System.out.println("Seçmek istediğiniz kelimenin indeksini yazınız: ");
                    int key2 = scan.nextInt();
                    word = filteredDictionary.get(key2);
                    control = false;
                }
                default -> System.out.println(" Yanlış giriş yaptınız tekrar deneyin.(1/2)");
            }
        }
        return word;
    }

    @Override
    public void requestPrediction() {
    }

    @Override
    public char predict(List<Character> letterDictionary, List<String> filteredDictionary) {
        char prediction = scan.next().charAt(0);                                //kullanıcıdan harf tahmini alır.
        if (!letterDictionary.contains(Character.valueOf(prediction))) {        //her tahmin esnasında tahmin letterDictionary'da varmı diye kontrol edilir.
            System.out.println("Hatalı giriş yaptınız yada aynı harfi tekrar girdiniz."); //tahmin letterDictionary'de yoksa hata mesajı döndürür ve tahmin null atanır.
            prediction = ' ';
        } else letterDictionary.remove(Character.valueOf(prediction)); // eğer tahmin edilen harf listede mevcut ise harf listeden silinir ve tahmin kullanıcı girişinden geldiği gibi döndürülür
        return prediction;                                             // bir harf birden fazla kez girilemez.
    }

    @Override
    public List<Object> checkPrediction(String word, char prediction, char[] table) {
        willReturn.clear();
        int successCount = 0;
        List<Integer> indexes = new ArrayList<>();
        ///////////////////////////////////////////////////////
        for (int i = 0; i < word.length(); i++) {           //
            if (prediction == word.charAt(i)) {             //
                indexes.add(i + 1);                         //----> Bu kısım bilgisayarın tahminine göre kullanıcıdan beklenen değerleri hesaplar
            }                                               //----> Test aşamasında kolaylık sağlaması için yazıldı.
        }                                                   //
        System.out.println("Harflerin Sırası= " + indexes); //
        ///////////////////////////////////////////////////////
        indexes.clear();
        char answer = scan.next().charAt(0);  // Bilgisayarın bildim mi ? sorusuna kullanıcıdan e/h girişi
        if (answer == 'e') {
            System.out.print("kaç harf bildim: ");
            successCount = scan.nextInt();    // Bilgisayarın kaç harfi bildiğini kullanıcıdan alır
            for (int i = 0; i < successCount; i++) {   // Bildiği harf kadar tekrar eder
                System.out.print((i + 1) + ".harfin sırası(indeks değil sıra): ");
                int a = scan.nextInt(); // harflerin kelime içindeki indeksleri kullanıcıdan alınır.
                table[a - 1] = prediction; // tablo doldurulur.
                indexes.add(a - 1); //Bilinen harfin indeksleri indexes listesine ekler
            }
        }
        willReturn.add(successCount);  // tüm değerleri döndürmek için successCount , table, indexes döndürülecek olan object list willReturn'e eklenir ve döndürülür
        willReturn.add(table);
        willReturn.add(indexes);
        System.out.println(table);
        return willReturn;
    }

    @Override
    public int sendPredictionCheckingResults(int succesCount, int limit) {//successCount'un değerine göre(tahmin doğrıysa 0'dan büyük,tahmin yanlışsa 0'dan küçük) ekrana yazdırma işlemleri yapar  ve
        if (succesCount != 0) {                                           //tahmin doğruyken tahmin hakkının azalmaması için game classında bulunan predictionProcesses metodundaki for'un sayacını azalır.
            limit--;
        } else System.out.println((9 - limit) + "  Hakkım kaldı  ");
        return limit;
    }

    public void updateList(char prediction, int successCount, List<Integer> indexes, List<String> filteredDictionary) {
    }
}
