package br.com.efo.bens.ds;

import br.com.efo.bens.common.utils.PasswordUtils;

public class UserNull extends User
{
    @Override
    public String getName()
    {
        return "Deleted";
    }

    @Override
    public String getPasswordText()
    {
       return PasswordUtils.DEFAULT_PASSWORD;
    }

    @Override
    public Integer getId()
    {
        return 0;
    }
    
}
