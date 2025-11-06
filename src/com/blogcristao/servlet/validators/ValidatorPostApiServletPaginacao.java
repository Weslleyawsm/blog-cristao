package com.blogcristao.servlet.validators;

import com.blogcristao.dao.postDAO;

public class ValidatorPostApiServletPaginacao {
    private boolean Validator = false;
    private postDAO dao = new postDAO();

    public boolean ValidarInteger(int numero){
        if (numero <0) {
            return Validator;
        }
        else {
            return !Validator;
        }
    }
}
