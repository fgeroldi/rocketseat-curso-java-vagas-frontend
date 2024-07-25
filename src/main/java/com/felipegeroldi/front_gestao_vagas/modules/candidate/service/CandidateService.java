package com.felipegeroldi.front_gestao_vagas.modules.candidate.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.felipegeroldi.front_gestao_vagas.modules.candidate.dto.TokenDTO;

@Service
public class CandidateService {
    public TokenDTO login(String username, String password) {
        var restTemplate = new RestTemplate();

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String,String> data = new HashMap<>();
        data.put("username", username);
        data.put("password", password);

        HttpEntity<Map<String,String>> request = new HttpEntity<>(data);

        TokenDTO result = restTemplate.postForObject("http://localhost:8080/candidate/auth", request, TokenDTO.class);
        return result;
    }
}
