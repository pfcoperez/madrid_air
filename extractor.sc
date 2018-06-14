import $ivy.`io.circe::circe-core:0.9.3`
import $ivy.`io.circe::circe-generic:0.9.3`
import $ivy.`io.circe::circe-parser:0.9.3`
import $ivy.`joda-time:joda-time:2.10`
  
val chemsTable = Map(
  1 -> "SO2",
  6 -> "CO",
  7 -> "NO",
  8 -> "NO2",
  9 -> "PM2.5",
  10 -> "PM10",
  12 -> "NOx",
  14 -> "O3",
  20 -> "TOL",
  30 -> "BEN",
  35 -> "EBE",
  37 -> "MXY",
  38 -> "PXY",
  39 -> "OXY",
  42 -> "TCH",
  43 -> "CH4",
  44 -> "NMHC",
)

val locations = Map(
  1 -> Location(40.4223264,-3.691596),
  2 -> Location(40.408387,-3.6941825),
  3 -> Location(40.4185841,-3.7056038),
  35 -> Location(40.4185841,-3.7056038),
  4 -> Location(40.4230897,-3.7160478),
  5 -> Location(40.4748841,-3.7095149),
  39 -> Location(40.4748841,-3.7095149),
  6 -> Location(40.4728901,-3.6859492),
  7 -> Location(40.4297758,-3.6818219),
  8 -> Location(40.4144339,-3.6768884),
  9 -> Location(40.4019142,-3.6960448),
  10 -> Location(40.447021,-3.7064304),
  38 -> Location(40.447021,-3.7064304),
  11 -> Location(40.4524458,-3.6709162),
  12 -> Location(40.4284427,-3.6706801),
  13 -> Location(40.3514097,-3.6877977),
  40 -> Location(40.3514097,-3.6877977),
  14 -> Location(40.3850951,-3.7194807),
  15 -> Location(40.4664204,-3.6917249),
  16 -> Location(40.4586826,-3.6611244),
  17 -> Location(40.34163,-3.714672),
  18 -> Location(40.3947827,-3.7341903),
  19 -> Location(40.4072764,-3.7438868),
  20 -> Location(0.4055786,-3.6602518),
  36 -> Location(0.4055786,-3.6602518),
  21 -> Location(40.4392971,-3.7195004),
  22 -> Location(40.4047324,-3.717898),
  23 -> Location(40.4482254,-3.6085951),
  24 -> Location(40.4197419,-3.7510306),
  25 -> Location(40.3833906,-3.6202455),
  26 -> Location(40.4599114,-3.5827472),
  27 -> Location(40.479502,-3.5763614),
  47 -> Location(40.3953882,-3.6803625),
  48 -> Location(40.4477303,-3.6932221),
  49 -> Location(40.4148374,-3.6867532),
  50 -> Location(40.4664204,-3.6917249),
  54 -> Location(40.3848848,-3.6025223),
  55 -> Location(40.4599114,-3.5827472),
  56 -> Location(40.3850951,-3.7194807),
  57 -> Location(40.4921889,-3.6701567),
  58 -> Location(40.5198757,-3.7821814),
  59 -> Location(40.4609216,-3.6072091),
  86 -> Location(40.4867764,-3.6998132),
  60 -> Location(40.4867764,-3.6998132)
)

import org.joda.time.DateTime

case class Location(lat: Double, lon: Double)

case class Measurement(value: Double, label: String)

case class Entry(timestamp: DateTime, location: Location, measurement: Measurement)

@main
def main(uri: String = "http://www.mambiente.munimadrid.es/opendata/horario.csv") {

  import io.circe._, io.circe.syntax._, io.circe.generic.auto._
  implicit val dateTimeEncoder: Encoder[DateTime] = Encoder.instance(a => a.getMillis.asJson)

  lazy val sourceLines = scala.io.Source.fromURL(uri).getLines().toList
  lazy val label2pos = sourceLines.head.split(";").zipWithIndex.toMap

  lazy val rawEntries = sourceLines.tail
  lazy val entries = rawEntries flatMap { rawEntry =>
    val positionalEntry = rawEntry.split(";").toVector
    val entry = label2pos.mapValues(positionalEntry)

    positionalEntry.drop(8).toList.grouped(2).zipWithIndex collect {
      case (List(value, "V"), hour) =>
        Entry(
          timestamp = new DateTime(entry("ANO").toInt, entry("MES").toInt, entry("DIA").toInt, hour, 0, 0),
          location = locations(entry("ESTACION").toInt),
          measurement = Measurement(value.toDouble, chemsTable(entry("MAGNITUD").toInt))
        )
    }
  }

  val asJsonStrings = entries map { (entry: Entry) =>
    entry.asJson.noSpaces
  }

  asJsonStrings.foreach(println)
}
