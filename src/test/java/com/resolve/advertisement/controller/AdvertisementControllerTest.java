package com.resolve.advertisement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resolve.advertisement.ApplicationConstantTest;
import com.resolve.advertisement.entity.AdvertisementEntity;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AdvertisementControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    AdvertisementController advertisementController;

    @Test
    public void createAdvertisement() throws Exception{
        AdvertisementEntity advertisement= new AdvertisementEntity();
        advertisement.setAddId(6L);
        advertisement.setLongitude(78.9);
        advertisement.setLatitude(89.99);
        advertisement.setHref("http://www.google.com");
        advertisement.setAdvertisementName("test add");
       advertisement.setUpdatedAt(new Date());
        advertisement.setCreatedAt(new Date());
        mockMvc.perform(MockMvcRequestBuilders
                        .post(ApplicationConstantTest.CONTROLLER_BASE_URL + "/createAdvertisement")
                        .content(asJsonString(advertisement))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
    }

    @Test
    void updateAdvertisement() throws Exception{
        AdvertisementEntity advertisement= new AdvertisementEntity();
        advertisement.setAddId(6L);
        advertisement.setLongitude(78.9);
        advertisement.setLatitude(89.99);
        advertisement.setHref("http://www.google.com");
        advertisement.setAdvertisementName("updae add");
        advertisement.setUpdatedAt(new Date());
        advertisement.setCreatedAt(new Date());
        mockMvc.perform(MockMvcRequestBuilders
                        .put(ApplicationConstantTest.CONTROLLER_BASE_URL + "/updateAdvertisement")
                        .content(asJsonString(advertisement))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    void deleteAdvertisement() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                        .delete(ApplicationConstantTest.CONTROLLER_BASE_URL +"/deleteAdvertisement/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    public static String asJsonString(final  Object obj){
        try{
            return  new ObjectMapper().writeValueAsString(obj);
        }catch (Exception ex){
            throw  new RuntimeException(ex);
        }
    }
}
