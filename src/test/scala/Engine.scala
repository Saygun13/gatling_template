import io.gatling.app.Gatling
import io.gatling.core.config.GatlingPropertiesBuilder

object Engine extends App {

	val props = new GatlingPropertiesBuilder()
		.resourcesDirectory("resources")
		.resultsDirectory("gatling")
		.binariesDirectory("test-classes")

	Gatling.fromMap(props.build)
}
