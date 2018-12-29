package cumt.innovative.training.project.basketballappointment.swagger2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class Swagger2 {

    @Bean
    public Docket createRestApi() {

        ParameterBuilder parameterBuilder = new ParameterBuilder();
        Parameter parameter = parameterBuilder.name("authorization").description("authorization").
                parameterType("header").modelRef(new ModelRef("string")).build();
        List<Parameter> increasedParameters = new ArrayList<Parameter>();
        increasedParameters.add(parameter);

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("cumt.innovative.training.project.basketballappointment.controller"))
                .paths(PathSelectors.any())
                .build().globalOperationParameters(new ArrayList<Parameter>(increasedParameters));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Guidance of Basketball Appointment")
                .description("This system is for innovative training project of China University of Mining and Technology. The developer is Neko Gong. APIs are RESTful and you can try them in this documentation. This documentation is supported by Swagger. The project includes framework of Springboot, Bootstrap, VUE.js as well.")
                .version("1.0")
                .build();
    }
}
