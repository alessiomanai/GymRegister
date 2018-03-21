package com.alessiomanai.gymregister.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alessio on 08/03/18.
 */

class ListenerList<L> {

    private List<L> listenerList = new ArrayList<L>();

    public void add(L listener) {
        listenerList.add(listener);
    }

    public void fireEvent(FireHandler<L> fireHandler) {
        List<L> copy = new ArrayList<L>(listenerList);
        for (L l : copy) {
            fireHandler.fireEvent(l);
        }
    }

    public void remove(L listener) {
        listenerList.remove(listener);
    }

    public List<L> getListenerList() {
        return listenerList;
    }

    public interface FireHandler<L> {
        void fireEvent(L listener);
    }

}