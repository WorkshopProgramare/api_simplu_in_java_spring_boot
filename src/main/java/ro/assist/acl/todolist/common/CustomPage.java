package ro.assist.acl.todolist.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class CustomPage<T> implements Slice<T> {
    Slice<T> delegate;

    public CustomPage(Slice<T> delegate) {
        this.delegate = delegate;
    }



    @Override
    @JsonIgnore
    public Pageable getPageable() {
        return Slice.super.getPageable();
    }

    @Override
    @JsonIgnore
    public boolean isEmpty() {
        return Slice.super.isEmpty();
    }

    @Override
    @JsonProperty("page")
    public int getNumber() {
        return delegate.getNumber();
    }

    @Override
    @JsonProperty("pageRecords")
    public int getSize() {
        return delegate.getSize();
    }

    @Override
    @JsonProperty("total")
    public int getNumberOfElements() {
        return delegate.getNumberOfElements();
    }

    @Override
    @JsonProperty("toDoLists")
    public List<T> getContent() {
        return delegate.getContent();
    }

    @Override
    public boolean hasContent() {
        return delegate.hasContent();
    }

    @Override
    @JsonIgnore
    public Sort getSort() {
        return delegate.getSort();
    }

    @Override
    @JsonIgnore
    public boolean isFirst() {
        return delegate.isFirst();
    }

    @Override
    @JsonIgnore
    public boolean isLast() {
        return delegate.isLast();
    }

    @Override
    @JsonIgnore
    public boolean hasNext() {
        return delegate.hasNext();
    }

    @Override
    @JsonIgnore
    public boolean hasPrevious() {
        return delegate.hasPrevious();
    }

    @Override
    @JsonIgnore
    public Pageable nextPageable() {
        return delegate.nextPageable();
    }

    @Override
    @JsonIgnore
    public Pageable previousPageable() {
        return delegate.previousPageable();
    }

    @Override
    public <U> Slice<U> map(Function<? super T, ? extends U> converter) {
        return delegate.map(converter);
    }

    @Override
    public Iterator<T> iterator() {
        return delegate.iterator();
    }
}