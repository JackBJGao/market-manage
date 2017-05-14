package cn.lmjia.market.wechat.config;

import cn.lmjia.market.core.config.WebModule;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.templateresource.SpringResourceTemplateResource;
import org.thymeleaf.templateresource.ITemplateResource;

import java.util.Map;
import java.util.function.Supplier;

/**
 * @author CJ
 */
@Configuration
@ComponentScan("cn.lmjia.market.wechat.controller")
public class WechatConfig implements WebModule {
    @Override
    public boolean hasOwnTemplateResolver() {
        return true;
    }

    @Override
    public Supplier<SpringResourceTemplateResolver> templateResolverSupplier() {
        return () -> new SpringResourceTemplateResolver() {
            @Override
            protected ITemplateResource computeTemplateResource(IEngineConfiguration configuration
                    , String ownerTemplate, String template, String resourceName, String characterEncoding
                    , Map<String, Object> templateResolutionAttributes) {
                // 只处理  xx@wechat 的模板
                if (!template.endsWith("@wechat"))
                    return null;
                // 然后把相关的名字去掉
                String newTemplate = template.substring(0, template.length() - "@wechat".length());
                SpringResourceTemplateResource resourceTemplateResource = (SpringResourceTemplateResource)
                        super.computeTemplateResource(configuration, ownerTemplate, newTemplate, resourceName, characterEncoding
                                , templateResolutionAttributes);
                if (resourceTemplateResource.exists())
                    return resourceTemplateResource;
                return null;
            }
        };
    }

    @Override
    public void templateResolver(SpringResourceTemplateResolver resolver) {
        resolver.setPrefix("classpath:/wechat-view/");
    }

    @Override
    public String[] resourcePathPatterns() {
        return new String[]{"/wechat-resource/**"};
    }

    @Override
    public void resourceHandler(String pattern, ResourceHandlerRegistration registration) {
        switch (pattern) {
            case "/wechat-resource/**":
                registration.addResourceLocations("classpath:/wechat-resource/");
                break;
        }
    }
}