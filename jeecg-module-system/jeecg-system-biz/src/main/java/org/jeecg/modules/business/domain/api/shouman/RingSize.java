package org.jeecg.modules.business.domain.api.shouman;

import lombok.Getter;

public enum RingSize {

    FORTY_SEVEN(47, "内径1.50cm\n周长47mm\n"),
    FORTY_EIGHT(48, "内径1.53cm\n周长48mm\n"),
    FORTY_NINE(49, "内径1.57cm\n周长49mm\n"),
    FIFTY(50, "内径1.60cm\n周长50mm\n"),
    FIFTY_ONE(51, "内径1.64cm\n周长51mm\n"),
    FIFTY_TWO(52, "内径1.65cm\n周长52mm\n"),
    FIFTY_THREE(53, "内径1.70cm\n周长53mm\n"),
    FIFTY_FOUR(54, "内径1.72cm\n周长54mm\n"),
    FIFTY_FIVE(55, "内径1.75cm\n周长55mm\n"),
    FIFTY_SIX(56, "内径1.78cm\n周长56mm\n"),
    FIFTY_SEVEN(57, "内径1.83cm\n周长57mm\n"),
    FIFTY_EIGHT(58, "内径1.85cm\n周长58mm\n"),
    FIFTY_NINE(59, "内径1.90cm\n周长59mm\n"),
    SIXTY(60, "内径1.92cm\n周长60mm\n"),
    SIXTY_ONE(61, "内径1.94cm\n周长61mm\n"),
    SIXTY_TWO(62, "内径1.97cm\n周长62mm\n"),
    SIXTY_THREE(63, "内径2cm\n周长63mm\n"),
    SIXTY_FOUR(64, "内径2.04cm\n周长64mm\n"),
    SIXTY_FIVE(65, "内径2.07cm\n周长65mm\n"),
    SIXTY_SIX(66, "内径2.10cm\n周长66mm\n"),
    SIXTY_SEVEN(67, "内径2.13cm\n周长67mm\n"),
    SIXTY_EIGHT(68, "内径2.16cm\n周长68mm\n"),
    SEVENTY(70, "内径2.22cm\n周长70mm\n");


    private final int size;
    @Getter
    private final String text;

    RingSize(int size, String text) {
        this.size = size;
        this.text = text;
    }

    public static RingSize getBySize(int size) {
        for (RingSize val : values()) {
            if (val.size == size) {
                return val;
            }
        }
        return null;
    }
}
