package br.com.efo.bens.common;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class TestRequestBuilder
{
    private String path;
    private List<String> parameters;
    private HttpMethod httpMethod;
    private HttpEntity<?> httpEntity;
    private Class<?> classType;
    private ParameterizedTypeReference<?> objectType;
    private HttpStatus httpStatusExpected;
}
