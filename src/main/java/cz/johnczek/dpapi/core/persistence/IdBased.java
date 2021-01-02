package cz.johnczek.dpapi.core.persistence;

public interface IdBased<T extends Number> {

    T getId();

    void setId(T id);
}
