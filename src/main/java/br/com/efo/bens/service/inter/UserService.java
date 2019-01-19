package br.com.efo.bens.service.inter;

import java.util.List;
import java.util.Optional;

import br.com.efo.bens.ds.User;

public interface UserService
{
    public User save(User user);
    public User queryByUserName(String userName);
    public User query(Integer userId);
    public void deleteById(Integer id);
    public List<User> queryAll();
    public User queryByEmail(String email);
    public Optional<User> getUserById(Integer id);
    public void delete();
}
