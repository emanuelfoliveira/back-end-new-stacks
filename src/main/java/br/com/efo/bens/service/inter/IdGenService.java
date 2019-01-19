package br.com.efo.bens.service.inter;

public interface IdGenService
{
    public Integer nextId(String collection);
    public void delete();
}
