package br.com.efo.bens.ds;

import groovy.transform.ToString;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class UserPostResponse
{
    private Integer id;
    private String name;
    private String email;
    private String gender;
    private String userName;
    private String passwordText;
}
