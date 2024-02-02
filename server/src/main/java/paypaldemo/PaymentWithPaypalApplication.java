package paypaldemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@SpringBootApplication
public class PaymentWithPaypalApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentWithPaypalApplication.class, args);
	}


//	@Bean
//	public ObjectMapper objectMapper (){
//		return  new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
//	}
//	@Bean
//	Jackson2JsonEncoder jackson2JsonEncoder() {
//		return new Jackson2JsonEncoder(objectMapper());
//	}
//
//	@Bean
//	Jackson2JsonDecoder jackson2JsonDecoder() {
//		return new Jackson2JsonDecoder(objectMapper());
//	}
//
//	@Bean
//	WebFluxConfigurer webFluxConfigurer(Jackson2JsonEncoder encoder, Jackson2JsonDecoder decoder) {
//		return new WebFluxConfigurer() {
//			@Override
//			public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
//				configurer.defaultCodecs().jackson2JsonEncoder(encoder);
//				configurer.defaultCodecs().jackson2JsonDecoder(decoder);
//			}
//		};
//	}

}
