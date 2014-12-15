/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.udev;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import javax.annotation.Nonnull;
import org.anarres.udev.generated.UdevLibrary;

/**
 *
 * @author shevek
 */
/* pp */ abstract class UdevIterator<T> implements Iterator<T> {

    protected enum State {

        READY, WAITING, FREE;
    }
    protected final UdevLibrary library;
    private UdevLibrary.udev_list_entry entry;
    private State state;

    protected UdevIterator(UdevLibrary library, UdevLibrary.udev_list_entry entry) {
        this.library = library;
        this.entry = entry;
        this.state = State.READY;
    }

    protected UdevLibrary getLibrary() {
        return library;
    }

    @Override
    public boolean hasNext() {
        if (entry == null)
            return false;
        if (state == State.READY)
            return true;
        entry = library.udev_list_entry_get_next(entry);
        state = State.READY;
        return entry != null;
    }

    protected abstract T toEntry(UdevLibrary library, UdevLibrary.udev_list_entry entry);

    @Override
    public T next() {
        if (entry == null)
            throw new NoSuchElementException();
        state = State.WAITING;
        return toEntry(library, entry);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Nonnull
    public List<T> toList() {
        List<T> out = new ArrayList<T>();
        while (hasNext())
            out.add(next());
        return out;
    }
}