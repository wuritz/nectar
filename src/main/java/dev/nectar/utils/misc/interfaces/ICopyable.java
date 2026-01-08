package dev.nectar.utils.misc.interfaces;

public interface ICopyable<T extends ICopyable<T>> {

    T set(T value);

    T copy();

}
