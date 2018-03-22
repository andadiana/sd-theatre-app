package dataaccess.repository;

import java.util.List;

public class Cache<T> {

    private List<T> storage;

    public void save (List<T> entries) {
        this.storage = entries;
    }

    public boolean isEmpty() {
        return storage.isEmpty();
    }

    public List<T> load() {
        return storage;
    }

    public void restoreCache() {
        storage = null;
    }
}
