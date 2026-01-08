package dev.nectar.utils.misc.interfaces;

import net.minecraft.nbt.NbtCompound;

public interface ISerializable<T> {

    NbtCompound toTag();

    T fromTag(NbtCompound tag);

}
