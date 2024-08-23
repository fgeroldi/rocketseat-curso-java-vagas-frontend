package com.felipegeroldi.front_gestao_vagas.modules.candidate.service;

import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApplyJobService {
    public String execute(String token, UUID jobId) {
        var rt = new RestTemplate();
        var headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UUID> request = new HttpEntity<>(jobId, headers);

        String result = rt.postForObject("http://localhost:8080/candidate/job/apply", request, String.class);
        System.out.println(result);
        return result;
    }
}
