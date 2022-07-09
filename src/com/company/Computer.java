package com.company;


import java.util.*;

public class Computer extends PlayerSlot {
    Random rnd = new Random();
    Map<Character, Integer> amount = new HashMap<>();
    boolean isUse = false;
    List<Object> willReturn = new ArrayList<>();

    @Override
    public void requestLetterCount(int lowest, int highest) {
        System.out.println("Harf Sayısı Belirleyin(" + lowest + " - " + highest + " arası)");
    }

    @Override
    public int setLetterCount(int lowest, int highest) { // Bilgisayar rastgele kelime uzunluğu seçer ve döndürür.
        int letterCount = rnd.nextInt(lowest, highest);
        System.out.println("Harf sayısını belirliyorum: " + letterCount);
        return letterCount;
    }

    @Override
    public String setWord(List<String> filteredDictionary) {  //Bilgisayar, kelime uzunluğuna göre filtrelenmiş Dictionary listesinden rastgele kelime seçer.
        int rndIndex = rnd.nextInt(1, filteredDictionary.size());
        return filteredDictionary.get(rndIndex);
    }

    @Override
    public void requestPrediction() {
        System.out.println("Harf tahmin edin: ");
    }

    @Override
    public char predict(List<Character> letterDictionary, List<String> filteredDictionary) {
        if (!isUse) {
            getLetterAmountInList(letterDictionary, filteredDictionary); // Bilgisayar ilk tahminini yaparken listedeki kelimelerde en çok hangi harf olduğuna bakar en çok hangi harf kelimelerin içeriğinde bulunuyor ise o harf ile tahmin etmeye başlar.
            isUse = true;                                                // Örnek 160 kelime a içermekte 100 kelime b içermekte ilk tahmin a olucaktır.
        } else {
            amount.clear();                                              // Öncesinde 1 kere tahmin yapmışsa harflerin liste içindeki tekrar sayısını tutan hashMap sıfırlanır.
            amount = getLetterAmountInList(letterDictionary, filteredDictionary); // Sonrasında yeniden harflerin skorunu tutmaya başlar. predict metodundan her çıkıldığında liste güncellendiği için (updateList() metodunda) yeni skorlar hashMap'e depolanır.
        }
        char prediction = amount.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey(); // en yüksek değerli key'i döndürür. Hashmapdeki keyler a,b,c,d.... harf listesi.
        amount.remove(prediction);    // 1 kere tahminde bulunulan char keyi hashmapden kaldırır.
        System.out.println("Harf tahmin ediyorum: " + prediction);
        System.out.println("bildim mi? (e/h): ");
        letterDictionary.remove(Character.valueOf(prediction)); // tahminde bulunulan harfi harf listesinden kaldırır.
        return prediction;
    }

    @Override
    public List<Object> checkPrediction(String word, char prediction, char[] table) {
        willReturn.clear();
        int successCount = 0;
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < word.length(); i++) {  // kelimenin uzunluğu kadar tekrar eder..
            if (prediction == word.charAt(i)) {
                successCount++;      // kelimenin indeksi tahmine eşitse successCount artar.
                table[i] = prediction;  // tahmin tabloya eklenir.
                indexes.add(i);         // Bir sonraki tahmin için filtreleme yapılacağından bulunan harfin kelime üstündeki yerleri indexes arraylistine eklenir ve döndürülür.
            }
        }
        willReturn.add(successCount);   // çoklu return için tüm dönmesi gereken değişkenler object tutan arrayliste eklendi.
        willReturn.add(table);
        willReturn.add(indexes);
        System.out.println(table);
        return willReturn;
    }

    @Override
    public int sendPredictionCheckingResults(int succesCount, int limit) {   // successCount'un değerine göre(tahmin doğrıysa 0'dan büyük,tahmin yanlışsa 0'dan küçük) ekrana yazdırma işlemleri yapar  ve
        if (succesCount != 0) {                                              // tahmin doğruyken tahmin hakkının azalmaması için game classında bulunan predictionProcesses metodundaki for'un sayacını azalır.
            System.out.println("bildiniz");
            limit--;
        } else System.out.println("bilemediniz:  " + (9 - limit) + "  Hakkınız kaldı  ");

        return limit;
    }

    public Map<Character, Integer> getLetterAmountInList(List<Character> letterDictionary, List<String> filteredDictionary) {
        for (char letter : letterDictionary) {                               // Bu metod letterDictionary'deki her harf için dictionarydeki her kelimeyi gezer ve hangi harf kaç kelimede bulunuyorsa
            int letterPoint = 0;                                             // hashmap'de karşısına değer olarak yazar
            for (int i = 0; i < filteredDictionary.size(); i++) {            // örnek [a=112],[b=54]... a içeren 112 kelime var, b içeren 54 kelime
                if (filteredDictionary.get(i).contains(String.valueOf(letter))) {
                    letterPoint++;
                }
            }

            amount.put(letter, letterPoint);
        }
        return amount;
    }

    public void updateList(char prediction, int successCount, List<Integer> indexes, List<String> filteredDictionary) {
        if (successCount == 0) {                                                            // Eğer Tahmin yanlış ise tahmin edilen harfi içeren tüm kelimeleri filteredDictionary listesinden çıkarır.
            for (int i = 0; i < filteredDictionary.size(); i++) {
                if (filteredDictionary.get(i).contains(String.valueOf(prediction))) {
                    filteredDictionary.remove(i);
                }
            }
        }
        if (successCount != 0) {
            for (int i = 0; i < filteredDictionary.size(); i++) {      //Bu döngüde eğer tahmin doğru ise doğru bulunan indekslerinde tahmin edilen harfi içermeyen tüm kelimeler filteredDictionary listesinden çıkarılır.
                int letterFitting = 0;                                 // Örnek : prediction = a, successCount = 3, indexes = [1,3,6] ise 1. 3. ve 6. harfi a olmayan tüm kelimeler listeden silinir.
                for (int j = 0; j < indexes.size(); j++) {             // 106. satırdaki for doğru bilinen indeksler kadar döner ya da successCount'da diyebiliriz.
                    if (filteredDictionary.get(i).charAt(indexes.get(j)) == prediction) {  // 107. satırda sadece kelimenin indexes listesindeki indekslerine bakılır.
                        letterFitting++;                               // her doğru eşleşmede letterFitting artar.
                    }
                }
                if (letterFitting != successCount) {      // letterFitting başarılı tahmine eşit değilse kelime silinir
                                                          // Örnek1 : prediction a, successCount = 3, indexes = [1,3,5]  araba[1,3,5] =a   yani 1. 3. ve 5. indeksinde a var letterFitting = successCount kelime silinmez
                    filteredDictionary.remove(i);         // Örnek2 : prediction a, successCount = 3, indexes = [1,3,5]  aroma[1,5]=a  yani 1. 5. indeksinde a var ama 3.indeksinde yok letterFitting=2  successCount= 3 kelime silinir.
                    i--;                                  // Son olarak silme işlemi varsa sayaç 1 azaltılır. Kelime silindikten sonra yeni kelime bir önceki indekse karşılık geldiğinden kaymayı önler.
                }
            }
        }
    }
}
