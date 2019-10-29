package pl.plewka.rsocket

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class RSocketTestApplication

fun main(args: Array<String>) {
	runApplication<RSocketTestApplication>(*args)
}

@Bean
fun rsocketServer() {

}
