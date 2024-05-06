package pau.pau5;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTests
{

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAddEmployeeSuccess() throws Exception
    {
        String employeeJson =
                "{\n" +
                        "  \"name\": \"Tomek\",\n" +
                        "  \"surname\": \"Rak\",\n" +
                        "  \"employeeCondition\": \"OBECNY\",\n" +
                        "  \"birthYear\": 1985,\n" +
                        "  \"salary\": 4.0,\n" +
                        "  \"classEmployeeId\": 33\n" +
                        "}" +
                        "\n";

        MvcResult result = mockMvc.perform(post("/api/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson))
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();

        JsonNode rootNode = new ObjectMapper().readTree(responseContent);
        int employeeId = rootNode.path("id").asInt();

        System.out.println("Created employee ID: " + employeeId);
    }

    @Test
    public void testAddEmployeeFail() throws Exception
    {
        String employeeJson =
                "{\n" +
                        "  \"name\": \"John\",\n" +
                        "  \"surname\": \"Doe\",\n" +
                        "  \"employeeCondition\": \"OBECNY\",\n" +
                        "  \"birthYear\": 1985,\n" +
                        "  \"salary\": -15.0,\n" +
                        "  \"classEmployeeId\": 31\n" +
                        "}" +
                        "\n";

        mockMvc.perform(post("/api/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteEmployeeSuccess() throws Exception
    {
        int id = 52;
        mockMvc.perform(delete("/api/employee/:" + id))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteEmployeeFailure() throws Exception
    {
        int id = 52;
        mockMvc.perform(delete("/api/employee/:" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDownloadCSV() throws Exception
    {
        mockMvc.perform(get("/api/employee/csv")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetGroupsSuccess() throws Exception
    {
        mockMvc.perform(get("/api/group")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddGroupSuccess() throws Exception
    {
        String groupJSON =
                "{\n" +
                "  \"workgroup\": \"TEST WORKGROUP\",\n" +
                "  \"maxEmployees\": 15\n" +
                "}" +
                "\n";

        mockMvc.perform(post("/api/group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(groupJSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void testAddGroupFail() throws Exception
    {
        String groupJSON =
                "{\n" +
                        "  \"workgroup\": \"TEST WORKGROUP\",\n" +
                        "  \"maxEmployees\": 0\n" +
                        "}" +
                        "\n";

        mockMvc.perform(post("/api/group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(groupJSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteGroupSuccess() throws Exception
    {
        int id = 47;
        mockMvc.perform(delete("/api/group/:" + id))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetEmployeesSuccess() throws Exception
    {
        int id = 31;
        mockMvc.perform(get("/api/group/:" + id + "/employee"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetEmployeesFailure() throws Exception
    {
        int id = 46;
        mockMvc.perform(get("/api/group/:" + id + "/employee"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetUtilizationSuccess() throws Exception
    {
        int id = 31;
        mockMvc.perform(get("/api/group/:" + id + "/fill"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetUtilizationFailure() throws Exception
    {
        int id = 46;
        mockMvc.perform(get("/api/group/:" + id + "/fill"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddRateSuccess() throws Exception
    {
        String JSON =
                "{\n" +
                        "  \"rating\": 3,\n" +
                        "  \"classEmployeeId\": 31,\n" +
                        "  \"comment\": \"very goofy\"\n" +
                        "}" +
                        "\n";

        mockMvc.perform(post("/api/rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void testAddRateFail() throws Exception
    {
        String JSON =
                "{\n" +
                 "  \"rating\": 6,\n" +
                 "  \"classEmployeeId\": 1,\n" +
                 "  \"comment\": \"very goofy\"\n" +
                 "}" +
                 "\n";

        mockMvc.perform(post("/api/rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON))
                        .andExpect(status().isBadRequest());
    }
}