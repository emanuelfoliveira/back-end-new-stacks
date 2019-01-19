package br.com.efo.bens.ds;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@ToString
@Getter
@Setter
public class UserRole 
{
    @Id
    private Integer id;
    private String name;
}
