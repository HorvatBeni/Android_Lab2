package com.example.beni.keep.util;

import android.util.JsonReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResourceListReader<E> implements ResourceReader<List<E>> {

    private final ResourceReader<E> mResourceReader;

    public ResourceListReader(ResourceReader<E> resourceReader) {
        mResourceReader = resourceReader;
    }

    @Override
    public List<E> read(JsonReader reader) throws IOException {
        List<E> entityList = new ArrayList<E>();
        reader.beginArray();
        while (reader.hasNext()) {
            entityList.add(mResourceReader.read(reader));
        }
        reader.endArray();
        return entityList;
    }
}
