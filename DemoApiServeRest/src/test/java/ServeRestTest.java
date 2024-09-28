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
    static final int statusCode200 = 200;
    static final int statusCode201 = 201;
    static final String path = "/produtos";

    @Test
    @Order(1)
    @DisplayName("TC001 - Login com Informações Válidas")
    void testDado_EmailESenhaValidos_Quando_FacoLogin_Entao_ValidoStatus200(){
        baseURI = "https://serverest.dev";

        token = given()
                    .filter(new AllureRestAssured())
                    .body("{\n" +
                            "  \"email\": \"fulano@qa.com\",\n" +
                            "  \"password\": \"teste\"\n" +
                            "}")
                    .contentType(ContentType.JSON)
                .when()
                        .post("/login")
                .then()
                        .statusCode(statusCode200)
                        .body("message", equalTo("Login realizado com sucesso"))
                        .extract()
                            .path("authorization");
    }

    @Test
    @Order(2)
    @DisplayName("TC002 - Cadastrar Produto")
    void testDado_UmTokenValido_Quando_CadastroUmProduto_Entao_ValidoStatus201(){
        productId = given()
                        .filter(new AllureRestAssured())
                        .body("{\n" +
                                "  \"nome\": \"WebCam Logitech\",\n" +
                                "  \"preco\": 800,\n" +
                                "  \"descricao\": \"Webcam com ótima qualidade de video\",\n" +
                                "  \"quantidade\": 100\n" +
                                "}")
                        .contentType(ContentType.JSON)
                        .header("Authorization", token)
                    .when()
                        .post(path)
                    .then()
                        .statusCode(statusCode201)
                        .body("message", equalTo("Cadastro realizado com sucesso"))
                        .extract()
                            .path("_id");
    }

    @Test
    @Order(3)
    @DisplayName("TC003 - Listar produto por ID")
    void testDado_ProdutoCadastrado_Quando_ConsultoInformacoes_Entao_validoStatus200(){
        given()
                .filter(new AllureRestAssured())
                .pathParams("id", productId)
            .when()
                .get(path+"/{id}")
            .then()
                .statusCode(statusCode200)
                .body("nome", equalTo("WebCam Logitech"))
                .body("preco", equalTo(800))
                .body("descricao", equalTo("Webcam com ótima qualidade de video"))
                .body("quantidade", equalTo(100))
                .body("_id", equalTo(productId))
                .contentType(ContentType.JSON)
            .assertThat()
                .body(matchesJsonSchemaInClasspath("products-schema.json"));
    }

    @Test
    @Order(4)
    @DisplayName("TC004 - Atualizar Produto")
    void testDado_ProdutoCadastrado_Quando_AtualizoAsInformacoes_Entao_ValidoStatus200(){
        given()
                .filter(new AllureRestAssured())
                .pathParams("id", productId)
                .body("{\n" +
                        "  \"nome\": \"WebCam Logitech 1800DPI\",\n" +
                        "  \"preco\": 750,\n" +
                        "  \"descricao\": \"Webcam com ótima qualidade de video e visão noturna\",\n" +
                        "  \"quantidade\": 90\n" +
                        "}")
                .contentType(ContentType.JSON)
                .header("Authorization", token)
            .when()
                .put(path+"/{id}")
            .then()
                .statusCode(statusCode200)
                .body("message", equalTo("Registro alterado com sucesso"));
    }

    @Test
    @Order(5)
    @DisplayName("TC005 - Remover Produto")
    void testDado_ProdutoCadastrado_Quando_DeletoProduto_Entao_ValidoStatus200(){
        given()
                .filter(new AllureRestAssured())
                .pathParams("id", productId)
                .header("Authorization", token)
            .when()
                .delete(path+"/{id}")
            .then()
                .statusCode(statusCode200)
                .body("message", equalTo("Registro excluído com sucesso"));
    }
}
