package com.resolve.advertisement;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(classes =AdvertisementServiceApplication.class)
@WebAppConfiguration
public class AbstractTest {
    protected MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;
    @BeforeEach
    protected  void setUP(){
        mockMvc= MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
}
