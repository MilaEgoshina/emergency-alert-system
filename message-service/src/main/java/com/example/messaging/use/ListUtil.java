package com.example.messaging.use;

import java.util.ArrayList;
import java.util.List;


public class ListUtil {

    /**
     * Метод для разделения исходного списка на несколько подсписков с примерно равным количеством элементов в каждом.
     * @param tList исходный список
     * @param parts значение, определяющее количество подсписков, на которые нужно разделить исходный список.
     * @return возвращает список, содержащий все подсписки.
     */
    public static <T> List<List<T>> separator(List<T> tList, int parts){
        int size = tList.size();
        // вычисление размера каждого подсписка
        int partition = (int) Math.ceil((double) size / parts);
        List<List<T>> subLists = new ArrayList<>();

        for (int i = 0; i < size; i += partition) {

            //  вычисление конечного индекса end для каждого подсписка
            int end = Math.min(i + partition, size);
            // создается новый подсписок, который представляет собой поддиапазон исходного списка от индекса i до индекса end.
            subLists.add(tList.subList(i, end));
        }

        return subLists;
    }
}
