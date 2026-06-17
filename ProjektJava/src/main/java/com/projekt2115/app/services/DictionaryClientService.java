package com.projekt2115.app.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DictionaryClientService {
    private final RestTemplate restTemplate;
    public DictionaryClientService(){
        this.restTemplate = new RestTemplate();
    }

    public boolean isWordInDictionary(String word){
        String url = "http://localhost:8080/api/directionary/verify?word=" + word;
        try{
            Boolean result = restTemplate.getForObject(url,Boolean.class);
            return result!= null && result;
        }catch (Exception e){
            return true;
        }
    }
}
