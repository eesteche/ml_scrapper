package com.colab.app.dto;

import java.util.Objects;

import com.colab.app.model.Item;

public class WrapperItem {

	private Item e;

    public WrapperItem(Item e) {
        this.e = e;
    }

    public Item unwrap() {
        return this.e;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WrapperItem that = (WrapperItem) o;
        return Objects.equals(e.getDb_id(), that.e.getDb_id());
    }

    @Override
    public int hashCode() {
        return Objects.hash(e.getIdml());
    }
}
