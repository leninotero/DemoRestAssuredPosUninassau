import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
import static io.restassured.module.jsv.JsonSchemaValidator.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ServeRestTest {
    private static String token;
    private static String productId;

    @Test
    @Order(1)
    void testDadoEmailESenhaValidoQuandoFacoLoginEntaoValidoStatusCode200(){
        baseURI = "https://serverest.dev";

         token = given()
                        .filter(new AllureRestAssured())
                        .body("{\n" +
                                "    \"email\": \"fulano@qa.com\",\n" +
                                "    \"password\": \"teste\"\n" +
                                "}")
                        .contentType(ContentType.JSON)
                .when()
                        .post("/login")
                .then()
                        .statusCode(200)
                        .body("message", equalTo("Login realizado com sucesso"))
                        .extract()
                            .path("authorization");
    }

    @Test
    @Order(2)
    void testDadoTokenValidoQuandoCadastroUmProdutoEntaoValidoStatusCode201(){
        productId = given()
                            .body("{\n" +
                                    "    \"nome\": \"Mouse Dell PX450\",\n" +
                                    "    \"preco\": 250,\n" +
                                    "    \"descricao\": \"Mouse a laser com otimo DPI\",\n" +
                                    "    \"quantidade\": 200\n" +
                                    "}")
                            .contentType(ContentType.JSON)
                            .header("Authorization", token)
                    .when()
                            .post("/produtos")
                    .then()
                            .statusCode(201)
                            .body("message", equalTo("Cadastro realizado com sucesso"))
                            .extract()
                                .path("_id");
    }

    @Test
    @Order(3)
    void testDadoUmProdutoCadastradoQuandoConsultoInformacoesEntaoValidoStatusCode200(){
        given()
                .pathParams("id", productId)
        .when()
                .get("produtos/{id}")
        .then()
                .statusCode(200)
                .body("nome", equalTo("Mouse Dell PX450"))
                .body("preco", equalTo(250))
                .body("descricao", equalTo("Mouse a laser com otimo DPI"))
                .body("quantidade", equalTo(200))
                .body("_id", equalTo(productId))
            .assertThat()
                .body(matchesJsonSchemaInClasspath("products-schema.json"));
    }

    @Test
    @Order(4)
    void testDadoTokenValidoQuandoModificoDadosEntaoValidoStatusCode200(){
        given()
                .pathParams("id", productId)
                .body("{\n" +
                        "    \"nome\": \"Mouse Dell LT189\",\n" +
                        "    \"preco\": 275,\n" +
                        "    \"descricao\": \"Mouse a laser com 180 DPI\",\n" +
                        "    \"quantidade\": 200\n" +
                        "}")
                .contentType(ContentType.JSON)
                .header("Authorization", token)
        .when()
                .put("/produtos/{id}")
        .then()
                .statusCode(200)
                .body("message", equalTo("Registro alterado com sucesso"));
    }

    @Test
    @Order(5)
    void testDadoTokenValidoQuandoExcluoProdutoEntaoValidoStatusCode200(){
        given()
                .pathParams("id", productId)
                .header("Authorization", token)
        .when()
                .delete("/produtos/{id}")
        .then()
                .statusCode(200)
                .body("message", equalTo("Registro excluído com sucesso"));
    }
}
