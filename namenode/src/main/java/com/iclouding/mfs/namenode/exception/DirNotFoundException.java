package com.iclouding.mfs.namenode.exception;

public class DirNotFoundException extends DirException {

    public DirNotFoundException(String dir){
        super(String.format("%s not found!",dir));
    }

    public DirNotFoundException(String dir,Throwable cause){
        super(String.format("%s not found!",dir),cause);
    }

}
