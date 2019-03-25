package org.w01f.dds.layer4.index;

import org.w01f.dds.layer3.indexapi.IIndexAccess;

import java.util.List;

public class IndexAccess implements IIndexAccess {
    @Override
    public void addUnique(String indexName, String value, String id) {

    }

    @Override
    public void addNonunique(String indexName, String value, String id) {

    }

    @Override
    public void deleteUnique(String indexName, String value) {

    }

    @Override
    public void deleteNonUnique(String indexName, String value) {

    }

    @Override
    public String queryUnique(String indexName, String value) {
        return null;
    }

    @Override
    public List<String> queryNonunique(String indexName, String value) {
        return null;
    }
}
