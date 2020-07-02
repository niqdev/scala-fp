package com.github.niqdev.model

import com.github.niqdev.enumeratum.{ Continent, Gender }
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ Decoder, Encoder, KeyDecoder, KeyEncoder }
//import squants.mass.Mass
//import squants.space.Length

object models {

  case class Person(
    name: NonEmptyString,
    //height: Length,
    //weight: Mass,
    gender: Gender,
    continent: Continent,
    motto: String,
    info: Map[NonEmptyString, String],
    active: Boolean
  )
  object Person extends PersonCodec

  @scala.annotation.nowarn
  trait PersonCodec {
    import io.circe.refined.{ refinedDecoder, refinedEncoder }

//    implicit val lengthEncoder: Encoder[Length] =
//      Encoder.encodeDouble.contramap[Length](_.value)
//    implicit val lengthDecoder: Decoder[Length] =
//      Decoder.decodeDouble.emapTry(Length.apply)
//
//    implicit val massEncoder: Encoder[Mass] =
//      Encoder.encodeDouble.contramap[Mass](_.value)
//    implicit val massDecoder: Decoder[Mass] =
//      Decoder.decodeDouble.emapTry(Mass.apply)

    implicit val keyMapEncoder: KeyEncoder[NonEmptyString] =
      _.value
    implicit val keyMapDecoder: KeyDecoder[NonEmptyString] =
      NonEmptyString.from(_).toOption

    implicit val personEncoder: Encoder[Person] =
      deriveEncoder[Person]
    implicit val personDecoder: Decoder[Person] =
      deriveDecoder[Person]
  }
}

/* FIXME Length and Mass generators

DecodingFailure(squants.QuantityParseException: Unable to parse Length:81.28684160233917
	at squants.Dimension.parse(Dimension.scala:66)
	at squants.Dimension.parse$(Dimension.scala:58)
	at squants.space.Length$.parse(Length.scala:103)
	at squants.space.Length$.apply(Length.scala:105)
	at com.github.niqdev.scalacheck.models$Person$.$anonfun$lengthDecoder$1(models.scala:28)
	at io.circe.Decoder$$anon$14.tryDecodeAccumulating(Decoder.scala:385)
	at com.github.niqdev.scalacheck.models$Person$anon$lazy$macro$43$1$$anon$4.decodeAccumulating(models.scala:43)
	at io.circe.generic.decoding.DerivedDecoder$$anon$1.decodeAccumulating(DerivedDecoder.scala:19)
	at com.github.niqdev.scalacheck.ModelProp.$anonfun$new$2(ModelProp.scala:12)
	at org.scalacheck.Prop$.$anonfun$forAllShrink$2(Prop.scala:765)
	at org.scalacheck.Prop$.secure(Prop.scala:475)
	at org.scalacheck.Prop$.result$1(Prop.scala:765)
	at org.scalacheck.Prop$.$anonfun$forAllShrink$1(Prop.scala:804)
	at org.scalacheck.Prop$.$anonfun$apply$1(Prop.scala:308)
	at org.scalacheck.PropFromFun.apply(Prop.scala:21)
	at org.scalacheck.Prop$.$anonfun$delay$1(Prop.scala:480)
	at org.scalacheck.Prop$.$anonfun$apply$1(Prop.scala:308)
	at org.scalacheck.PropFromFun.apply(Prop.scala:21)
	at org.scalacheck.Prop.$anonfun$viewSeed$1(Prop.scala:40)
	at org.scalacheck.Prop$.$anonfun$apply$1(Prop.scala:308)
	at org.scalacheck.PropFromFun.apply(Prop.scala:21)
	at org.scalacheck.Test$.workerFun$1(Test.scala:424)
	at org.scalacheck.Test$.$anonfun$check$1(Test.scala:453)
	at org.scalacheck.Test$.$anonfun$check$1$adapted(Test.scala:453)
	at org.scalacheck.Platform$.runWorkers(Platform.scala:40)
	at org.scalacheck.Test$.check(Test.scala:453)
	at org.scalacheck.ScalaCheckRunner$$anon$3.executeInternal(ScalaCheckFramework.scala:129)
	at org.scalacheck.ScalaCheckRunner$$anon$3.$anonfun$execute$5(ScalaCheckFramework.scala:112)
	at org.scalacheck.ScalaCheckRunner$$anon$3.$anonfun$execute$5$adapted(ScalaCheckFramework.scala:111)
	at scala.Option.foreach(Option.scala:437)
	at org.scalacheck.ScalaCheckRunner$$anon$3.$anonfun$execute$4(ScalaCheckFramework.scala:111)
	at org.scalacheck.ScalaCheckRunner$$anon$3.$anonfun$execute$4$adapted(ScalaCheckFramework.scala:108)
	at scala.collection.ArrayOps$.foreach$extension(ArrayOps.scala:1323)
	at org.scalacheck.ScalaCheckRunner$$anon$3.execute(ScalaCheckFramework.scala:108)
	at sbt.TestRunner.runTest$1(TestFramework.scala:139)
	at sbt.TestRunner.run(TestFramework.scala:154)
	at sbt.TestFramework$$anon$3$$anonfun$$lessinit$greater$1.$anonfun$apply$1(TestFramework.scala:317)
	at sbt.TestFramework$.sbt$TestFramework$$withContextLoader(TestFramework.scala:277)
	at sbt.TestFramework$$anon$3$$anonfun$$lessinit$greater$1.apply(TestFramework.scala:317)
	at sbt.TestFramework$$anon$3$$anonfun$$lessinit$greater$1.apply(TestFramework.scala:317)
	at sbt.TestFunction.apply(TestFramework.scala:329)
	at sbt.Tests$.$anonfun$toTask$1(Tests.scala:311)
	at sbt.std.Transform$$anon$3.$anonfun$apply$2(Transform.scala:46)
	at sbt.std.Transform$$anon$4.work(Transform.scala:67)
	at sbt.Execute.$anonfun$submit$2(Execute.scala:281)
	at sbt.internal.util.ErrorHandling$.wideConvert(ErrorHandling.scala:19)
	at sbt.Execute.work(Execute.scala:290)
	at sbt.Execute.$anonfun$submit$1(Execute.scala:281)
	at sbt.ConcurrentRestrictions$$anon$4.$anonfun$submitValid$1(ConcurrentRestrictions.scala:178)
	at sbt.CompletionService$$anon$2.call(CompletionService.scala:37)
	at java.util.concurrent.FutureTask.run(FutureTask.java:266)
	at java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:511)
	at java.util.concurrent.FutureTask.run(FutureTask.java:266)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
	at java.lang.Thread.run(Thread.java:748)
, List(DownField(height))),DecodingFailure(squants.QuantityParseException: Unable to parse Mass:150.921878635604
	at squants.Dimension.parse(Dimension.scala:66)
	at squants.Dimension.parse$(Dimension.scala:58)
	at squants.mass.Mass$.parse(Mass.scala:82)
	at squants.mass.Mass$.apply(Mass.scala:84)
	at com.github.niqdev.scalacheck.models$Person$.$anonfun$massDecoder$1(models.scala:33)
	at io.circe.Decoder$$anon$14.tryDecodeAccumulating(Decoder.scala:385)
	at com.github.niqdev.scalacheck.models$Person$anon$lazy$macro$43$1$$anon$4.decodeAccumulating(models.scala:43)
	at io.circe.generic.decoding.DerivedDecoder$$anon$1.decodeAccumulating(DerivedDecoder.scala:19)
	at com.github.niqdev.scalacheck.ModelProp.$anonfun$new$2(ModelProp.scala:12)
	at org.scalacheck.Prop$.$anonfun$forAllShrink$2(Prop.scala:765)
	at org.scalacheck.Prop$.secure(Prop.scala:475)
	at org.scalacheck.Prop$.result$1(Prop.scala:765)
	at org.scalacheck.Prop$.$anonfun$forAllShrink$1(Prop.scala:804)
	at org.scalacheck.Prop$.$anonfun$apply$1(Prop.scala:308)
	at org.scalacheck.PropFromFun.apply(Prop.scala:21)
	at org.scalacheck.Prop$.$anonfun$delay$1(Prop.scala:480)
	at org.scalacheck.Prop$.$anonfun$apply$1(Prop.scala:308)
	at org.scalacheck.PropFromFun.apply(Prop.scala:21)
	at org.scalacheck.Prop.$anonfun$viewSeed$1(Prop.scala:40)
	at org.scalacheck.Prop$.$anonfun$apply$1(Prop.scala:308)
	at org.scalacheck.PropFromFun.apply(Prop.scala:21)
	at org.scalacheck.Test$.workerFun$1(Test.scala:424)
	at org.scalacheck.Test$.$anonfun$check$1(Test.scala:453)
	at org.scalacheck.Test$.$anonfun$check$1$adapted(Test.scala:453)
	at org.scalacheck.Platform$.runWorkers(Platform.scala:40)
	at org.scalacheck.Test$.check(Test.scala:453)
	at org.scalacheck.ScalaCheckRunner$$anon$3.executeInternal(ScalaCheckFramework.scala:129)
	at org.scalacheck.ScalaCheckRunner$$anon$3.$anonfun$execute$5(ScalaCheckFramework.scala:112)
	at org.scalacheck.ScalaCheckRunner$$anon$3.$anonfun$execute$5$adapted(ScalaCheckFramework.scala:111)
	at scala.Option.foreach(Option.scala:437)
	at org.scalacheck.ScalaCheckRunner$$anon$3.$anonfun$execute$4(ScalaCheckFramework.scala:111)
	at org.scalacheck.ScalaCheckRunner$$anon$3.$anonfun$execute$4$adapted(ScalaCheckFramework.scala:108)
	at scala.collection.ArrayOps$.foreach$extension(ArrayOps.scala:1323)
	at org.scalacheck.ScalaCheckRunner$$anon$3.execute(ScalaCheckFramework.scala:108)
	at sbt.TestRunner.runTest$1(TestFramework.scala:139)
	at sbt.TestRunner.run(TestFramework.scala:154)
	at sbt.TestFramework$$anon$3$$anonfun$$lessinit$greater$1.$anonfun$apply$1(TestFramework.scala:317)
	at sbt.TestFramework$.sbt$TestFramework$$withContextLoader(TestFramework.scala:277)
	at sbt.TestFramework$$anon$3$$anonfun$$lessinit$greater$1.apply(TestFramework.scala:317)
	at sbt.TestFramework$$anon$3$$anonfun$$lessinit$greater$1.apply(TestFramework.scala:317)
	at sbt.TestFunction.apply(TestFramework.scala:329)
	at sbt.Tests$.$anonfun$toTask$1(Tests.scala:311)
	at sbt.std.Transform$$anon$3.$anonfun$apply$2(Transform.scala:46)
	at sbt.std.Transform$$anon$4.work(Transform.scala:67)
	at sbt.Execute.$anonfun$submit$2(Execute.scala:281)
	at sbt.internal.util.ErrorHandling$.wideConvert(ErrorHandling.scala:19)
	at sbt.Execute.work(Execute.scala:290)
	at sbt.Execute.$anonfun$submit$1(Execute.scala:281)
	at sbt.ConcurrentRestrictions$$anon$4.$anonfun$submitValid$1(ConcurrentRestrictions.scala:178)
	at sbt.CompletionService$$anon$2.call(CompletionService.scala:37)
	at java.util.concurrent.FutureTask.run(FutureTask.java:266)
	at java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:511)
	at java.util.concurrent.FutureTask.run(FutureTask.java:266)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
	at java.lang.Thread.run(Thread.java:748)
, List(DownField(weight)))

 */
