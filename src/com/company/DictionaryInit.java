package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DictionaryInit {

    private String path = "dictionary.txt";
    private String path1 = "letters.txt";
    private List<String> cleanDictionary = new ArrayList<String>();
    public List<String> filteredDictionary = new ArrayList<String>();
    public List<Character> letterDictionary = new ArrayList<Character>();





    public void initializingDictionary() throws IOException { // Dizinde bulunan dictionary.txt dosyasını okur.
        File dictionaryFile = new File(path);
        FileReader fileReader = new FileReader(dictionaryFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        while (bufferedReader.ready()) {
            cleanDictionary.add(bufferedReader.readLine().split(String.valueOf(' '))[0]); // Split metoduyla satır içindeki Stringleri ayırıp ilk Stringi cleanDictionary listesine ekler.
        }

    }

    public void InitializingLetterDict() throws IOException { // Dizindeki letters.txt dosyasını okur.
        File charFile = new File(path1);
        FileReader fileReader = new FileReader(charFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        while (bufferedReader.ready()) {
            letterDictionary.add(bufferedReader.readLine().charAt(0)); // text içindeki tüm karakterleri letterDictionary listesine ekler.
        }

    }

    public void filterDictionaryByLetterCount(int letterCount){ // P2 tarafından belirlenen kelime uzunluğuna göre programdaki ilk dictionaryList filtreleme işlemini gerçekleştirir.
        for (int i = 0; i < cleanDictionary.size(); i++) {
            if (cleanDictionary.get(i).length() == letterCount) {

                filteredDictionary.add(cleanDictionary.get(i)); // listedeki kelime'nin uzunluğu belirlenen kelime uzunluğuna eşitse kelimeyi filteredDictionary listesine ekler.

            }
        }
    }

    public List<String> getCleanDictionary() throws IOException { // cleanDictionary listesini çağırır. içi boşsa initializingDictionary() metodunu çalıştırıp cleanDictionary dolu gider.
        if(cleanDictionary.isEmpty())
            initializingDictionary();
        return cleanDictionary;
    }

    public List<String> getFilteredDictionary(int letterCount) throws IOException { // filteredDictionary listesini çağırır. içi boşsa getCleanDictionary() metodunu çalıştırır .
        if(filteredDictionary.isEmpty()) {                                          // filterDictionaryByLetterCount(); metodu ile filtreleme yapıp filteredDictionary listesini doldurur return eder.
            getCleanDictionary();
        }
            filterDictionaryByLetterCount(letterCount);
        return filteredDictionary;
    }
    public List<Character> getLetterDictionary() throws IOException {  // letterDictionary return eder letterDictionary boşsa initializingLetterDict() metodunu çalıştırır.
        if(letterDictionary.isEmpty())
            InitializingLetterDict();

        return letterDictionary;
    }




}
