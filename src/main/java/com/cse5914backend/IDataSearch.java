package com.cse5914backend;

import java.util.List;

public interface IDataSearch {
    boolean sendHistory(Record record);
    List<Record> getSearchHistory();
}
