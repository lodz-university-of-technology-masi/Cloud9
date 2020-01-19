package com.serverless.handlers.pojo;

public class ImportCSVHandlerPojo {
    private String nameFile;

    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }

    @Override
    public String toString() {
        return "ImportCSVHandlerPojo{" +
                "nameFile='" + nameFile + '\'' +
                '}';
    }
}
