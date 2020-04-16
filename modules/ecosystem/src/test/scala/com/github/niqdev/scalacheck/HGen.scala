//package com.github.niqdev.scalacheck
//
//import com.github.ghik.silencer.silent
//import io.circe.{Json, JsonObject}
//import org.scalacheck.Gen
//import shapeless.labelled.FieldType
//import shapeless.ops.record.Fields.Aux
//import shapeless.ops.record.{Fields, Keys}
//import shapeless.{CNil, HList, HNil, LabelledGeneric, Lazy, Witness, labelled, tag}
//
//object MyGenerators {
//  lazy val genBoolean: Gen[Boolean] =
//    Gen.oneOf(true, false)
//}
//
//// TODO refactor To[circe] To[spray-json] To[avro4s] To[scodec]
//trait ToJson[T] {
//  def from(tGen: Gen[T]): Gen[Json]
//}
//
//// TODO it should relay on existing codec, no manual
//object ToJson {
//  def apply[T](implicit ev: ToJson[T]): ToJson[T] = ev
//
//  implicit val stringToJson: ToJson[String] =
//    _.map(Json.fromString)
//
//  implicit val intToJson: ToJson[Int] =
//    _.map(Json.fromInt)
//
//  implicit val booleanToJson: ToJson[Boolean] =
//    _.map(Json.fromBoolean)
//}
//
//trait HGen[T] {
//  def gen: Gen[Json]
//}
//
//@silent
//object HGen {
//  def apply[T](implicit ev: HGen[T]): HGen[T] = ev
//
//  def instance[T](f: =>Gen[Json]): HGen[T] =
//    new HGen[T] {
//      override def gen: Gen[Json] = f
//    }
//
//  implicit val defaultStringHGen: HGen[String] =
//    gmap[String](Gen.alphaNumStr)
//
//  implicit val defaultIntHGen: HGen[Int] =
//    gmap[Int](Gen.posNum[Int])
//
//  implicit val defaultBooleanHGen: HGen[Boolean] =
//    gmap[Boolean](MyGenerators.genBoolean)
//
//  def gmap[T: ToJson](tGen: Gen[T]): HGen[T] =
//    instance[T](ToJson[T].from(tGen))
//
//  // TODO
//  import shapeless.::
//  implicit val hNilHGen: HGen[HNil] =
//    instance(Json.Null)
//
//  implicit val cNilHGen: HGen[CNil] =
//    instance(Json.Null)
//
//  /*
//  val gen = LabelledGeneric[A]
//  val keys = Keys[gen.Repr].apply
//  keys.toList.map(_.name)
//
//  ---
//
//  https://scastie.scala-lang.org/XKCGi35FRzSV01xfjdZBnQ
//  import shapeless.LabelledGeneric
//  import shapeless.ops.hlist.ToList
//  import shapeless.ops.record.Keys
//  import shapeless.ops.record.Values
//
//  case class Foo(
//      x: Int,
//      y: Option[Double],
//      z: List[Unit]
//  )
//
//  val genFoo = LabelledGeneric[Foo]
//
//  val keys = Keys[genFoo.Repr]
//  val values = Values[genFoo.Repr]
//
//  val keyList = ToList[keys.Out, Symbol]
//
//LabelledGeneric[values.Out]
//
//  keyList(keys()).map(_.name)
//   */
//  implicit def hListHGen[K <: Symbol, H, T <: HList](
//    implicit
//    witness: Witness.Aux[K],
//    hHGen: Lazy[HGen[H]],
//    tHGen: HGen[T],
//    toJson: ToJson[H]
//  ): HGen[FieldType[K, H] :: T] = {
//    val fieldName = witness.value.name
//
//    // TODO iterate over Repr
//
//    new HGen[FieldType[K, H] :: T] {
//      override def gen: Gen[Json] = {
//        val head = hHGen.value.gen
//        val tail = tHGen.gen
//
//        ???
//      }
//    }
//  }
//
//  implicit def genericHGen[H](
////    implicit
////    generic: LabelledGeneric.Aux[A, H],
////    headHGen: Lazy[HGen[H]]
//  ): HGen[H] = {
//
//    val gen: LabelledGeneric.Aux[Example, FieldType[String with labelled.KeyTag[Symbol with tag.Tagged[ {
//      type myString
//    }], String], String] :: FieldType[Int with labelled.KeyTag[Symbol with tag.Tagged[ {
//      type myInt
//    }], Int], Int] :: HNil] = LabelledGeneric[Example]
//
//    val example: FieldType[String with labelled.KeyTag[Symbol with tag.Tagged[{
//  type myString
//}], String], String] :: FieldType[Int with labelled.KeyTag[Symbol with tag.Tagged[{
//  type myInt
//}], Int], Int] :: HNil = gen.to(Example("aaa", 8))
//
//
//    import shapeless.ops.record.Fields
//    val fields: Fields.Aux[labelled.FieldType[String with labelled.KeyTag[Symbol with tag.Tagged[{
//  type myString
//}], String], String] :: labelled.FieldType[Int with labelled.KeyTag[Symbol with tag.Tagged[{
//  type myInt
//}], Int], Int] :: HNil, (String with labelled.KeyTag[Symbol with tag.Tagged[{
//  type myString
//}], String], String) :: (Int with labelled.KeyTag[Symbol with tag.Tagged[{
//  type myInt
//}], Int], Int) :: HNil] = Fields[gen.Repr]
//
//    val x: (String with labelled.KeyTag[Symbol with tag.Tagged[{
//  type myString
//}], String], String) :: (Int with labelled.KeyTag[Symbol with tag.Tagged[{
//  type myInt
//}], Int], Int) :: HNil = fields.apply(example)
//
//    ???
//  }
//
//  implicit def ss[K <: Symbol, H, T <: HList](
//    implicit
//    fields: Fields.Aux[K, H]
//    ): HGen[FieldType[K, H] :: T] = ???
//
//  implicit def all[T]: HGen[T] = ???
//
////  implicit def exampleHGen(
////    implicit
////    stringHGen: HGen[String],
////    intHGen: HGen[Int]
////  ): HGen[Example] =
////    instance[Example] {
////      for {
////        myString <- stringHGen.gen
////        myInt    <- intHGen.gen
////      } yield Json.fromJsonObject(
////        JsonObject(
////          "myString" -> myString,
////          "myInt" -> myInt
////        )
////      )
////    }
//}
//object Mappable {
//  implicit class ToMapOps[A](val a: A) extends AnyVal {
//    import shapeless._
//    import ops.record._
//
//    def toMap[L <: HList](implicit
//                          gen: LabelledGeneric.Aux[A, L],
//                          tmr: ToMap[L]
//                         ): Map[String, Any] = {
//      val m: Map[tmr.Key, tmr.Value] = tmr(gen.to(a))
//      m.map { case (k: Symbol, v) => k.name -> v }
//    }
//  }
//}
//
//case class Example(myString: String, myInt: Int)
//
//object TestApp extends App {
//
//  println(HGen.defaultStringHGen.gen.sample)
//  println(HGen.gmap[String](Gen.numStr).gen.sample)
//  println(HGen[Example].gen.sample)
//}
//
///*
//import shapeless.LabelledGeneric
//import shapeless.ops.hlist.ToList
//import shapeless.ops.record.Keys
//import shapeless.ops.record.Values
//
//val gen = LabelledGeneric[Example]
//
//---
//val keys = Keys[gen.Repr]
//val keyList = ToList[keys.Out, Symbol]
//keyList(keys()).map(_.name)
//
//val swap = SwapRecord[gen.Repr]
//val valueList = ToList[swap.Out, Any]
//valueList(swap()).map(_.name)
//
//---
//import shapeless.ops.record.Fields
//val fields = Fields[gen.Repr]
//
//val unzipFields = UnzipFields[gen.Repr]
//
//val toMap = ToMap[fields.Out]
//val toMap = ToMap[keys.Out]
//*/
