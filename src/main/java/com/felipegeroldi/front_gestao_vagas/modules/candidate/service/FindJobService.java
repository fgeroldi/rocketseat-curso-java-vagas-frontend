package com.felipegeroldi.front_gestao_vagas.modules.candidate.service;

import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;
import org.springframework.web.util.UriComponentsBuilder;

import com.felipegeroldi.front_gestao_vagas.modules.candidate.dto.JobDTO;

import org.springframework.web.client.RestTemplate;

@Service
public class FindJobService {
    public List<JobDTO> execute(String token, String filter) {
        var rt = new RestTemplate();
        var headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(headers);
        var uriBuilder = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/candidate/job");
        uriBuilder.queryParam("filter", filter);

        try {
            var responseType = new ParameterizedTypeReference<List<JobDTO>>(){};
            var result = rt.exchange(uriBuilder.toUriString(),
                HttpMethod.GET,
                request,
                responseType);

            return result.getBody();
        } catch (Unauthorized e) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        }
    }
}
