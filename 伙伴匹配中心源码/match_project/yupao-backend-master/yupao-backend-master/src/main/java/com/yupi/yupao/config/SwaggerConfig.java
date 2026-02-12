package com.yupi.yupao.config;
 
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * Swagger 接口文档配置（仅在 dev / test 环境启用）。
 *
 * @author Ethan
 */
@Configuration
@EnableSwagger2WebMvc
@Profile({"dev", "test"})
public class SwaggerConfig {

    /**
     * 创建 Swagger Docket（扫描 controller 包生成接口文档）。
     *
     * @return Swagger Docket
     */
    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 这里一定要标注你控制器的位置
                .apis(RequestHandlerSelectors.basePackage("com.yupi.yupao.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 构建 Swagger 的基础信息。
     *
     * @return ApiInfo
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("鱼皮用户中心")
                .description("鱼皮用户中心接口文档")
                .termsOfServiceUrl("https://github.com/liyupi")
                .contact(new Contact("yupi","https://github.com/liyupi","xxx@qq.com"))
                .version("1.0")
                .build();
    }
}
