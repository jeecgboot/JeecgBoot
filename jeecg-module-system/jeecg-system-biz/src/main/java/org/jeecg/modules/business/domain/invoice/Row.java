package org.jeecg.modules.business.domain.invoice;

import lombok.Data;

@Data
public class Row<E, F, G, H, I> {
    private final E col1;

    private final F col2;

    private final G col3;

    private final H col4;

    private final I col5;

    public Row(E col1, F col2, G col3, H col4, I col5) {
        this.col1 = col1;
        this.col2 = col2;
        this.col3 = col3;
        this.col4 = col4;
        this.col5 = col5;
    }
}
