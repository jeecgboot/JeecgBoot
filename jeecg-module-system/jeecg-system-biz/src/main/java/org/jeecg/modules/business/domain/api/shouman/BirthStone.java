package org.jeecg.modules.business.domain.api.shouman;

import lombok.Getter;

public enum BirthStone {

    GARNET("1月","January", "Janvier", "Januar", "Gennaio", "Enero"),
    AMETHYST("2月","February", "Février", "Februar", "Febbraio", "Febrero"),
    AQUAMARINE("3月","March", "Mars", "März", "Marzo", "Marzo"),
    DIAMOND("4月","April", "Avril", "April", "Aprile", "Abril"),
    EMERALD("5月","May", "Mai", "Mai", "Maggio", "Mayo"),
    ALEXANDRITE("6月","June", "Juin", "Juni", "Giugno", "Junio"),
    RUBY("7月","July", "Juillet", "Juli", "Luglio", "Julio"),
    PERIDOT("8月","August", "Août", "August", "Agosto", "Agosto"),
    SAPPHIRE("9月","September", "Septembre", "September", "Settembre", "Septiembre"),
    OPAL("10月","October", "Octobre", "Oktober", "Ottobre", "Octubre"),
    TOPAZ("11月","November", "Novembre", "November", "Novembre", "Noviembre"),
    TURQUOISE("12月","December", "Décembre", "Dezember", "Dicembre", "Diciembre");

    @Getter
    private final String chinese;
    private final String english;
    private final String french;
    private final String german;
    private final String italian;
    private final String spanish;

    BirthStone(String chinese, String english, String french, String german, String italian, String spanish) {
        this.chinese = chinese;
        this.english = english;
        this.french = french;
        this.german = german;
        this.italian = italian;
        this.spanish = spanish;
    }

    public static BirthStone getByName(String name) {
        for (BirthStone val : values()) {
            if (name.contains(val.english) || name.contains(val.french) || name.contains(val.german)
                    || name.contains(val.italian) || name.contains(val.spanish)) {
                return val;
            }
        }
        return null;
    }
}
