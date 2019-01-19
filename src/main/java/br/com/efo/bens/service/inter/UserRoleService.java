package br.com.efo.bens.service.inter;

import java.util.List;
import java.util.Optional;

import br.com.efo.bens.ds.UserRole;

public interface UserRoleService
{
    public UserRole save(UserRole userRole);
    public List<UserRole> query();
    public UserRole query(String userRole);
    public Optional<UserRole> query(Integer id);

    public void delete();
    public void delete(Integer id);
}
