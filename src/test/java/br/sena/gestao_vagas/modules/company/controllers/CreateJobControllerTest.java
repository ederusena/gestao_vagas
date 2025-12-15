package br.sena.gestao_vagas.modules.company.controllers;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import br.sena.gestao_vagas.modules.company.dto.CreateJobDTO;
import br.sena.gestao_vagas.modules.company.entities.CompanyEntity;
import br.sena.gestao_vagas.modules.company.repositories.CompanyRepository;
import br.sena.gestao_vagas.utils.TestUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CreateJobControllerTest {

  private MockMvc mvc;

  @Autowired
  private WebApplicationContext context;

  @Autowired
  private CompanyRepository companyRepository;

  @Before
  public void setup() {
    mvc = MockMvcBuilders
        .webAppContextSetup(context)
        .apply(SecurityMockMvcConfigurers.springSecurity())
        .build();
  }

  @Test
  public void should_be_able_to_create_a_new_job() throws Exception {

    var company = CompanyEntity.builder()
        .description("Company Test Description")
        .email("teste2@company.com")
        .password("senhateste123teste")
        .username("company_test")
        .name("Company Test LTDA")
        .build();

    company = companyRepository.saveAndFlush(company);

    var createJobDTO = CreateJobDTO.builder()
        .description("DESCRIPTION TEST")
        .benefits("BENEFITS TEST")
        .level("LEVEL_TEST")
        .build();

    mvc.perform(MockMvcRequestBuilders.post("/company/job")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtils.asJsonString(createJobDTO))
        .header("Authorization", "Bearer " + TestUtils.generateToken(company.getId(), "JAVAGAS_@123#")))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  public void should_not_be_able_to_create_a_new_job_if_company_not_found() throws Exception {
    var createJobDTO = CreateJobDTO.builder()
        .description("DESCRIPTION TEST")
        .benefits("BENEFITS TEST")
        .level("LEVEL_TEST")
        .build();

      mvc.perform(MockMvcRequestBuilders.post("/company/job")
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtils.asJsonString(createJobDTO))
          .header("Authorization", "Bearer " + TestUtils.generateToken(UUID.randomUUID(), "JAVAGAS_@123#")))
          .andExpect(MockMvcResultMatchers.status().isNotFound());
  }
}
