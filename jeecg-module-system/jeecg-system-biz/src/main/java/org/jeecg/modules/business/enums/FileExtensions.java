package org.jeecg.modules.business.enums;

import lombok.Getter;

public class FileExtensions {
    public static enum EXTENSION {
        XLSX(".xlsx"),
        XLS(".xls"),
        CSV(".csv"),
        PDF(".pdf"),
        JPG(".jpg"),
        PNG(".png"),
        JPEG(".jpeg");
        @Getter
        private final String extension;

        EXTENSION(String extension) {
            this.extension = extension;
        }
    }
}
